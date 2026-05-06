package com.gymprogress.api.dto;

import java.util.List;

public class EjercicioRequest {
    private Long id;
    private String nombre;
    private String grupoMuscular;
    private String descripcion;

    private List<EquipamientoRequest> equipamientoDisponibles;

    public EjercicioRequest() {
    }

    public EjercicioRequest(Long id, String nombre, String grupoMuscular, String descripcion, List<EquipamientoRequest> equipamientoDisponibles) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
        this.equipamientoDisponibles = equipamientoDisponibles;
    }

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

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<EquipamientoRequest> getEquipamientoDisponibles() {
        return equipamientoDisponibles;
    }

    public void setEquipamientoDisponibles(List<EquipamientoRequest> equipamientoDisponibles) {
        this.equipamientoDisponibles = equipamientoDisponibles;
    }

    public void setEquiposDisponibles(List<EquipamientoRequest> equiposDTO) {
    }
}
