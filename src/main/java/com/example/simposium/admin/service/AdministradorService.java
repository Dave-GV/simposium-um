package com.example.simposium.admin.service;

import com.example.simposium.admin.dto.ActualizarAdministradorRequest;
import com.example.simposium.admin.dto.AdministradorResponse;
import com.example.simposium.admin.dto.RegistroAdministradorRequest;
import com.example.simposium.admin.entity.Administrador;
import com.example.simposium.admin.entity.RolAdmin;
import com.example.simposium.admin.repository.AdministradorRepository;
import com.example.simposium.admin.service.exception.AdministradorNoEncontradoException;
import com.example.simposium.admin.service.exception.AdministradorYaExisteException;
import com.example.simposium.admin.service.exception.ContrasenaNoCoincideException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Implementacion del modulo Registro de Administrador.
 * Orquesta validaciones, hashing de contrasena y persistencia.
 */
@Service
@Transactional
public class AdministradorService implements IRegistroAdministrador {

    private final AdministradorRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ValidadorRegistro validador;

    public AdministradorService(AdministradorRepository repository,
                                PasswordEncoder passwordEncoder,
                                ValidadorRegistro validador) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.validador = validador;
    }

    @Override
    public AdministradorResponse registrarAdministrador(RegistroAdministradorRequest request) {
        // 1. Validaciones complementarias (refuerzan a Bean Validation)
        if (!validador.validarCorreo(request.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo electronico no tiene un formato valido");
        }
        if (!validador.validarContrasena(request.getContrasena())) {
            throw new IllegalArgumentException("La contrasena no cumple los requisitos minimos");
        }
        if (!validador.validarTelefono(request.getNumeroTelefono())) {
            throw new IllegalArgumentException("El numero de telefono no es valido");
        }

        // 2. Confirmar contrasena
        if (!request.getContrasena().equals(request.getConfirmarContrasena())) {
            throw new ContrasenaNoCoincideException("La contrasena y su confirmacion no coinciden");
        }

        // 3. Verificar unicidad
        if (repository.existsByCorreoElectronicoIgnoreCase(request.getCorreoElectronico())) {
            throw new AdministradorYaExisteException("Ya existe un administrador con ese correo");
        }
        if (repository.existsByNombreUsuarioIgnoreCase(request.getNombreUsuario())) {
            throw new AdministradorYaExisteException("Ya existe un administrador con ese nombre de usuario");
        }

        // 4. Construir y guardar entidad
        Administrador entidad = new Administrador();
        entidad.setNombreCompleto(request.getNombreCompleto().trim());
        entidad.setNombreUsuario(request.getNombreUsuario().trim());
        entidad.setCorreoElectronico(request.getCorreoElectronico().trim().toLowerCase());
        entidad.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        entidad.setNumeroTelefono(request.getNumeroTelefono());
        entidad.setRol(RolAdmin.valueOf(request.getRolAdministrador()));
        entidad.setActivo(true);
        entidad.setFechaRegistro(OffsetDateTime.now());

        Administrador guardado = repository.save(entidad);

        // 5. (TODO) Aqui se invocaria el ServicioCorreo para enviar verificacion.

        return new AdministradorResponse(guardado);
    }

    @Override
    public AdministradorResponse editarAdministrador(Long id, ActualizarAdministradorRequest request) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new AdministradorNoEncontradoException("Administrador con id " + id + " no existe"));

        if (request.getNombreCompleto() != null && !request.getNombreCompleto().isBlank()) {
            admin.setNombreCompleto(request.getNombreCompleto().trim());
        }
        if (request.getCorreoElectronico() != null && !request.getCorreoElectronico().isBlank()) {
            String nuevoCorreo = request.getCorreoElectronico().trim().toLowerCase();
            if (!nuevoCorreo.equalsIgnoreCase(admin.getCorreoElectronico())
                    && repository.existsByCorreoElectronicoIgnoreCase(nuevoCorreo)) {
                throw new AdministradorYaExisteException("Ya existe otro administrador con ese correo");
            }
            admin.setCorreoElectronico(nuevoCorreo);
        }
        if (request.getNumeroTelefono() != null) {
            admin.setNumeroTelefono(request.getNumeroTelefono());
        }
        if (request.getActivo() != null) {
            admin.setActivo(request.getActivo());
        }

        return new AdministradorResponse(repository.save(admin));
    }

    @Override
    public void eliminarAdministrador(Long id) {
        if (!repository.existsById(id)) {
            throw new AdministradorNoEncontradoException("Administrador con id " + id + " no existe");
        }
        repository.deleteById(id);
    }

    @Override
    public AdministradorResponse asignarRol(Long id, RolAdmin nuevoRol) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new AdministradorNoEncontradoException("Administrador con id " + id + " no existe"));
        admin.setRol(nuevoRol);
        return new AdministradorResponse(repository.save(admin));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorResponse> consultarAdministradores() {
        return repository.findAll().stream()
                .map(AdministradorResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdministradorResponse consultarPorId(Long id) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new AdministradorNoEncontradoException("Administrador con id " + id + " no existe"));
        return new AdministradorResponse(admin);
    }
}
