package com.gymprogress.api.controller;

import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Ejercicios.
 * <p>
 * Esta clase expone los endpoints de la API necesarios para interactuar
 * con los ejercicios globales disponibles en la aplicación del gimnasio.
 * </p>
 */
@RestController // Indica a Spring que esta clase es un controlador REST. Las respuestas de sus métodos se serializarán automáticamente a JSON.
@RequestMapping("/api/ejercicios") // Define la ruta base (URL) para todos los endpoints configurados dentro de esta clase.
public class EjercicioController {

    /**
     * Repositorio para acceder a las operaciones de base de datos de la entidad Ejercicio.
     */
    @Autowired // Inyección de dependencias: Spring Boot crea e inyecta automáticamente la instancia de este repositorio.
    private EjercicioRepository ejercicioRepository;

    /**
     * Endpoint para obtener el catálogo completo de ejercicios.
     * <p>
     * Responde a peticiones HTTP GET en la ruta base "/api/ejercicios".
     * </p>
     *
     * @return Una lista (List) que contiene todos los objetos de tipo {@link Ejercicio} almacenados en la base de datos.
     */
    @GetMapping // Mapea específicamente las peticiones de tipo HTTP GET a este método.
    public List<Ejercicio> findAll() {
        // Delega la operación al repositorio, ejecutando internamente un "SELECT * FROM ejercicios"
        return ejercicioRepository.findAll();
    }

    /**
     * Endpoint para registrar un nuevo ejercicio en el catálogo.
     * <p>
     * Responde a peticiones HTTP POST en la ruta base "/api/ejercicios".
     * </p>
     *
     * @param ejercicio Objeto de tipo {@link Ejercicio} construido automáticamente a partir del JSON recibido en el cuerpo de la petición (Body).
     * @return El objeto {@link Ejercicio} recién creado y guardado (incluyendo el ID generado por la base de datos).
     */
    @PostMapping // Mapea específicamente las peticiones de tipo HTTP POST a este método.
    public Ejercicio crearEjercicio(@RequestBody Ejercicio ejercicio) {
        // @RequestBody es fundamental aquí: convierte el texto JSON de la petición HTTP en una instancia de la clase Java Ejercicio.

        // Llama al repositorio para hacer un "INSERT" en la base de datos y devuelve la entidad persistida.
        return ejercicioRepository.save(ejercicio);
    }
}