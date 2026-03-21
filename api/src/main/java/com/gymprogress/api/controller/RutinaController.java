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

/**
 * Controlador REST para la gestión del catálogo global de Rutinas.
 * Permite consultar las rutinas predefinidas del sistema y crear nuevas combinaciones de ejercicios.
 */
@RestController // Define la clase como controlador de la API RESTful
@RequestMapping("/api/rutinas") // Ruta base para los endpoints de rutinas
public class RutinaController {

    /* * Inyección de dependencias para acceder a las tablas de rutinas y ejercicios. */
    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    /**
     * Endpoint para crear una nueva rutina y añadirla al catálogo global.
     * Método HTTP: POST
     * URL: /api/rutinas
     * * @param request Objeto DTO que contiene el nombre de la rutina y la lista de IDs de los ejercicios.
     * @return ResponseEntity con la rutina guardada (HTTP 200) o un error (HTTP 400) si no hay ejercicios.
     */
    @PostMapping
    public ResponseEntity<?> crearRutina(@RequestBody RutinaRequest request){

        // 1. Validar de forma preventiva que la petición contenga al menos un ejercicio
        if (request.getEjercicioIds() == null || request.getEjercicioIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La lista de ejercicios está vacía.");
        }

        // 2. Buscar en la base de datos todos los ejercicios correspondientes a los IDs recibidos.
        // Se utiliza findAllById por eficiencia, ya que ejecuta una única consulta SQL (WHERE id IN (...))
        List<Ejercicio> ejercicios = ejercicioRepository.findAllById(request.getEjercicioIds());

        // 3. Crear la nueva instancia de Rutina y mapear los datos recibidos
        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setNombre(request.getNombre());

        // 4. Asociar los ejercicios encontrados (esto gestionará la tabla intermedia N:M)
        nuevaRutina.setEjercicios(ejercicios);

        // 5. Persistir la rutina en la base de datos
        Rutina rutinaGuardada = rutinaRepository.save(nuevaRutina);

        return ResponseEntity.ok(rutinaGuardada);
    }

    /**
     * Endpoint para obtener el catálogo completo de rutinas disponibles en la aplicación.
     * Método HTTP: GET
     * URL: /api/rutinas
     * * @return Lista de todas las rutinas globales con sus respectivos ejercicios.
     */
    @GetMapping
    public ResponseEntity<List<Rutina>> obtenerTodas(){
        // Recupera todas las rutinas sin filtrar por usuario, ya que ahora son de dominio público
        List<Rutina> rutinas = rutinaRepository.findAll();

        return ResponseEntity.ok(rutinas);
    }
}