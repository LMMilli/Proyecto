package com.gymprogress.api.controller;

import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Ejercicios.
 * Expone los endpoints necesarios para el catálogo global de ejercicios del gimnasio.
 */
@RestController// Indica que esta clase es un controlador de una API REST (devuelve JSON, no vistas HTML)
@RequestMapping("/api/ejercicios")// Define la ruta base para todas las peticiones de este controlador
public class EjercicioController {
    @Autowired // Inyección de dependencias: Spring Boot nos proporciona el repositorio automáticamente
    private EjercicioRepository ejercicioRepository;

    /**
     * Endpoint para obtener el catálogo completo de ejercicios.
     * Método HTTP: GET
     * URL: /api/ejercicios
     * * @return Lista de todos los ejercicios almacenados en la base de datos.
     */
    @GetMapping
    // Llama al repositorio para buscar todos los registros en la tabla 'ejercicios'
    public List<Ejercicio> findAll() {
        return ejercicioRepository.findAll();
    }

    /**
     * Endpoint para registrar un nuevo ejercicio en el catálogo.
     * Método HTTP: POST
     * URL: /api/ejercicios
     * * @param ejercicio Objeto Ejercicio recibido en formato JSON desde el cuerpo de la petición (Body).
     * @return El ejercicio recién creado y guardado (con su ID generado por la base de datos).
     */
    @PostMapping
    public Ejercicio crearEjercicio(@RequestBody Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }
}
