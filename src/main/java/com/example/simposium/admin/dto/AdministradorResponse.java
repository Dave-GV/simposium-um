package com.example.simposium.admin.dto;

import com.example.simposium.admin.entity.Administrador;

import java.time.OffsetDateTime;

/**
 * DTO de salida para mostrar un Administrador SIN exponer la contrasena.
 */
public class AdministradorResponse {

    private final Long idAdministrador;
    private final String nombreCompleto;
    private final String nombreUsuario;
    private final String correoElectronico;
    private final String numeroTelefono;
    private final String rol;
    private final boolean activo;
    private final OffsetDateTime fechaRegistro;

    public AdministradorResponse(Administrador a) {
        this.idAdministrador = a.getIdAdministrador();
        this.nombreCompleto = a.getNombreCompleto();
        this.nombreUsuario = a.getNombreUsuario();
        this.correoElectronico = a.getCorreoElectronico();
        this.numeroTelefono = a.getNumeroTelefono();
        this.rol = a.getRol() != null ? a.getRol().name() : null;
        this.activo = a.isActivo();
        this.fechaRegistro = a.getFechaRegistro();
    }

    public Long getIdAdministrador() {
        return idAdministrador;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
