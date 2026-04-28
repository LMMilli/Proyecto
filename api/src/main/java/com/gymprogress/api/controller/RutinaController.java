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
 * <p>
 * Permite consultar las rutinas predefinidas del sistema y crear nuevas combinaciones
 * de ejercicios. Al ser de ámbito global, estas rutinas estarán disponibles para todos los usuarios.
 * </p>
 */
@RestController // Define la clase como controlador de la API RESTful. Las respuestas se serializarán a JSON.
@RequestMapping("/api/rutinas") // Ruta base para todos los endpoints relacionados con las rutinas.
public class RutinaController {

    /**
     * Repositorio para persistir y consultar las rutinas en la base de datos.
     */
    @Autowired
    private RutinaRepository rutinaRepository;

    /**
     * Repositorio para acceder a los datos de los ejercicios y poder vincularlos a las rutinas.
     */
    @Autowired
    private EjercicioRepository ejercicioRepository;

    /**
     * Endpoint para crear una nueva rutina y añadirla al catálogo global.
     * <p>
     * Responde a peticiones HTTP POST en la ruta "/api/rutinas".
     * </p>
     *
     * @param request Objeto DTO que contiene el nombre de la rutina y una lista de IDs con los ejercicios a incluir.
     * @return {@link ResponseEntity} con la rutina guardada (200 OK) o un error (400 Bad Request) si la lista de ejercicios está vacía.
     */
    @PostMapping
    public ResponseEntity<?> crearRutina(@RequestBody RutinaRequest request){

        // 1. Validar de forma preventiva que la petición contenga al menos un ejercicio.
        // Una rutina sin ejercicios carece de sentido lógico en el dominio de la aplicación.
        if (request.getEjercicioIds() == null || request.getEjercicioIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La lista de ejercicios está vacía.");
        }

        // 2. Buscar en la base de datos todos los ejercicios correspondientes a los IDs recibidos.
        // Se utiliza findAllById por eficiencia, ya que ejecuta una única consulta SQL (WHERE id IN (...)) en lugar de múltiples SELECTs.
        List<Ejercicio> ejercicios = ejercicioRepository.findAllById(request.getEjercicioIds());

        // 3. Crear la nueva instancia de la entidad Rutina y mapear el nombre recibido en el DTO.
        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setNombre(request.getNombre());

        // 4. Asociar los ejercicios encontrados a la rutina.
        // Hibernate/JPA gestionará automáticamente la inserción en la tabla intermedia (Many-to-Many) al guardar.
        nuevaRutina.setEjercicios(ejercicios);

        // 5. Persistir la rutina en la base de datos (junto con sus relaciones) y devolverla.
        Rutina rutinaGuardada = rutinaRepository.save(nuevaRutina);

        return ResponseEntity.ok(rutinaGuardada);
    }

    /**
     * Endpoint para obtener el catálogo completo de rutinas disponibles en la aplicación.
     * <p>
     * Responde a peticiones HTTP GET en la ruta "/api/rutinas".
     * </p>
     *
     * @return {@link ResponseEntity} con la lista de todas las rutinas globales y sus respectivos ejercicios.
     */
    @GetMapping
    public ResponseEntity<List<Rutina>> obtenerTodas(){
        // Recupera todas las rutinas de la base de datos. Al ser globales, no se filtra por usuario.
        List<Rutina> rutinas = rutinaRepository.findAll();

        // Envuelve la lista en un código HTTP 200 OK.
        return ResponseEntity.ok(rutinas);
    }
}