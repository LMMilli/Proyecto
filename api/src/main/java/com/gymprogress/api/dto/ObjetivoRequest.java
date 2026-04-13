package com.gymprogress.api.dto;

import java.time.LocalDate;

public class ObjetivoRequest {
    private Long usuarioId;
    private String tipo;
    private Double valorObjetivo;
    private LocalDate fechaLimite;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public Double getValorObjetivo() {
        return valorObjetivo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setValorObjetivo(Double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
