package com.example.simposium.auth.dto;

public class LoginResponse {

    private final String message;
    private final String email;

    public LoginResponse(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}

