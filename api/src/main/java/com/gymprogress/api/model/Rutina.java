package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entidad que representa una rutina de entrenamiento (un conjunto agrupado de ejercicios).
 * <p>
 * Sirve como plantilla predefinida (por ejemplo: "Día de Pierna", "Rutina Full Body")
 * para que los usuarios no tengan que añadir los ejercicios uno por uno cada vez
 * que van al gimnasio.
 * </p>
 */
@Entity // Indica a Hibernate que esta clase es una entidad persistente.
@Table(name = "rutinas") // Sobrescribe el nombre de la tabla en la base de datos a plural.
public class Rutina {

    /**
     * Identificador único de la rutina (Clave Primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental gestionado por el motor SQL.
    private Long id;

    /**
     * Nombre descriptivo de la rutina.
     * <p>
     * Se aplica la restricción nullable = false en la base de datos para garantizar
     * que ninguna rutina se guarde sin nombre.
     * </p>
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Lista de ejercicios que componen esta rutina.
     * <p>
     * @ManyToMany: Una rutina contiene varios ejercicios, y un ejercicio puede pertenecer a varias rutinas.
     * @JoinTable: Define explícitamente la creación de la tabla intermedia ("rutina_ejercicio")
     * para gestionar esta relación N:M en la base de datos de forma limpia.
     * </p>
     */
    @ManyToMany
    @JoinTable(
            name = "rutina_ejercicio", // Nombre de la tabla puente que se creará en SQL.
            joinColumns = @JoinColumn(name = "rutina_id"), // Columna que hace referencia a esta entidad (Rutina).
            inverseJoinColumns = @JoinColumn(name = "ejercicio_id") // Columna que hace referencia a la entidad destino (Ejercicio).
    )
    private List<Ejercicio> ejercicios;

    /**
     * Constructor por defecto vacío.
     * Es un requerimiento estricto de JPA para poder instanciar la entidad al recuperar datos.
     */
    public Rutina() {
    }

    /**
     * Constructor con parámetros.
     * Facilita la instanciación de rutinas completas desde el código (ej. para inicializar datos base).
     */
    public Rutina(Long id, String nombre, List<Ejercicio> ejercicios) {
        this.id = id;
        this.nombre = nombre;
        this.ejercicios = ejercicios;
    }

    // --- Getters y Setters ---

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

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
}