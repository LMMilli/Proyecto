package com.gymprogress.api.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) para la creación y configuración de Rutinas.
 * <p>
 * Se utiliza para recibir los datos desde el cliente cuando un administrador o usuario
 * diseña una nueva rutina de entrenamiento. En lugar de enviar los objetos de ejercicio completos,
 * solo se envían sus identificadores (IDs) para optimizar el peso de la petición de red.
 * </p>
 */
public class RutinaRequest {

    /**
     * Nombre descriptivo de la rutina (por ejemplo: "Día de Pierna", "Rutina Full Body", "Pecho y Tríceps").
     */
    private String nombre;

    /**
     * Lista con los identificadores únicos (IDs) de los ejercicios que compondrán esta rutina.
     * El controlador usará estos IDs para buscar los ejercicios reales en la base de datos y vincularlos.
     */
    private List<Long> ejercicioIds;

    /**
     * Constructor por defecto vacío.
     * Necesario para que el framework de Spring Boot (a través de Jackson) pueda
     * instanciar el objeto y mapear el JSON entrante de forma automática.
     */
    public RutinaRequest() {
    }

    // --- Getters y Setters ---

    /**
     * @return El nombre de la rutina.
     */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return La lista de IDs de los ejercicios incluidos.
     */
    public List<Long> getEjercicioIds() {
        return ejercicioIds;
    }

    public void setEjercicioIds(List<Long> ejercicioIds) {
        this.ejercicioIds = ejercicioIds;
    }
}