package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.login.Evento;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "talleres", schema = "dashboard")
public class Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 200)
    private String ponente;

    @Column(name = "duracion_min")
    private Integer duracionMin;

    @Column(name = "capacidad_max")
    private Integer capacidadMax;

    @Column(length = 100)
    private String sala;

    @Column(name = "hora_inicio", nullable = false)
    private OffsetDateTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private OffsetDateTime horaFin;

    @Column(nullable = false, length = 30)
    private String estado = "programado";

    public Taller() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPonente() { return ponente; }
    public void setPonente(String ponente) { this.ponente = ponente; }

    public Integer getDuracionMin() { return duracionMin; }
    public void setDuracionMin(Integer duracionMin) { this.duracionMin = duracionMin; }

    public Integer getCapacidadMax() { return capacidadMax; }
    public void setCapacidadMax(Integer capacidadMax) { this.capacidadMax = capacidadMax; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }

    public OffsetDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(OffsetDateTime horaInicio) { this.horaInicio = horaInicio; }

    public OffsetDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(OffsetDateTime horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
