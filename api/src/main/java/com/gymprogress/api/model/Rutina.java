package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rutinas")
public class Rutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;


    @ManyToMany
    @JoinTable(
            name = "rutina_ejercicio",
            joinColumns = @JoinColumn(name = "rutina_id"),
            inverseJoinColumns = @JoinColumn(name = "ejercicio_id")
    )
    private List<Ejercicio> ejercicios;

    public Rutina() {
    }

    public Rutina(Long id, String nombre, List<Ejercicio> ejercicios) {
        this.id = id;
        this.nombre = nombre;
        this.ejercicios = ejercicios;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }


    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
