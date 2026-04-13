package com.gymprogress.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class EjercicioEntrenamientoRequest {
    @NotNull(message = "Debes indicar el ID del ejercicio")
    private Long ejercicioId;

    private Long equipamientoId;
    private Integer orden;
    private String notas;

    private List<SerieRequest> series;

    public Long getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(Long ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public Long getEquipamientoId() {
        return equipamientoId;
    }

    public void setEquipamientoId(Long equipamientoId) {
        this.equipamientoId = equipamientoId;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<SerieRequest> getSeries() {
        return series;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}
