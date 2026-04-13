package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "objetivos")
public class Objetivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private Double valorObjetivo;

    private LocalDate fechaLimite;

    private boolean completado = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isCompletado() {
        return completado;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public Double getValorObjetivo() {
        return valorObjetivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public void setValorObjetivo(Double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
