package com.example.simposium.admin.repository;

import com.example.simposium.admin.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByCorreoElectronicoIgnoreCase(String correoElectronico);

    Optional<Administrador> findByNombreUsuarioIgnoreCase(String nombreUsuario);

    boolean existsByCorreoElectronicoIgnoreCase(String correoElectronico);

    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
}
