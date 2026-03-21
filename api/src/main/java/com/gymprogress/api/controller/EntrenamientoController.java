package com.gymprogress.api.controller;

import com.gymprogress.api.dto.EntrenamientoRequest;
import com.gymprogress.api.dto.SerieRequest;
import com.gymprogress.api.model.*;
import com.gymprogress.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST principal para la gestión de las sesiones de entrenamiento.
 * Se encarga de registrar nuevos entrenamientos con sus respectivas series,
 * así como de devolver el historial de actividad de los usuarios.
 */
@RestController // Define la clase como controlador de la API RESTful
@RequestMapping("/api/entrenamientos") // Ruta base para los endpoints de entrenamientos
public class EntrenamientoController {

    /* * Inyección de todos los repositorios necesarios.
     * Al guardar un entrenamiento completo, necesitamos acceder a múltiples tablas
     * para verificar que los IDs enviados por el cliente realmente existen.
     */
    @Autowired
    private EntrenamientoRepository entrenamientoRepository;
    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private EjercicioRepository ejercicioRepository;

    /**
     * Endpoint para registrar un entrenamiento completo junto con todas sus series.
     * Método HTTP: POST
     * URL: /api/entrenamientos
     * * @param request Objeto DTO (Data Transfer Object) que contiene los datos del entrenamiento y una lista de series.
     * @return ResponseEntity con el entrenamiento guardado o un mensaje de error si el usuario no existe.
     */
    @PostMapping
    public ResponseEntity<?> registarEntrenamiento(@RequestBody EntrenamientoRequest request){

        // 1. Validar que el usuario que intenta registrar el entrenamiento existe en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findById((request.getUsuarioId()));
        if(usuarioOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("Error: Usuario no encontrado"));
        }

        // 2. Crear la entidad principal del Entrenamiento y asignar sus valores básicos
        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setUsuario(usuarioOpt.get());
        entrenamiento.setFecha(LocalDateTime.now()); // Marca de tiempo automática en el servidor
        entrenamiento.setDuracionMinutos(request.getDuracionMinutos());

        // 3. Comprobar si el entrenamiento se basa en una rutina global existente
        if(request.getRutinaId() != null){
            Optional<Rutina> rutinaOpt = rutinaRepository.findById((request.getRutinaId()));
            rutinaOpt.ifPresent(entrenamiento::setRutina); // Si existe, la asocia al entrenamiento
        }

        // 4. Guardar el entrenamiento inicial para generar su ID (necesario para las series)
        Entrenamiento entrenamientoGuardado = entrenamientoRepository.save(entrenamiento);

        // 5. Procesar la lista de series recibidas en la petición
        List<Serie> seriesGuardadas = new ArrayList<>();

        for (SerieRequest serieReq : request.getSeries()) {
            // Verificar que el ejercicio indicado en la serie existe en el catálogo
            Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(serieReq.getEjercicioId());

            if(ejercicioOpt.isPresent()){
                // Crear la entidad Serie y vincularla a su entrenamiento y ejercicio
                Serie nuevaSerie = new Serie();
                nuevaSerie.setEntrenamiento(entrenamientoGuardado);
                nuevaSerie.setEjercicio(ejercicioOpt.get());
                nuevaSerie.setRepeticiones(serieReq.getRepeticiones());
                nuevaSerie.setPeso(serieReq.getPeso());
                nuevaSerie.setRpe(serieReq.getRpe());

                // Guardar la serie en la base de datos
                serieRepository.save(nuevaSerie);

                // Añadir a la lista temporal para la respuesta JSON
                seriesGuardadas.add(nuevaSerie);
            }
        }

        // 6. Adjuntar las series al objeto final y devolver respuesta HTTP 200 (OK)
        entrenamientoGuardado.setSeries(seriesGuardadas);
        return ResponseEntity.ok(entrenamientoGuardado);
    }

    /**
     * Endpoint para obtener todo el historial de entrenamientos de un usuario concreto.
     * Método HTTP: GET
     * URL: /api/entrenamientos/usuario/{usuarioId}
     * * @param usuarioId Identificador único del usuario (extraído de la URL).
     * @return Lista de entrenamientos ordenados por fecha descendente.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrenamiento>> obtenerHistorialEntrenamientos(@PathVariable Long usuarioId){
        // Llama al método personalizado del repositorio que busca por ID y ordena de más reciente a más antiguo
        List<Entrenamiento> historial = entrenamientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        return ResponseEntity.ok(historial);
    }
}