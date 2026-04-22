package com.example.simposium.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para editar un administrador. Todos los campos son opcionales,
 * pero si se envian deben cumplir las mismas reglas de formato.
 */
public class ActualizarAdministradorRequest {

    @Size(min = 3, max = 150)
    private String nombreCompleto;

    @Email(message = "El correo electronico no tiene un formato valido")
    @Size(max = 150)
    private String correoElectronico;

    @Pattern(regexp = "^[+0-9 ()-]{7,20}$",
            message = "El numero de telefono no es valido")
    private String numeroTelefono;

    private Boolean activo;

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

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
