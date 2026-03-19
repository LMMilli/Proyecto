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

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {
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

    @PostMapping
    public ResponseEntity<?> registarEntrenamiento(@RequestBody EntrenamientoRequest request){

        Optional<Usuario> usuarioOpt = usuarioRepository.findById((request.getUsuarioId()));
        if(usuarioOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("Error: Usuario no encontrado"));
        }

        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setUsuario(usuarioOpt.get());
        entrenamiento.setFecha(LocalDateTime.now());
        entrenamiento.setDuracionMinutos(request.getDuracionMinutos());

        if(request.getRutinaId() != null){
            Optional<Rutina> rutinaOpt = rutinaRepository.findById((request.getRutinaId()));
            rutinaOpt.ifPresent(entrenamiento::setRutina);
        }

        Entrenamiento entrenamientoGuardado = entrenamientoRepository.save(entrenamiento);

        List<Serie> seriesGuardadas = new ArrayList<>();

        for (SerieRequest serieReq : request.getSeries()) {
            Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(serieReq.getEjercicioId());

            if(ejercicioOpt.isPresent()){
                Serie nuevaSerie = new Serie();
                nuevaSerie.setEntrenamiento(entrenamientoGuardado);
                nuevaSerie.setEjercicio(ejercicioOpt.get());
                nuevaSerie.setRepeticiones(serieReq.getRepeticiones());
                nuevaSerie.setPeso(serieReq.getPeso());
                nuevaSerie.setRpe(serieReq.getRpe());

                serieRepository.save(nuevaSerie);

                seriesGuardadas.add(nuevaSerie);
            }
        }

        entrenamientoGuardado.setSeries(seriesGuardadas);
        return ResponseEntity.ok(entrenamientoGuardado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Entrenamiento>> obtenerHistorialEntrenamientos(@PathVariable Long usuarioId){
        List<Entrenamiento> historial = entrenamientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        return ResponseEntity.ok(historial);
    }
}
