package com.gymprogress.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Data Transfer Object (DTO) que representa un ejercicio específico dentro de una sesión de entrenamiento.
 * <p>
 * Se utiliza para recibir los datos desde el cliente (ej. la app móvil) cuando el usuario
 * registra un entrenamiento. Contiene los detalles de ejecución del ejercicio y agrupa
 * las series (sets) realizadas.
 * </p>
 */
public class EjercicioEntrenamientoRequest {

    /**
     * Identificador del ejercicio base en el catálogo global (ej. ID de "Press de banca").
     * Es obligatorio, ya que no se puede registrar un bloque de entrenamiento sin saber qué ejercicio es.
     */
    @NotNull(message = "Debes indicar el ID del ejercicio") // Validación de Jakarta: Spring devolverá un error 400 si este campo viene nulo o no se envía.
    private Long ejercicioId;

    /**
     * Identificador del equipamiento específico utilizado, si aplica (ej. ID de una máquina concreta o mancuernas).
     * Es opcional.
     */
    private Long equipamientoId;

    /**
     * Posición o número de orden de este ejercicio dentro de la sesión de entrenamiento (1º, 2º, 3º...).
     */
    private Integer orden;

    /**
     * Anotaciones u observaciones libres del usuario sobre cómo ha sentido el ejercicio.
     */
    private String notas;

    /**
     * Lista de las series (sets) realizadas para este ejercicio (repeticiones, peso, etc.).
     */
    private List<SerieRequest> series;

    // --- Getters y Setters ---

    /**
     * @return El ID del ejercicio del catálogo.
     */
    public Long getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(Long ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    /**
     * @return El ID del equipamiento utilizado.
     */
    public Long getEquipamientoId() {
        return equipamientoId;
    }

    public void setEquipamientoId(Long equipamientoId) {
        this.equipamientoId = equipamientoId;
    }

    /**
     * @return El número de orden del ejercicio en la rutina.
     */
    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * @return Las notas adicionales del usuario.
     */
    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    /**
     * @return La lista de DTOs con las series asociadas a este ejercicio.
     */
    public List<SerieRequest> getSeries() {
        return series;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}