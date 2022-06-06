package com.server.ordering;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.server.ordering.domain.OrderType;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body, OrderType channel) throws IOException {

        String message = makeMessage(targetToken, title, body, channel);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        String API_URL = "https://fcm.googleapis.com/v1/projects/ordering-f880f/messages:send";
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request)
                .execute();

        System.out.println(Objects.requireNonNull(response.body()).string());
    }

    private String makeMessage(String targetToken, String title, String body, OrderType channel) throws JsonProcessingException {

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .data(FcmMessage.Data.builder()
                                .title(title)
                                .body(body)
                                .channel_id(channel.name())
                                .build())
                        .build())
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {

        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }
}
