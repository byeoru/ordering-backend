package com.byeoru.ordering_server

import com.byeoru.ordering_server.domain.OrderType
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.net.HttpHeaders
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class FirebaseCloudMessageService(private val objectMapper: ObjectMapper) {

    @Throws(IOException::class)
    fun sendMessageTo(targetToken: String, title: String, body: String, channel: OrderType) {
        val message = makeMessage(targetToken, title, body, channel)
        val client = OkHttpClient()
        val requestBody: RequestBody = message.toRequestBody("application/json; charset=utf-8".toMediaType())
        val API_URL = "https://fcm.googleapis.com/v1/projects/ordering-f880f/messages:send"
        val request: Request = Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build()

        client.newCall(request).execute()
    }

    @Throws(JsonProcessingException::class)
    private fun makeMessage(targetToken: String, title: String, body: String, channel: OrderType): String {
        val fcmMessage = FcmMessage(
            message = FcmMessage.Message(
                data = FcmMessage.Data(
                    title = title,
                    body = body,
                    channel_id = channel.name
                ),
                token = targetToken
            ),
            validate_only = false
        )
        return objectMapper.writeValueAsString(fcmMessage)
    }

    @get:Throws(IOException::class)
    private val accessToken: String
        get() {
            val firebaseConfigPath = "firebase/firebase_service_key.json"
            val googleCredentials = GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath).inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
            googleCredentials.refreshIfExpired()
            return googleCredentials.accessToken.tokenValue
        }
}