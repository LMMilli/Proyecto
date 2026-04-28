package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entidad que representa un Ejercicio en la base de datos.
 * <p>
 * Esta clase mapea directamente contra la tabla "ejercicios". Almacena la información
 * base o "de catálogo" de cada movimiento (ej. Press de Banca, Sentadilla),
 * independientemente de quién o cuándo lo realice.
 * </p>
 */
@Entity // Indica a JPA/Hibernate que esta clase es una entidad persistente que debe guardarse en la base de datos.
@Table(name="ejercicios") // Sobrescribe el nombre por defecto (que sería "ejercicio") para pluralizar la tabla en SQL.
public class Ejercicio {

    /**
     * Clave primaria (Primary Key) de la tabla.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Le dice a la base de datos que genere este ID automáticamente (AUTO_INCREMENT).
    private Long id;

    /**
     * Nombre del ejercicio.
     * <p>
     * Se usa @Column(nullable = false) para añadir una restricción "NOT NULL" en el esquema de la base de datos.
     * </p>
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Grupo muscular principal implicado (ej. "Pecho", "Espalda", "Pierna").
     */
    private String grupoMuscular;

    /**
     * Descripción sobre cómo ejecutar la técnica correctamente.
     */
    private String descripcion;

    /**
     * Relación Muchos a Muchos (N:M) con la entidad Equipamiento.
     * <p>
     * Un mismo ejercicio puede hacerse con varios equipos distintos (ej. Curl de bíceps con barra o con mancuernas),
     * y un mismo equipo sirve para muchos ejercicios.
     * </p>
     */
    @ManyToMany
    @JoinTable(
            name = "ejercicio_equipamiento", // Nombre de la tabla intermedia que se creará automáticamente en SQL.
            joinColumns = @JoinColumn(name = "ejercicio_id"), // Columna que referencia al ID de esta clase (Ejercicio).
            inverseJoinColumns = @JoinColumn(name = "equipamiento_id") // Columna que referencia a la otra entidad (Equipamiento).
    )
    private List<Equipamiento> equiposDisponibles;

    /**
     * Constructor por defecto vacío.
     * Estrictamente necesario para que Hibernate pueda instanciar la entidad al recuperar datos mediante "SELECT".
     */
    public Ejercicio() {
    }

    /**
     * Constructor con todos los parámetros básicos (excepto relaciones).
     */
    public Ejercicio(Long id, String nombre, String grupoMuscular, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.descripcion = descripcion;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<Equipamiento> getEquiposDisponibles() {
        return equiposDisponibles;
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEquiposDisponibles(List<Equipamiento> equiposDisponibles) {
        this.equiposDisponibles = equiposDisponibles;
    }
}