package com.gymprogress.api.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) para la creación y gestión de objetivos de los usuarios.
 * <p>
 * Se encarga de recibir los datos que el usuario envía desde la aplicación cuando
 * establece una nueva meta (por ejemplo: alcanzar un peso corporal determinado,
 * levantar ciertos kilos en un ejercicio, etc.).
 * </p>
 */
public class ObjetivoRequest {

    /**
     * Identificador del usuario que está estableciendo el objetivo.
     * Es necesario para vincular esta meta a su perfil en la base de datos.
     */
    private Long usuarioId;

    /**
     * Categoría o tipo de objetivo que se quiere alcanzar.
     * (Ejemplos típicos podrían ser: "PESO_CORPORAL", "1RM_PRESS_BANCA", "PORCENTAJE_GRASA").
     */
    private String tipo;

    /**
     * El valor numérico exacto que el usuario desea alcanzar.
     * Si el tipo es "PESO_CORPORAL", este valor representaría los kilos a los que quiere llegar.
     */
    private Double valorObjetivo;

    /**
     * Fecha límite o "deadline" en la que el usuario espera haber cumplido este objetivo.
     */
    private LocalDate fechaLimite;

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
     * @return El tipo de objetivo.
     */
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return El valor numérico de la meta a alcanzar.
     */
    public Double getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(Double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    /**
     * @return La fecha límite establecida para cumplir el objetivo.
     */
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}