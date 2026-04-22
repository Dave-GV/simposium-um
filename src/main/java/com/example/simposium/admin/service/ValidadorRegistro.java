package com.example.simposium.admin.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validaciones complementarias a Bean Validation.
 * Equivale a la clase ValidadorRegistro del diagrama.
 */
@Component
public class ValidadorRegistro {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[+0-9 ()-]{7,20}$");

    public boolean validarCorreo(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo).matches();
    }

    public boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            return false;
        }
        boolean tieneLetra = contrasena.chars().anyMatch(Character::isLetter);
        boolean tieneNumero = contrasena.chars().anyMatch(Character::isDigit);
        return tieneLetra && tieneNumero;
    }

    public boolean validarTelefono(String telefono) {
        // Telefono es opcional; si viene vacio se considera valido
        if (telefono == null || telefono.isBlank()) {
            return true;
        }
        return PATRON_TELEFONO.matcher(telefono).matches();
    }
}
