package com.server.ordering;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("env")
@Getter @Setter
public class Properties {

    // twilio
    private String account_sid;
    private String auth_token;
    private String path_service_sid;
}
