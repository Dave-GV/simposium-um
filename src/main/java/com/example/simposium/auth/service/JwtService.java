package com.example.simposium.auth.service;

import com.example.simposium.auth.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // Convertir string secret a SecretKey usando HMAC-SHA256
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Genera un token JWT con el email como subject (sujeto/usuario)
     * @param email email del usuario autenticado
     * @return token JWT en formato String
     */
    public String generateToken(String email) {
        // Fecha actual
        Date now = new Date();
        // Fecha de expiración = ahora + expiration (86400000 ms = 24 horas)
        Date expirationDate = new Date(now.getTime() + jwtProperties.getExpiration());

        // Construir token JWT
        return Jwts.builder()
                // "sub" (subject) = email del usuario
                .subject(email)
                // "iat" (issued at) = timestamp de creación
                .issuedAt(now)
                // "exp" (expiration) = timestamp de expiración
                .expiration(expirationDate)
                // Firmar con algoritmo HMAC-SHA256 y nuestra clave secreta
                .signWith(key, SignatureAlgorithm.HS256)
                // Construir y serializar a String
                .compact();
    }

    /**
     * Extrae el email (subject) de un token JWT válido
     * @param token token JWT a parsear
     * @return email extraído
     */
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valida si un token es válido (no expirado, firma correcta)
     * @param token token JWT a validar
     * @return true si es válido, false si no
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Token expirado, firma inválida, formato incorrecto, etc.
            return false;
        }
    }
}


