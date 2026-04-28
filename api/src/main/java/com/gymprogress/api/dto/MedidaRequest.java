package com.gymprogress.api.dto;

/**
 * Data Transfer Object (DTO) para registrar nuevas medidas de composición corporal.
 * <p>
 * Se utiliza para recibir y estructurar la información (peso y porcentaje de grasa)
 * que el usuario envía desde la aplicación cliente para actualizar su historial físico,
 * vinculando estos datos a su identificador de usuario.
 * </p>
 */
public class MedidaRequest {

    /**
     * Identificador único del usuario al que pertenecen estas medidas.
     * Es fundamental para saber a qué perfil de la base de datos asociar el registro.
     */
    private Long usuarioId;

    /**
     * Peso corporal actual registrado por el usuario (generalmente tratado en kilogramos,
     * dependiendo de la lógica que implementes en tu front-end).
     */
    private Double peso;

    /**
     * Porcentaje de grasa corporal estimado.
     * Suele ser opcional, ya que no todos los usuarios tienen básculas de bioimpedancia o plicómetros.
     */
    private Double porcentajeGrasa;

    /**
     * Constructor por defecto vacío.
     * Requerido por Spring Boot (y la librería Jackson) para poder crear la instancia
     * y poblarla con los datos extraídos del JSON de la petición HTTP.
     */
    public MedidaRequest() {
    }

    // --- Getters y Setters ---

    /**
     * @return El ID del usuario.
     */
    public Long getUsuarioId() {
        return usuarioId;
    }

    /**
     * @return El peso corporal registrado.
     */
    public Double getPeso() {
        return peso;
    }

    /**
     * @return El porcentaje de grasa corporal.
     */
    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }
}