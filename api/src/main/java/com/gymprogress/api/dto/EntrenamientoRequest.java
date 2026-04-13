package com.gymprogress.api.dto;

import java.util.List;

public class EntrenamientoRequest {
    private Long usuarioId;
    private Long rutinaId;
    private Integer duracionMinutos;
    private List<EjercicioEntrenamientoRequest> ejercicios;

    public EntrenamientoRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getRutinaId() {
        return rutinaId;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public List<EjercicioEntrenamientoRequest> getEjercicios() {
        return ejercicios;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setRutinaId(Long rutinaId) {
        this.rutinaId = rutinaId;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setEjercicios(List<EjercicioEntrenamientoRequest> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
