package com.gymprogress.api.model;

import jakarta.persistence.*;

@Entity
@Table(name="ejercicios")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String grupoMuscular;

    public Ejercicio() {
    }

    public Ejercicio(Long id, String nombre, String grupoMuscular) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }
}
