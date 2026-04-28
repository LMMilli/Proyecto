package com.gymprogress.api.controller;

import com.gymprogress.api.model.Equipamiento;
import com.gymprogress.api.repository.EquipamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la gestión del Equipamiento del gimnasio.
 * <p>
 * Esta clase expone los endpoints necesarios para consultar el catálogo de
 * material disponible (máquinas, mancuernas, barras, etc.) en la aplicación.
 * </p>
 */
@RestController // Indica que esta clase es un controlador REST y sus respuestas se convertirán a JSON.
@RequestMapping("/api/equipamiento") // Define la ruta base para todas las peticiones relacionadas con el equipamiento.
public class EquipamientoController {

    /**
     * Repositorio para acceder a los datos de la entidad Equipamiento en la base de datos.
     */
    @Autowired // Inyección de dependencias: Spring proporciona automáticamente la instancia del repositorio.
    private EquipamientoRepository equipamientoRepository;

    /**
     * Endpoint para obtener la lista completa de equipamiento disponible.
     * <p>
     * Responde a peticiones HTTP GET en la ruta base "/api/equipamiento".
     * </p>
     *
     * @return Una lista (List) con todos los objetos de tipo {@link Equipamiento} registrados en el sistema.
     */
    @GetMapping // Mapea las peticiones HTTP GET a este método específico.
    public List<Equipamiento> findAll() {
        // Ejecuta una consulta a la base de datos para recuperar todos los registros de la tabla de equipamiento.
        return equipamientoRepository.findAll();
    }
}