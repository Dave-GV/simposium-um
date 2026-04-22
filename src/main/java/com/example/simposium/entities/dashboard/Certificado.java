package com.example.simposium.entities.dashboard;

import com.example.simposium.entities.registro.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "certificados", schema = "dashboard",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "taller_id"}))
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "taller_id", nullable = false)
    private Taller taller;

    @Column(name = "numero_certificado", nullable = false, unique = true, length = 100)
    private String numeroCertificado;

    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

    @Column(name = "emitido_en", nullable = false)
    private OffsetDateTime emitidoEn = OffsetDateTime.now();

    @Column(nullable = false)
    private Boolean enviado = false;

    public Certificado() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Taller getTaller() { return taller; }
    public void setTaller(Taller taller) { this.taller = taller; }

    public String getNumeroCertificado() { return numeroCertificado; }
    public void setNumeroCertificado(String numeroCertificado) { this.numeroCertificado = numeroCertificado; }

    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public OffsetDateTime getEmitidoEn() { return emitidoEn; }
    public void setEmitidoEn(OffsetDateTime emitidoEn) { this.emitidoEn = emitidoEn; }

    public Boolean getEnviado() { return enviado; }
    public void setEnviado(Boolean enviado) { this.enviado = enviado; }
}
