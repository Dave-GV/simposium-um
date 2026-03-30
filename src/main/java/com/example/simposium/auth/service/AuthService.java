package com.example.simposium.auth.service;

import com.example.simposium.auth.config.DemoUserProperties;
import com.example.simposium.auth.dto.LoginRequest;
import com.example.simposium.auth.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final DemoUserProperties demoUserProperties;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, DemoUserProperties demoUserProperties, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.demoUserProperties = demoUserProperties;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica un usuario y genera token JWT
     * @param request credentials (email, password)
     * @return LoginResponse con token si credentials son correctas
     * @throws InvalidCredentialsException si email/password son invalidos
     */
    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {
        // Validar email
        if (!request.getEmail().equals(demoUserProperties.getEmail())) {
            throw new InvalidCredentialsException();
        }

        // Validar password usando PasswordEncoder.matches()
        // Este método compara: password en texto plano vs hash guardado
        if (!passwordEncoder.matches(request.getPassword(), passwordEncoder.encode(demoUserProperties.getPassword()))) {
            throw new InvalidCredentialsException();
        }

        // Credenciales válidas → generar token
        String token = jwtService.generateToken(request.getEmail());

        // Retornar respuesta con token
        return new LoginResponse(token, "Bearer", request.getEmail());
    }
}

