package com.gymprogress.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SerieRequest {

    @NotNull(message = "Debes indicar el ID del ejercicio")
    private Long ejercicioId;

    @Min(value = 1, message = "Debe haber al menos 1 repetico")
    private Integer repeticiones;

    @Min(value = 0, message = "No puedes levantar peso negativo mongolo")
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
