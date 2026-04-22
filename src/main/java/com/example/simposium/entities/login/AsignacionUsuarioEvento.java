package com.example.simposium.entities.login;

import com.example.simposium.entities.registro.Usuario;
import com.example.simposium.entities.seguridad.Rol;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "asignacion_usuario_evento", schema = "auth",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "evento_id"}))
public class AsignacionUsuarioEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Column(name = "especialidad_staff", length = 150)
    private String especialidadStaff;

    @Column(name = "fecha_asignacion", nullable = false)
    private OffsetDateTime fechaAsignacion = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "asignado_por")
    private Usuario asignadoPor;

    public AsignacionUsuarioEvento() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getEspecialidadStaff() { return especialidadStaff; }
    public void setEspecialidadStaff(String especialidadStaff) { this.especialidadStaff = especialidadStaff; }

    public OffsetDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(OffsetDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public Usuario getAsignadoPor() { return asignadoPor; }
    public void setAsignadoPor(Usuario asignadoPor) { this.asignadoPor = asignadoPor; }
}
