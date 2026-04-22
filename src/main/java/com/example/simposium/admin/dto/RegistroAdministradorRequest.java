package com.example.simposium.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para registrar un nuevo administrador.
 * Refleja los campos del diagrama de clases RegistroAdministrador.
 */
public class RegistroAdministradorRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre completo debe tener entre 3 y 150 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo electronico no tiene un formato valido")
    @Size(max = 150)
    private String correoElectronico;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 60, message = "El nombre de usuario debe tener entre 3 y 60 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
            message = "El nombre de usuario solo permite letras, numeros, guiones, puntos y guion bajo")
    private String nombreUsuario;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, max = 100, message = "La contrasena debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "La contrasena debe incluir al menos una letra y un numero")
    private String contrasena;

    @NotBlank(message = "Debe confirmar la contrasena")
    private String confirmarContrasena;

    @Pattern(regexp = "^[+0-9 ()-]{7,20}$",
            message = "El numero de telefono no es valido")
    private String numeroTelefono;

    @NotBlank(message = "El rol del administrador es obligatorio")
    @Pattern(regexp = "^(SUPER_ADMIN|ADMIN)$",
            message = "El rol debe ser SUPER_ADMIN o ADMIN")
    private String rolAdministrador;

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getRolAdministrador() {
        return rolAdministrador;
    }

    public void setRolAdministrador(String rolAdministrador) {
        this.rolAdministrador = rolAdministrador;
    }
}
