package com.example.simposium.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DemoUserProperties demoUserProperties;

    public SecurityConfig(DemoUserProperties demoUserProperties) {
        this.demoUserProperties = demoUserProperties;
    }

    /**
     * PasswordEncoder para encriptar/verificar contraseñas
     * BCryptPasswordEncoder es estándar de seguridad
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * UserDetailsService en memoria con usuario demo
     * Para demo/dev; en producción traería de DB
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Crear usuario demo con rol USER
        UserDetails demoUser = User.builder()
                // Email del usuario
                .username(demoUserProperties.getEmail())
                // Password en texto (será hasheado en runtime)
                .password(passwordEncoder().encode(demoUserProperties.getPassword()))
                // Rol por defecto
                .roles("USER")
                .build();

        // Retornar gestor en memoria con ese usuario
        return new InMemoryUserDetailsManager(demoUser);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/api/public/**",
                                "/login-demo/**",
                                "/registro-admin/**",
                                "/api/admin/**",
                                "/",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}

