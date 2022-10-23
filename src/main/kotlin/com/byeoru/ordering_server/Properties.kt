package com.byeoru.ordering_server

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("env")
class Properties {
    // twilio
    var account_sid: String? = null
    var auth_token: String? = null
    var path_service_sid: String? = null
}