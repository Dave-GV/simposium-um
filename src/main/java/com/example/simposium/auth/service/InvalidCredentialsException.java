package com.example.simposium.auth.service;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Email o contrasena incorrectos");
    }
}
