package com.gymprogress.api.dto;

import java.util.List;

public class RutinaRequest {
    private String nombre;
    private Long usuarioId;
    private List<Long> ejercicioIds;

    public RutinaRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public List<Long> getEjercicioIds() {
        return ejercicioIds;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setEjercicioIds(List<Long> ejercicioIds) {
        this.ejercicioIds = ejercicioIds;
    }


}
