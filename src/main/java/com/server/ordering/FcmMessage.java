package com.server.ordering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@AllArgsConstructor
public class FcmMessage {

    private boolean validate_only;
    private Message message;

    @Getter @Builder
    @AllArgsConstructor
    public static class Message {

        private Notification notification;
        private Data data;
        private String token;
    }

    @Getter @Builder
    @AllArgsConstructor
    public static class Notification {

        private String title;
        private String body;
        private String image;
    }

    @Getter @Builder
    @AllArgsConstructor
    public static class Data {

        private String title;
        private String body;
        private String channel_id;
    }
}
