package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.registro.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "inscripciones_talleres", schema = "dashboard",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "taller_id"}))
public class InscripcionTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "taller_id", nullable = false)
    private Taller taller;

    @Column(nullable = false, length = 30)
    private String estado = "confirmada";

    @Column(name = "inscrito_en", nullable = false)
    private OffsetDateTime inscritoEn = OffsetDateTime.now();

    public InscripcionTaller() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Taller getTaller() { return taller; }
    public void setTaller(Taller taller) { this.taller = taller; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getInscritoEn() { return inscritoEn; }
    public void setInscritoEn(OffsetDateTime inscritoEn) { this.inscritoEn = inscritoEn; }
}
