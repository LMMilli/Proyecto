package com.gymprogress.api.dto;

import java.util.List;

public class RutinaResponse {
    private Long id;
    private String nombre;
    private List<EjercicioResponse> ejercicios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<EjercicioResponse> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioResponse> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
