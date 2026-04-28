package com.gymprogress.api.controller;

import com.gymprogress.api.dto.EntrenamientoRequest;
import com.gymprogress.api.model.Entrenamiento;
import com.gymprogress.api.repository.EntrenamientoRepository;
import com.gymprogress.api.service.EntrenamientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST principal para la gestión de las sesiones de entrenamiento.
 * <p>
 * Aplica el patrón de diseño de separación de responsabilidades: delega la lógica de negocio
 * y guardado complejo al Servicio ({@link EntrenamientoService}), mientras que utiliza el
 * Repositorio ({@link EntrenamientoRepository}) únicamente para operaciones de lectura directas.
 * Gestiona también las respuestas HTTP y sus códigos de estado.
 * </p>
 */
@RestController // Indica que los métodos de esta clase devolverán datos (JSON) y no vistas.
@RequestMapping("/api/entrenamientos") // Ruta base para todos los endpoints de entrenamientos.
public class EntrenamientoController {

    /**
     * Servicio que encapsula la lógica de negocio compleja (validaciones cruzadas, cálculos, etc.).
     */
    @Autowired // Inyección de dependencias para el servicio.
    private EntrenamientoService entrenamientoService;

    /**
     * Repositorio para accesos directos y de solo lectura a la base de datos.
     */
    @Autowired // Inyección de dependencias para el repositorio.
    private EntrenamientoRepository entrenamientoRepository;

    /**
     * Endpoint para registrar una nueva sesión de entrenamiento completa.
     * <p>
     * Recibe un DTO de solicitud, valida su estructura y delega el procesamiento al servicio.
     * Responde a peticiones HTTP POST en la ruta "/api/entrenamientos".
     * </p>
     *
     * @param request Objeto DTO (Data Transfer Object) con los datos del entrenamiento.
     * La anotación @Valid asegura que cumpla con las restricciones de validación antes de entrar al método.
     * @return {@link ResponseEntity} con el entrenamiento guardado (200 OK) o un mensaje de error (400 Bad Request).
     */
    @PostMapping
    public ResponseEntity<?> registarEntrenamiento(@Valid @RequestBody EntrenamientoRequest request) {
        try {
            // El controlador actúa como "director de tráfico", delegando la lógica pesada al Service
            Entrenamiento entrenamientoGuardado = entrenamientoService.procesarNuevoEntrenamiento(request);

            // Si el servicio procesa todo correctamente, devolvemos un código de estado 200 (OK) junto con el objeto guardado
            return ResponseEntity.ok(entrenamientoGuardado);

        } catch (Exception e) {
            // Si ocurre alguna excepción (ej. datos inconsistentes, usuario inexistente),
            // capturamos el error y devolvemos un estado 400 (Bad Request) con el mensaje descriptivo
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener el historial completo de entrenamientos de un usuario específico.
     * <p>
     * Responde a peticiones HTTP GET en la ruta "/api/entrenamientos/usuario/{usuarioId}".
     * </p>
     *
     * @param usuarioId ID del usuario del que se quiere consultar el historial (extraído de la URL).
     * @return {@link ResponseEntity} que contiene una lista de entrenamientos ordenados por fecha descendente.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrenamiento>> obtenerHistorialEntrenamientos(@PathVariable Long usuarioId) {
        // Al ser una consulta simple, llamamos directamente al repositorio sin pasar por el Service.
        // @PathVariable asocia la variable {usuarioId} de la URL al parámetro del método.
        List<Entrenamiento> historial = entrenamientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        // Devolvemos la lista con un código 200 OK. Si la lista está vacía, simplemente devuelve un JSON de array vacío [].
        return ResponseEntity.ok(historial);
    }

    /**
     * Endpoint para obtener los detalles de un entrenamiento concreto a partir de su ID.
     * <p>
     * Responde a peticiones HTTP GET en la ruta "/api/entrenamientos/{id}".
     * </p>
     *
     * @param id ID único del entrenamiento que se desea consultar (extraído de la URL).
     * @return {@link ResponseEntity} con el objeto entrenamiento si existe (200 OK), o un estado 404 (Not Found) si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerDetalleEntrenamiento(@PathVariable("id") Long id) {
        // Utilizamos Optional para manejar de forma segura la posibilidad de que el entrenamiento no exista en la BD.
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findById(id);

        if (entrenamiento.isPresent()) {
            // Si el entrenamiento existe en el Optional, lo extraemos con .get() y lo devolvemos con 200 OK.
            return ResponseEntity.ok(entrenamiento.get());
        } else {
            // Si no existe, devolvemos un código de estado 404 Not Found (sin cuerpo).
            return ResponseEntity.notFound().build();
        }
    }
}