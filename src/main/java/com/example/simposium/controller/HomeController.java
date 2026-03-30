package com.example.simposium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/login-demo/index.html";
    }

    @GetMapping("/login-demo/")
    public String loginDemo() {
        return "forward:/login-demo/index.html";
    }
}

