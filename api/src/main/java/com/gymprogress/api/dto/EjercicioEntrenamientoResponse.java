package com.gymprogress.api.dto;

import java.util.List;

public class EjercicioEntrenamientoResponse {
    private Long id;
    private Long ejercicioId;
    private String nombreEjercicio;
    private String nombreEquipamiento;
    private Integer orden;
    private String notas;
    private List<SerieResponse> series;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(Long ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public String getNombreEjercicio() {
        return nombreEjercicio;
    }

    public void setNombreEjercicio(String nombreEjercicio) {
        this.nombreEjercicio = nombreEjercicio;
    }

    public String getNombreEquipamiento() {
        return nombreEquipamiento;
    }

    public void setNombreEquipamiento(String nombreEquipamiento) {
        this.nombreEquipamiento = nombreEquipamiento;
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

    public List<SerieResponse> getSeries() {
        return series;
    }

    public void setSeries(List<SerieResponse> series) {
        this.series = series;
    }
}
