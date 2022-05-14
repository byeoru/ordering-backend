package com.server.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    @Value("${env.kakao_api_key}")
    private String kakaoApiKey;

    @GetMapping("/street")
    public String street(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "street";
    }

    @GetMapping("/app-download")
    public String appDownload() {
        return "downloadApp";
    }

    @GetMapping("/banner/1")
    public String banner1() {
        return "banner1";
    }

    @GetMapping("/banner/2")
    public String banner2() { return "banner2"; }
}
