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
 * Delega la lógica de guardado complejo al Servicio y gestiona las respuestas HTTP.
 */
@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    // 1. Inyectamos nuestro nuevo "cerebro" (el Service)
    @Autowired
    private EntrenamientoService entrenamientoService;

    // 2. Mantenemos el repositorio SOLO para las consultas simples (GET)
    @Autowired
    private EntrenamientoRepository entrenamientoRepository;

    /**
     * Endpoint para registrar un entrenamiento completo.
     * La lógica compleja ahora vive en EntrenamientoService.
     */
    @PostMapping
    public ResponseEntity<?> registarEntrenamiento(@Valid @RequestBody EntrenamientoRequest request) {
        try {
            // El controlador ya no piensa, solo le pasa el JSON de Android al Service
            Entrenamiento entrenamientoGuardado = entrenamientoService.procesarNuevoEntrenamiento(request);

            // Si todo va bien, devolvemos el OK (200)
            return ResponseEntity.ok(entrenamientoGuardado);

        } catch (Exception e) {
            // Si el Service detecta un error (ej. Usuario no existe), devuelve un Bad Request (400)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todo el historial de entrenamientos de un usuario.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrenamiento>> obtenerHistorialEntrenamientos(@PathVariable Long usuarioId) {
        List<Entrenamiento> historial = entrenamientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return ResponseEntity.ok(historial);
    }

    /**
     * Endpoint para obtener el detalle de un entrenamiento concreto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerDetalleEntrenamiento(@PathVariable("id") Long id) {
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findById(id);

        if (entrenamiento.isPresent()) {
            return ResponseEntity.ok(entrenamiento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}