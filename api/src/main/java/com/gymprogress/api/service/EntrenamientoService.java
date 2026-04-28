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

/**
 * Servicio encargado de la lógica de negocio para los entrenamientos.
 * <p>
 * Su función principal es orquestar la persistencia de una sesión completa,
 * asegurando que se guarden correctamente el entrenamiento, sus ejercicios y sus series.
 * </p>
 */
@Service
public class EntrenamientoService {

    // Inyección de todos los repositorios necesarios para reconstruir el objeto complejo
    @Autowired private EntrenamientoRepository entrenamientoRepository;
    @Autowired private SerieRepository serieRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RutinaRepository rutinaRepository;
    @Autowired private EjercicioRepository ejercicioRepository;
    @Autowired private EjercicioEntrenamientoRepository ejercicioEntrenamientoRepository;
    @Autowired private EquipamientoRepository equipamientoRepository;

    /**
     * Procesa y guarda un entrenamiento completo desde una petición (DTO).
     * <p>
     * @Transactional: Esta anotación es CRÍTICA. Asegura que si algo falla durante el proceso
     * (por ejemplo, al guardar la serie número 50), se haga un ROLLBACK de todo. Así evitamos
     * que se guarde un entrenamiento "a medias" o con datos huérfanos.
     * </p>
     *
     * @param request Datos del entrenamiento enviados desde el cliente.
     * @return El objeto Entrenamiento persistido y con todas sus relaciones cargadas.
     * @throws Exception Si el usuario no existe o hay errores de validación.
     */
    @Transactional
    public Entrenamiento procesarNuevoEntrenamiento(EntrenamientoRequest request) throws Exception {

        // Validación inicial: Sin usuario no hay entrenamiento
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            throw new Exception("Error: Usuario no encontrado");
        }

        // 1. Crear y guardar el entrenamiento base (Cabecera)
        Entrenamiento entrenamiento = new Entrenamiento();
        entrenamiento.setUsuario(usuarioOpt.get());
        entrenamiento.setFecha(LocalDateTime.now());
        entrenamiento.setDuracionMinutos(request.getDuracionMinutos());

        // Si el usuario siguió una rutina predefinida, la vinculamos
        if(request.getRutinaId() != null){
            rutinaRepository.findById(request.getRutinaId()).ifPresent(entrenamiento::setRutina);
        }

        // Guardamos primero el padre para tener un ID generado
        Entrenamiento entrenamientoGuardado = entrenamientoRepository.save(entrenamiento);
        List<EjercicioEntrenamiento> bloquesGuardados = new ArrayList<>();

        // 2. Recorrer los bloques de ejercicios (Lógica anidada)
        for (EjercicioEntrenamientoRequest bloqueReq : request.getEjercicios()){
            Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(bloqueReq.getEjercicioId());

            if(ejercicioOpt.isPresent()){
                // Creamos el vínculo entre el entrenamiento y el ejercicio del catálogo
                EjercicioEntrenamiento nuevoBloque = new EjercicioEntrenamiento();
                nuevoBloque.setEntrenamiento(entrenamientoGuardado);
                nuevoBloque.setEjercicio(ejercicioOpt.get());
                nuevoBloque.setOrden(bloqueReq.getOrden());
                nuevoBloque.setNotas(bloqueReq.getNotas());

                // Asignamos el equipamiento (mancuernas, barra, etc.) si se especifica
                if(bloqueReq.getEquipamientoId() != null){
                    equipamientoRepository.findById(bloqueReq.getEquipamientoId()).ifPresent(nuevoBloque::setEquipamiento);
                }

                // Guardamos el bloque intermedio
                EjercicioEntrenamiento bloqueGuardado = ejercicioEntrenamientoRepository.save(nuevoBloque);
                List<Serie> seriesDelBloque = new ArrayList<>();

                // 3. Recorrer las series dentro de cada bloque de ejercicio
                for(SerieRequest serieReq : bloqueReq.getSeries()){
                    Serie nuevaSerie = new Serie();
                    nuevaSerie.setEjercicioEntrenamiento(bloqueGuardado); // Vinculamos la serie a su bloque
                    nuevaSerie.setRepeticiones(serieReq.getRepeticiones());
                    nuevaSerie.setPeso(serieReq.getPeso());
                    nuevaSerie.setRpe(serieReq.getRpe());
                    nuevaSerie.setTipo(serieReq.getTipo());

                    // Guardamos la unidad mínima: la serie
                    serieRepository.save(nuevaSerie);
                    seriesDelBloque.add(nuevaSerie);
                }

                // Recomponemos el objeto en memoria para devolverlo completo
                bloqueGuardado.setSeries(seriesDelBloque);
                bloquesGuardados.add(bloqueGuardado);
            }
        }

        entrenamientoGuardado.setEjerciciosEntrenamiento(bloquesGuardados);
        return entrenamientoGuardado;
    }
}