package com.gymprogress.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

/**
 * Entidad que representa la ejecución de un ejercicio específico dentro de una sesión de entrenamiento.
 * <p>
 * A diferencia de la entidad {@link Ejercicio} (que es el catálogo), esta clase registra
 * los detalles reales de una sesión: qué equipamiento se usó, en qué orden se hizo
 * y qué series se completaron.
 * </p>
 */
@Entity
@Table(name = "ejercicio_entrenamiento") // Nombre de la tabla que almacena el detalle de los ejercicios por sesión
public class EjercicioEntrenamiento {

    /**
     * Identificador único de este bloque de ejercicio en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Sesión de entrenamiento a la que pertenece este ejercicio.
     * <p>
     * @ManyToOne: Muchos registros de EjercicioEntrenamiento pertenecen a un solo Entrenamiento.
     * @JsonIgnore: Evita la recursividad infinita al serializar a JSON (impide que el entrenamiento
     * intente serializar sus ejercicios y estos de nuevo al entrenamiento).
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "entrenamiento_id") // Clave ajena (FK) hacia la tabla de entrenamientos
    @JsonIgnore
    private Entrenamiento entrenamiento;

    /**
     * Referencia al ejercicio del catálogo que se está realizando.
     */
    @ManyToOne
    @JoinColumn(name = "ejercicio_id") // Clave ajena (FK) hacia la tabla de ejercicios (catálogo)
    private Ejercicio ejercicio;

    /**
     * Referencia opcional al equipamiento utilizado en esta sesión específica.
     */
    @ManyToOne
    @JoinColumn(name = "equipamiento_id") // Clave ajena (FK) hacia la tabla de equipamiento
    private Equipamiento equipamiento;

    /**
     * Posición del ejercicio dentro de la rutina (ej: 1 para el primer ejercicio, 2 para el segundo).
     */
    private Integer orden;

    /**
     * Comentarios u observaciones específicas sobre el desempeño en este ejercicio durante la sesión.
     */
    private String notas;

    /**
     * Listado de series (sets) realizadas para este ejercicio en esta sesión.
     * <p>
     * cascade = CascadeType.ALL: Si se guarda o elimina este objeto, se guardarán o eliminarán sus series automáticamente.
     * orphanRemoval = true: Si se quita una serie de la lista, se borrará definitivamente de la base de datos.
     * </p>
     */
    @OneToMany(mappedBy = "ejercicioEntrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public EjercicioEntrenamiento(){}

    // --- Getters y Setters ---

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