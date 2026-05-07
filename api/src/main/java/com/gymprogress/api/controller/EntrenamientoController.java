package com.gymprogress.api.controller;

import com.gymprogress.api.dto.*;
import com.gymprogress.api.model.Entrenamiento;
import com.gymprogress.api.repository.EntrenamientoRepository;
import com.gymprogress.api.service.EntrenamientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    @Autowired
    private EntrenamientoService entrenamientoService;

    @Autowired
    private EntrenamientoRepository entrenamientoRepository;

    @PostMapping
    public ResponseEntity<?> registarEntrenamiento(@Valid @RequestBody EntrenamientoRequest request) {
        try {
            Entrenamiento entrenamientoGuardado = entrenamientoService.procesarNuevoEntrenamiento(request);
            // Devolvemos el DTO limpio también al guardar
            return ResponseEntity.ok(mapearAEntrenamientoResponse(entrenamientoGuardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EntrenamientoResponse>> obtenerHistorialEntrenamientos(@PathVariable Long usuarioId) {
        List<Entrenamiento> historial = entrenamientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        // Convertimos la lista de la Base de Datos a DTOs limpios
        List<EntrenamientoResponse> respuestas = historial.stream()
                .map(this::mapearAEntrenamientoResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoResponse> obtenerDetalleEntrenamiento(@PathVariable("id") Long id) {
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findById(id);

        if (entrenamiento.isPresent()) {
            return ResponseEntity.ok(mapearAEntrenamientoResponse(entrenamiento.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- MÉTODO PRIVADO PARA MAPEAR LA ENTIDAD AL DTO Y ROMPER EL BUCLE ---
    private EntrenamientoResponse mapearAEntrenamientoResponse(Entrenamiento entidad) {
        EntrenamientoResponse response = new EntrenamientoResponse();
        response.setId(entidad.getId());
        response.setFecha(entidad.getFecha());
        response.setDuracionMinutos(entidad.getDuracionMinutos());

        if (entidad.getUsuario() != null) {
            response.setUsuarioId(entidad.getUsuario().getId());
        }
        if (entidad.getRutina() != null) {
            response.setNombreRutina(entidad.getRutina().getNombre());
        }

        if (entidad.getEjerciciosEntrenamiento() != null) {
            List<EjercicioEntrenamientoResponse> ejerciciosDto = entidad.getEjerciciosEntrenamiento().stream().map(ej -> {
                EjercicioEntrenamientoResponse ejDto = new EjercicioEntrenamientoResponse();
                ejDto.setId(ej.getId());
                ejDto.setOrden(ej.getOrden());
                ejDto.setNotas(ej.getNotas());

                if (ej.getEjercicio() != null) {
                    ejDto.setEjercicioId(ej.getEjercicio().getId());
                    ejDto.setNombreEjercicio(ej.getEjercicio().getNombre());
                }
                if (ej.getEquipamiento() != null) {
                    ejDto.setNombreEquipamiento(ej.getEquipamiento().getNombre());
                }

                if (ej.getSeries() != null) {
                    List<SerieResponse> seriesDto = ej.getSeries().stream().map(s -> {
                        SerieResponse sDto = new SerieResponse();
                        sDto.setId(s.getId());
                        sDto.setPeso(s.getPeso());
                        sDto.setRepeticiones(s.getRepeticiones());
                        sDto.setRpe(s.getRpe());
                        sDto.setTipo(s.getTipo());
                        return sDto;
                    }).collect(Collectors.toList());
                    ejDto.setSeries(seriesDto);
                }
                return ejDto;
            }).collect(Collectors.toList());
            response.setEjercicios(ejerciciosDto);
        }
        return response;
    }
}