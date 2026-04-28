package com.gymprogress.api.controller;

import com.gymprogress.api.dto.MedidaRequest;
import com.gymprogress.api.model.Medida;
import com.gymprogress.api.model.Usuario;
import com.gymprogress.api.repository.MedidaRepository;
import com.gymprogress.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de la composición corporal y medidas de los usuarios.
 * <p>
 * Permite registrar nuevos pesajes y consultar el historial evolutivo del atleta.
 * </p>
 */
@RestController // Define la clase como controlador de la API RESTful. Las respuestas se serializarán a JSON.
@RequestMapping("/api/medidas") // Ruta base para todos los endpoints relacionados con medidas corporales.
public class MedidaController {

    /**
     * Repositorio para persistir y consultar las medidas corporales en la base de datos.
     */
    @Autowired
    private MedidaRepository medidaRepository;

    /**
     * Repositorio para acceder a los datos de los usuarios y validar su existencia.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para registrar una nueva medición corporal de un usuario.
     * <p>
     * Responde a peticiones HTTP POST en la ruta "/api/medidas".
     * </p>
     *
     * @param request Objeto DTO (Data Transfer Object) que contiene los datos de la medida (peso, porcentaje de grasa y ID del usuario).
     * @return {@link ResponseEntity} con la medida guardada (200 OK) o un error (400 Bad Request) si el usuario no existe.
     */
    @PostMapping
    public ResponseEntity<?> registrarMedida(@RequestBody MedidaRequest request){

        // 1. Validar la existencia del usuario en la base de datos a partir del ID recibido en el DTO
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            // Si el Optional está vacío, el usuario no existe. Devolvemos un código 400 descriptivo.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        }

        // 2. Instanciar la nueva medida y mapear los datos recibidos desde el DTO
        Medida nuevaMedida = new Medida();
        nuevaMedida.setUsuario(usuarioOpt.get()); // Extraemos la entidad usuario validada del Optional
        nuevaMedida.setPesoCorporal(request.getPeso());
        nuevaMedida.setPorcentajeGrasa(request.getPorcentajeGrasa());

        // 3. Establecer la fecha actual automáticamente desde el servidor
        // Esto es crucial para asegurar la consistencia de los datos y evitar manipulaciones desde el cliente (Android/Web)
        nuevaMedida.setFecha(LocalDate.now());

        // 4. Guardar en base de datos y devolver la respuesta con la entidad persistida (incluyendo su ID autogenerado)
        Medida medidaGuardada = medidaRepository.save(nuevaMedida);
        return ResponseEntity.ok(medidaGuardada);
    }

    /**
     * Endpoint para consultar el historial de medidas de un usuario en concreto.
     * <p>
     * Responde a peticiones HTTP GET en la ruta "/api/medidas/usuario/{usuarioId}".
     * </p>
     *
     * @param usuarioId Identificador único del usuario (extraído directamente de la URL).
     * @return {@link ResponseEntity} que contiene una lista de medidas ordenadas cronológicamente de forma descendente.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Medida>> obtenerHistorial(@PathVariable Long usuarioId){
        // Utiliza un "Query Method" derivado del repositorio para filtrar por usuario y ordenar directamente en la consulta SQL
        List<Medida> historial = medidaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        // Retorna la lista en formato JSON con un código HTTP 200 (OK). Si no hay medidas, devolverá un array vacío [].
        return ResponseEntity.ok(historial);
    }
}