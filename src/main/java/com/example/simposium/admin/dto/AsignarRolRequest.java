package com.example.simposium.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AsignarRolRequest {

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(SUPER_ADMIN|ADMIN)$",
            message = "El rol debe ser SUPER_ADMIN o ADMIN")
    private String rol;

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
