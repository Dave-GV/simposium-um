package com.example.simposium.admin.controller;

import com.example.simposium.admin.service.exception.AdministradorNoEncontradoException;
import com.example.simposium.admin.service.exception.AdministradorYaExisteException;
import com.example.simposium.admin.service.exception.ContrasenaNoCoincideException;
import com.example.simposium.auth.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador de excepciones para los endpoints del modulo admin.
 * Reusa el formato ApiErrorResponse del modulo auth para consistencia.
 */
@RestControllerAdvice(assignableTypes = AdministradorController.class)
public class AdministradorExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "ADMIN_VALIDATION_ERROR",
                "Datos de administrador invalidos",
                request.getRequestURI(),
                details
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ContrasenaNoCoincideException.class)
    public ResponseEntity<ApiErrorResponse> handleContrasena(
            ContrasenaNoCoincideException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "ADMIN_PASSWORD_MISMATCH",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AdministradorYaExisteException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicado(
            AdministradorYaExisteException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "ADMIN_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AdministradorNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleNoEncontrado(
            AdministradorNoEncontradoException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "ADMIN_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegal(
            IllegalArgumentException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "ADMIN_VALIDATION_ERROR",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformed(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "ADMIN_MALFORMED_REQUEST",
                "El JSON enviado no es valido",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }
}
