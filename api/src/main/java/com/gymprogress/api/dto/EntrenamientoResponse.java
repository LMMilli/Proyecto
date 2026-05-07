package com.gymprogress.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EntrenamientoResponse {
    private Long id;
    private LocalDateTime fecha;
    private Integer duracionMinutos;
    private Long usuarioId;
    private String nombreRutina;
    private List<EjercicioEntrenamientoResponse> ejercicios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreRutina() {
        return nombreRutina;
    }

    public void setNombreRutina(String nombreRutina) {
        this.nombreRutina = nombreRutina;
    }

    public List<EjercicioEntrenamientoResponse> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioEntrenamientoResponse> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
