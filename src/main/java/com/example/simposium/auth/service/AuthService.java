package com.example.simposium.auth.service;

import com.example.simposium.auth.dto.LoginRequest;
import com.example.simposium.auth.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Usuario demo hardcoded (para demo/dev, en producción sería de DB)
    private static final String DEMO_EMAIL = "demo@simposium.com";
    private static final String DEMO_PASSWORD = "demo123";

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica un usuario y genera token JWT
     * @param request credentials (email, password)
     * @return LoginResponse con token si credentials son correctas
     * @throws IllegalArgumentException si email/password son inválidos
     */
    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {
        // Validar email
        if (!request.getEmail().equals(DEMO_EMAIL)) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        // Validar password usando PasswordEncoder.matches()
        // Este método compara: password en texto plano vs hash guardado
        if (!passwordEncoder.matches(request.getPassword(), passwordEncoder.encode(DEMO_PASSWORD))) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        // Credenciales válidas → generar token
        String token = jwtService.generateToken(request.getEmail());

        // Retornar respuesta con token
        return new LoginResponse(token, "Bearer", request.getEmail());
    }
}

