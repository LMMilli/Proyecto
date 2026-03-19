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
    @JoinColumn(name = "entrenamiento_id" )
    @JsonIgnore
    private Entrenamiento entrenamiento;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    private Ejercicio ejercicio;

    public Serie() {
    }

    public Serie(Long id, Double peso, Integer repeticiones, Integer rpe, Entrenamiento entrenamiento, Ejercicio ejercicio) {
        this.id = id;
        this.peso = peso;
        this.repeticiones = repeticiones;
        this.rpe = rpe;
        this.entrenamiento = entrenamiento;
        this.ejercicio = ejercicio;
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

    public Entrenamiento getEntrenamiento() {
        return entrenamiento;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
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

    public void setEntrenamiento(Entrenamiento entrenamiento) {
        this.entrenamiento = entrenamiento;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }
}
