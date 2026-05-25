package com.gymprogress.api.controller;

import com.gymprogress.api.dto.EjercicioResponse;
import com.gymprogress.api.dto.RutinaRequest;
import com.gymprogress.api.dto.RutinaResponse;
import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.model.Rutina;
import com.gymprogress.api.repository.EjercicioRepository;
import com.gymprogress.api.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @PostMapping
    public ResponseEntity<?> crearRutina(@RequestBody RutinaRequest request){
        if (request.getEjercicioIds() == null || request.getEjercicioIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La lista de ejercicios está vacía.");
        }

        List<Ejercicio> ejercicios = ejercicioRepository.findAllById(request.getEjercicioIds());

        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setNombre(request.getNombre());
        nuevaRutina.setEjercicios(ejercicios);
        nuevaRutina.setTipo(request.getTipo());

        Rutina rutinaGuardada = rutinaRepository.save(nuevaRutina);

        // Devolvemos el DTO limpio
        return ResponseEntity.ok(mapearARutinaResponse(rutinaGuardada));
    }

    @GetMapping
    public ResponseEntity<List<RutinaResponse>> obtenerTodas(){
        List<Rutina> rutinas = rutinaRepository.findAll();

        List<RutinaResponse> respuestas = rutinas.stream()
                .map(this::mapearARutinaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuestas);
    }

    // --- MÉTODO PRIVADO PARA MAPEAR LA ENTIDAD AL DTO ---
    private RutinaResponse mapearARutinaResponse(Rutina rutina) {
        RutinaResponse response = new RutinaResponse();
        response.setId(rutina.getId());
        response.setNombre(rutina.getNombre());
        response.setTipo(rutina.getTipo());

        if (rutina.getEjercicios() != null) {
            List<EjercicioResponse> ejerciciosDto = rutina.getEjercicios().stream().map(ej -> {
                EjercicioResponse ejDto = new EjercicioResponse();
                ejDto.setId(ej.getId());
                ejDto.setNombre(ej.getNombre());
                ejDto.setGrupoMuscular(ej.getGrupoMuscular());
                return ejDto;
            }).collect(Collectors.toList());

            response.setEjercicios(ejerciciosDto);
        }

        return response;
    }
}