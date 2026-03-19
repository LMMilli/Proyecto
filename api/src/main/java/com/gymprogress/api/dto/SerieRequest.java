package com.gymprogress.api.dto;

public class SerieRequest {
    private Long ejercicioId;
    private Integer repeticiones;
    private Double peso;
    private Integer rpe;

    public SerieRequest() {
    }

    public Long getEjercicioId() {
        return ejercicioId;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public Double getPeso() {
        return peso;
    }

    public Integer getRpe() {
        return rpe;
    }

    public void setEjercicioId(Long ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }
}
