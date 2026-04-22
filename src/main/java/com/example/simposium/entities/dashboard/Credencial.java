package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.registro.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "credenciales", schema = "dashboard")
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "numero_credencial", nullable = false, unique = true, length = 50)
    private String numeroCredencial;

    @Column(nullable = false, length = 30)
    private String estado = "activa";

    @Column(name = "emitida_en", nullable = false)
    private OffsetDateTime emitidaEn = OffsetDateTime.now();

    @Column(name = "expira_en")
    private OffsetDateTime expiraEn;

    @Column(name = "qr_codigo", length = 500)
    private String qrCodigo;

    public Credencial() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNumeroCredencial() { return numeroCredencial; }
    public void setNumeroCredencial(String numeroCredencial) { this.numeroCredencial = numeroCredencial; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getEmitidaEn() { return emitidaEn; }
    public void setEmitidaEn(OffsetDateTime emitidaEn) { this.emitidaEn = emitidaEn; }

    public OffsetDateTime getExpiraEn() { return expiraEn; }
    public void setExpiraEn(OffsetDateTime expiraEn) { this.expiraEn = expiraEn; }

    public String getQrCodigo() { return qrCodigo; }
    public void setQrCodigo(String qrCodigo) { this.qrCodigo = qrCodigo; }
}
