package com.gymprogress.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull; // Ojo: Este import no se está usando, lo puedes borrar.

/**
 * Data Transfer Object (DTO) que representa una única serie (set) dentro de un ejercicio.
 * <p>
 * Se utiliza para registrar los detalles exactos de ejecución en cada ronda de un
 * ejercicio específico (cuántas repeticiones se hicieron, con qué peso y el esfuerzo).
 * </p>
 */
public class SerieRequest {

    /**
     * Número de repeticiones completadas en esta serie.
     * <p>
     * Se utiliza @Min para garantizar lógicamente que no se registre una serie en vacío.
     * </p>
     */
    @Min(value = 1, message = "Debe haber al menos 1 repetición")
    private Integer repeticiones;

    /**
     * Peso utilizado durante la serie (generalmente en kilogramos).
     * <p>
     * El valor mínimo es 0 para permitir registrar ejercicios realizados únicamente
     * con el peso corporal (bodyweight) como dominadas o flexiones.
     * </p>
     */
    @Min(value = 0, message = "El peso no puede ser negativo")
    private Double peso;

    /**
     * RPE (Rate of Perceived Exertion) o Escala de Esfuerzo Percibido.
     * Es un valor subjetivo (generalmente del 1 al 10) que indica lo cerca que estuvo el usuario del fallo muscular.
     */
    private Integer rpe;

    /**
     * Categoría o tipo de serie ejecutada.
     * Ejemplos de uso: "CALENTAMIENTO", "EFECTIVA", "DROP_SET", "AL_FALLO".
     */
    private String tipo;

    /**
     * Constructor por defecto vacío.
     * Requerido por la librería de serialización (Jackson) para procesar el JSON correctamente.
     */
    public SerieRequest() {
    }

    // --- Getters y Setters ---

    /**
     * @return El número de repeticiones realizadas.
     */
    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    /**
     * @return El peso levantado en la serie.
     */
    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    /**
     * @return El nivel de esfuerzo percibido (RPE).
     */
    public Integer getRpe() {
        return rpe;
    }

    public void setRpe(Integer rpe) {
        this.rpe = rpe;
    }

    /**
     * @return El tipo o clasificación de la serie.
     */
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}