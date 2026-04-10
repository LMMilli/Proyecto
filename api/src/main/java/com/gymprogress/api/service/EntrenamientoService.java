package com.gymprogress.api.service;

import com.gymprogress.api.dto.EntrenamientoRequest;
import com.gymprogress.api.dto.SerieRequest;
import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.model.Entrenamiento;
import com.gymprogress.api.model.Serie;
import com.gymprogress.api.model.Usuario;
import com.gymprogress.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntrenamientoService {

    @Autowired private EntrenamientoRepository entrenamientoRepository;
    @Autowired private SerieRepository serieRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RutinaRepository rutinaRepository;
    @Autowired private EjercicioRepository ejercicioRepository;

    //@Transactional asegura que si algo falla a medias, no se guarade basura en la base de datos
    @Transactional
    public Entrenamiento procesarNuevoEntrenamiento(EntrenamientoRequest request) throws Exception{
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            throw new Exception("Error: Usuario no encontrado");
        }

        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setUsuario(usuarioOpt.get());
        entrenamiento.setFecha(LocalDateTime.now());
        entrenamiento.setDuracionMinutos(request.getDuracionMinutos());

        if(request.getRutinaId() != null){
            rutinaRepository.findById(request.getRutinaId()).ifPresent(entrenamiento::setRutina);
        }

        Entrenamiento entrenamientoGuardado = entrenamientoRepository.save(entrenamiento);
        List<Serie> seriesGuardadas = new ArrayList<>();

        for (SerieRequest serieReq : request.getSeries()){
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
        return entrenamientoGuardado;
    }
}
