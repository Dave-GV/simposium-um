package com.example.simposium.admin.service.exception;

public class AdministradorYaExisteException extends RuntimeException {
    public AdministradorYaExisteException(String message) {
        super(message);
    }
}
