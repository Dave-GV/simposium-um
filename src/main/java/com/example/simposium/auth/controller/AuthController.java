package com.example.simposium.auth.controller;

import com.example.simposium.auth.dto.LoginRequest;
import com.example.simposium.auth.dto.LoginResponse;
import com.example.simposium.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de login: valida credentials y devuelve JWT
     * @param request email + password
     * @return LoginResponse con accessToken si credenciales son correctas
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // AuthService autentica y genera token
            LoginResponse response = authService.authenticateAndGenerateToken(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Email/password incorrectos → 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

