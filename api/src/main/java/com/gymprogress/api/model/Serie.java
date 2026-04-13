package com.gymprogress.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double peso;
    private Integer repeticiones;
    private Integer rpe;

    @ManyToOne
    @JoinColumn(name = "ejercicio_entrenamiento_id")
    @JsonIgnore
    private EjercicioEntrenamiento ejercicioEntrenamiento;

    private String tipo;


    public Serie() {
    }


    public Long getId() {
        return id;
    }

    public Double getPeso() {
        return peso;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public Integer getRpe() {
        return rpe;
    }

    public EjercicioEntrenamiento getEjercicioEntrenamiento() {
        return ejercicioEntrenamiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }

    public void setEjercicioEntrenamiento(EjercicioEntrenamiento ejercicioEntrenamiento) {
        this.ejercicioEntrenamiento = ejercicioEntrenamiento;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
