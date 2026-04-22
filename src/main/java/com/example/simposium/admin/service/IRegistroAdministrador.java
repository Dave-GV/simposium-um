package com.example.simposium.admin.service;

import com.example.simposium.admin.dto.ActualizarAdministradorRequest;
import com.example.simposium.admin.dto.AdministradorResponse;
import com.example.simposium.admin.dto.RegistroAdministradorRequest;
import com.example.simposium.admin.entity.RolAdmin;

import java.util.List;

/**
 * Contrato del modulo Registro de Administrador.
 * Corresponde a IRegistroAdministrador del diagrama de clases.
 */
public interface IRegistroAdministrador {

    AdministradorResponse registrarAdministrador(RegistroAdministradorRequest request);

    AdministradorResponse editarAdministrador(Long id, ActualizarAdministradorRequest request);

    void eliminarAdministrador(Long id);

    AdministradorResponse asignarRol(Long id, RolAdmin nuevoRol);

    List<AdministradorResponse> consultarAdministradores();

    AdministradorResponse consultarPorId(Long id);
}
