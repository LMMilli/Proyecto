package com.gymprogress.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ejercicio_entrenamiento")
public class EjercicioEntrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entrenamiento_id")
    @JsonIgnore
    private Entrenamiento entrenamiento;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    private Ejercicio ejercicio;

    @ManyToOne
    @JoinColumn(name = "equipamiento_id")
    private Equipamiento equipamiento;

    private Integer orden;
    private String notas;

    @OneToMany(mappedBy = "ejercicioEntrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series;

    public EjercicioEntrenamiento(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entrenamiento getEntrenamiento() {
        return entrenamiento;
    }

    public void setEntrenamiento(Entrenamiento entrenamiento) {
        this.entrenamiento = entrenamiento;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Equipamiento getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(Equipamiento equipamiento) {
        this.equipamiento = equipamiento;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
}
