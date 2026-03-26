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

    private String descripcion;

    public Ejercicio() {
    }

    public Ejercicio(Long id, String nombre, String grupoMuscular, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
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

    public String getDescripcion() { return descripcion; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
