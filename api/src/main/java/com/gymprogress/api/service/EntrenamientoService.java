package com.gymprogress.api.service;


import com.gymprogress.api.dto.EjercicioEntrenamientoRequest;
import com.gymprogress.api.dto.EntrenamientoRequest;
import com.gymprogress.api.dto.SerieRequest;
import com.gymprogress.api.model.*;
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
    @Autowired private EjercicioEntrenamientoRepository ejercicioEntrenamientoRepository;
    @Autowired private EquipamientoRepository equipamientoRepository;

    @Transactional
    public Entrenamiento procesarNuevoEntrenamiento(EntrenamientoRequest request) throws Exception{

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            throw new Exception("Error: Usuario no ecntrado");
        }

        //1. Crear y guardar el entrenamiento base
        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setUsuario(usuarioOpt.get());
        entrenamiento.setFecha(LocalDateTime.now());
        entrenamiento.setDuracionMinutos(request.getDuracionMinutos());

        if(request.getRutinaId() != null){
            rutinaRepository.findById(request.getRutinaId()).ifPresent(entrenamiento::setRutina);
        }

        Entrenamiento entrenamientoGuardado = entrenamientoRepository.save(entrenamiento);
        List<EjercicioEntrenamiento> bloquesGuardados = new ArrayList<>();

        //2. Recorrer los bloques
        for (EjercicioEntrenamientoRequest bloqueReq : request.getEjercicios()){
            Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(bloqueReq.getEjercicioId());

            if(ejercicioOpt.isPresent()){
                EjercicioEntrenamiento nuevoBloque = new EjercicioEntrenamiento();
                nuevoBloque.setEntrenamiento(entrenamientoGuardado);
                nuevoBloque.setEjercicio(ejercicioOpt.get());
                nuevoBloque.setOrden(bloqueReq.getOrden());
                nuevoBloque.setNotas(bloqueReq.getNotas());

                //Buscar y asignar el equipamiento
                if(bloqueReq.getEquipamientoId() != null){
                    equipamientoRepository.findById(bloqueReq.getEquipamientoId()).ifPresent(nuevoBloque::setEquipamiento);
                }

                //Guardar el bloque en la bd
                EjercicioEntrenamiento bloqueGuardado = ejercicioEntrenamientoRepository.save(nuevoBloque);
                List<Serie> seriesDelBloque = new ArrayList<>();

                //Recorre las serries que van dentro de este bloque
                for(SerieRequest serieReq : bloqueReq.getSeries()){
                    Serie nuevaSerie = new Serie();
                    nuevaSerie.setEjercicioEntrenamiento(bloqueGuardado);
                    nuevaSerie.setRepeticiones(serieReq.getRepeticiones());
                    nuevaSerie.setPeso(serieReq.getPeso());
                    nuevaSerie.setRpe(serieReq.getRpe());
                    nuevaSerie.setTipo(serieReq.getTipo());

                    serieRepository.save(nuevaSerie);
                    seriesDelBloque.add(nuevaSerie);
                }

                //Enganchar las series al bloque, y el bloque a la lista final
                bloqueGuardado.setSeries(seriesDelBloque);
                bloquesGuardados.add(bloqueGuardado);

            }
        }

        entrenamientoGuardado.setEjerciciosEntrenamiento(bloquesGuardados);
        return entrenamientoGuardado;
    }

}
