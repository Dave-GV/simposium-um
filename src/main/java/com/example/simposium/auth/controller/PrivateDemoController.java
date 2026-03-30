package com.example.simposium.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/private")
public class PrivateDemoController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Map.of(
                "email", authentication.getName(),
                "roles", roles
        );
    }
}

