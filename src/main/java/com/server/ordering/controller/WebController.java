package com.server.ordering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    @GetMapping("/street")
    public String street() {
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
    public String banner2() {
        return "banner2";
    }
}
