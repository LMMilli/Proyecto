package com.gymprogress.api.controller;

import com.gymprogress.api.dto.RutinaRequest;
import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.model.Rutina;
import com.gymprogress.api.repository.EjercicioRepository;
import com.gymprogress.api.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        Rutina rutinaGuardada = rutinaRepository.save(nuevaRutina);

        return ResponseEntity.ok(rutinaGuardada);
    }

    @GetMapping
    public ResponseEntity<List<Rutina>> obtenerTodas(){
        List<Rutina> rutinas = rutinaRepository.findAll();
        return ResponseEntity.ok(rutinas);
    }
}
