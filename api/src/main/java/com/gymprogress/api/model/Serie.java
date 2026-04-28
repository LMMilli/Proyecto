package com.gymprogress.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entidad que representa una serie (set) individual dentro de la ejecución de un ejercicio.
 * <p>
 * Es el nivel más granular del registro de entrenamiento. Almacena el esfuerzo exacto
 * realizado en una "ronda" concreta: cuánto peso se movió, cuántas repeticiones se
 * lograron y el nivel de esfuerzo percibido.
 * </p>
 */
@Entity // Indica a JPA/Hibernate que esta clase se mapeará a una tabla relacional.
@Table(name = "series") // Nombra la tabla como "series" en la base de datos.
public class Serie {

    /**
     * Identificador único de la serie en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental.
    private Long id;

    /**
     * Peso levantado o utilizado durante esta serie (generalmente en kilogramos).
     * Al ser Double, permite registrar pesos fraccionados (ej. 12.5 kg).
     */
    private Double peso;

    /**
     * Número de repeticiones completadas con éxito en esta serie.
     */
    private Integer repeticiones;

    /**
     * RPE (Rate of Perceived Exertion) o Escala de Esfuerzo Percibido (1-10).
     * Indica lo cerca que estuvo el usuario del fallo muscular.
     */
    private Integer rpe;

    /**
     * El ejercicio (dentro de una sesión específica) al que pertenece esta serie.
     * <p>
     * @ManyToOne: Muchas series pertenecen a un único "EjercicioEntrenamiento".
     * @JsonIgnore: Es fundamental aquí. Como "EjercicioEntrenamiento" ya tiene una lista de
     * series, ignorar este campo al serializar a JSON evita un bucle infinito (Ejercicio -> Serie -> Ejercicio...).
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "ejercicio_entrenamiento_id") // Clave foránea que apunta a la tabla ejercicio_entrenamiento.
    @JsonIgnore
    private EjercicioEntrenamiento ejercicioEntrenamiento;

    /**
     * Categoría o tipo de la serie (ej. "CALENTAMIENTO", "EFECTIVA", "DROP_SET").
     */
    private String tipo;

    /**
     * Constructor por defecto vacío. Obligatorio para JPA.
     */
    public Serie() {
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Integer getRpe() {
        return rpe;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }

    public EjercicioEntrenamiento getEjercicioEntrenamiento() {
        return ejercicioEntrenamiento;
    }

    public void setEjercicioEntrenamiento(EjercicioEntrenamiento ejercicioEntrenamiento) {
        this.ejercicioEntrenamiento = ejercicioEntrenamiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}