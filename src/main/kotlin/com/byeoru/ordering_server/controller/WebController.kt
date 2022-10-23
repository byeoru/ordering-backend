package com.byeoru.ordering_server.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @Value("\${env.kakao_api_key}")
    private val kakaoApiKey: String? = null

    @GetMapping("/street")
    fun street(model: Model): String {
        model.addAttribute("kakaoApiKey", kakaoApiKey)
        return "street"
    }

    @GetMapping("/app-download")
    fun appDownload(): String {
        return "downloadApp"
    }

    @GetMapping("/banner/1")
    fun banner1(): String {
        return "banner1"
    }

    @GetMapping("/banner/2")
    fun banner2(): String {
        return "banner2"
    }
}