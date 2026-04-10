package com.gymprogress.api.dto;

import java.util.List;

public class RutinaRequest {
    private String nombre;
    private List<Long> ejercicioIds;

    public RutinaRequest() {
    }

    public String getNombre() {
        return nombre;
    }



    public List<Long> getEjercicioIds() {
        return ejercicioIds;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setEjercicioIds(List<Long> ejercicioIds) {
        this.ejercicioIds = ejercicioIds;
    }


}
