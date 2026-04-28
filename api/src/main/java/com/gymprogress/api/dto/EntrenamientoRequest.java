package com.gymprogress.api.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) principal para el registro de una sesión de entrenamiento.
 * <p>
 * Este objeto actúa como el contenedor o "raíz" (payload) de la información que envía
 * la aplicación cliente (ej. Android) cuando el usuario finaliza y guarda su entrenamiento.
 * Incluye los datos generales de la sesión y la lista detallada de los ejercicios realizados.
 * </p>
 */
public class EntrenamientoRequest {

    /**
     * Identificador del usuario que ha realizado la sesión de entrenamiento.
     */
    private Long usuarioId;

    /**
     * Identificador de la rutina base utilizada, en caso de que el usuario haya seguido una predefinida.
     * Puede ser nulo si el usuario realizó un entrenamiento libre o improvisado.
     */
    private Long rutinaId;

    /**
     * Duración total de la sesión de entrenamiento expresada en minutos.
     */
    private Integer duracionMinutos;

    /**
     * Lista de los ejercicios que componen este entrenamiento.
     * Cada elemento es a su vez un DTO ({@link EjercicioEntrenamientoRequest}) que contiene los detalles y series.
     */
    private List<EjercicioEntrenamientoRequest> ejercicios;

    /**
     * Constructor por defecto vacío.
     * Es estrictamente necesario para que las librerías de serialización de Spring (como Jackson)
     * puedan instanciar el objeto y mapear el JSON entrante correctamente.
     */
    public EntrenamientoRequest() {
    }

    // --- Getters y Setters ---

    /**
     * @return El ID del usuario.
     */
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return El ID de la rutina asociada.
     */
    public Long getRutinaId() {
        return rutinaId;
    }

    public void setRutinaId(Long rutinaId) {
        this.rutinaId = rutinaId;
    }

    /**
     * @return La duración del entrenamiento en minutos.
     */
    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * @return La lista de ejercicios realizados en la sesión.
     */
    public List<EjercicioEntrenamientoRequest> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioEntrenamientoRequest> ejercicios) {
        this.ejercicios = ejercicios;
    }
}