package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.login.Evento;
import com.example.simposium.entities.registro.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "inscripciones", schema = "dashboard",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "evento_id"}))
public class Inscripcion {

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
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @Column(nullable = false, length = 30)
    private String estado = "pendiente";

    @Column(name = "inscrito_en", nullable = false)
    private OffsetDateTime inscritoEn = OffsetDateTime.now();

    public Inscripcion() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getInscritoEn() { return inscritoEn; }
    public void setInscritoEn(OffsetDateTime inscritoEn) { this.inscritoEn = inscritoEn; }
}
