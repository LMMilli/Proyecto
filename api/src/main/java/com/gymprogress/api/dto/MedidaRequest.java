package com.gymprogress.api.dto;

public class MedidaRequest {
    private Long usuarioId;
    private Double peso;
    private Double porcentajeGrasa;

    public MedidaRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Double getPeso() {
        return peso;
    }

    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }
}
