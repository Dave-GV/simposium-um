package com.example.simposium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistroAdminViewController {

    @GetMapping("/registro-admin")
    public String registroAdmin() {
        return "forward:/registro-admin/index.html";
    }

    @GetMapping("/registro-admin/")
    public String registroAdminSlash() {
        return "forward:/registro-admin/index.html";
    }
}
