package com.example.simposium.admin.controller;

import com.example.simposium.admin.dto.ActualizarAdministradorRequest;
import com.example.simposium.admin.dto.AdministradorResponse;
import com.example.simposium.admin.dto.AsignarRolRequest;
import com.example.simposium.admin.dto.RegistroAdministradorRequest;
import com.example.simposium.admin.entity.RolAdmin;
import com.example.simposium.admin.service.IRegistroAdministrador;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints REST del modulo Registro de Administrador.
 *
 * Rutas:
 *   POST   /api/admin/registro          - registrar nuevo admin
 *   GET    /api/admin                   - listar administradores
 *   GET    /api/admin/{id}              - obtener administrador
 *   PUT    /api/admin/{id}              - editar administrador
 *   PATCH  /api/admin/{id}/rol          - asignar rol
 *   DELETE /api/admin/{id}              - eliminar administrador
 */
@RestController
@RequestMapping("/api/admin")
public class AdministradorController {

    private final IRegistroAdministrador service;

    public AdministradorController(IRegistroAdministrador service) {
        this.service = service;
    }

    @PostMapping("/registro")
    public ResponseEntity<AdministradorResponse> registrar(
            @Valid @RequestBody RegistroAdministradorRequest request) {
        AdministradorResponse creado = service.registrarAdministrador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<AdministradorResponse>> listar() {
        return ResponseEntity.ok(service.consultarAdministradores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarAdministradorRequest request) {
        return ResponseEntity.ok(service.editarAdministrador(id, request));
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<AdministradorResponse> asignarRol(
            @PathVariable Long id,
            @Valid @RequestBody AsignarRolRequest request) {
        return ResponseEntity.ok(service.asignarRol(id, RolAdmin.valueOf(request.getRol())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}
