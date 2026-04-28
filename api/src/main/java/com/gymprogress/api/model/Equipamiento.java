package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entidad que representa el material o equipamiento disponible en el gimnasio.
 * <p>
 * Sirve como un catálogo maestro. Permite que los ejercicios indiquen con qué
 * herramientas se pueden realizar (ej. "Barra", "Mancuerna", "Máquina en polea")
 * y que el usuario especifique qué usó en su entrenamiento.
 * </p>
 */
@Entity // Define la clase como una entidad gestionada por JPA.
@Table(name = "equipamiento") // Mapea la entidad a la tabla "equipamiento" en la base de datos.
public class Equipamiento {

    /**
     * Identificador único del equipamiento (Clave Primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental gestionado por la base de datos.
    private Long id;

    /**
     * Nombre descriptivo del material.
     * <p>
     * Se aplican dos restricciones a nivel de base de datos:
     * - nullable = false: No puede dejarse en blanco.
     * - unique = true: No pueden existir dos equipos con exactamente el mismo nombre,
     * lo cual evita duplicados en el catálogo (ej. tener dos veces "Mancuerna").
     * </p>
     */
    @Column(unique = true, nullable = false)
    private String nombre; // Ejemplos: "Barra", "Mancuerna", "Peso Corporal", "Banda elástica"

    /**
     * Lista de ejercicios que se pueden realizar utilizando este equipamiento.
     * <p>
     * Es el lado "inverso" de la relación Many-To-Many.
     * El atributo 'mappedBy = "equiposDisponibles"' le indica a Hibernate que la tabla
     * intermedia ('ejercicio_equipamiento') ya ha sido definida y es gestionada
     * por el atributo 'equiposDisponibles' en la clase {@link Ejercicio}.
     * </p>
     */
    @ManyToMany(mappedBy = "equiposDisponibles")
    private List<Ejercicio> ejercicios;

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