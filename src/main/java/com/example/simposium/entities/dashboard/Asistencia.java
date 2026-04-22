package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.registro.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "asistencias", schema = "dashboard",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "taller_id"}))
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "taller_id", nullable = false)
    private Taller taller;

    @Column(nullable = false)
    private Boolean asistio = false;

    @Column(name = "registrado_en", nullable = false)
    private OffsetDateTime registradoEn = OffsetDateTime.now();

    public Asistencia() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Taller getTaller() { return taller; }
    public void setTaller(Taller taller) { this.taller = taller; }

    public Boolean getAsistio() { return asistio; }
    public void setAsistio(Boolean asistio) { this.asistio = asistio; }

    public OffsetDateTime getRegistradoEn() { return registradoEn; }
    public void setRegistradoEn(OffsetDateTime registradoEn) { this.registradoEn = registradoEn; }
}
