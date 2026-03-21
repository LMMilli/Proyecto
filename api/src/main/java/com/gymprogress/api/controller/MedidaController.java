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
 * Permite registrar nuevos pesajes y consultar el historial evolutivo del atleta.
 */
@RestController // Define la clase como controlador de la API RESTful
@RequestMapping("/api/medidas") // Ruta base para los endpoints de medidas corporales
public class MedidaController {

    /* * Inyección de repositorios necesarios para consultar y persistir la información. */
    @Autowired
    private MedidaRepository medidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para registrar una nueva medición corporal de un usuario.
     * Método HTTP: POST
     * URL: /api/medidas
     * * @param request Objeto DTO (Data Transfer Object) que contiene los datos de la medida (peso, porcentaje de grasa y ID del usuario).
     * @return ResponseEntity con la medida guardada o un error 400 (Bad Request) si el usuario no existe.
     */
    @PostMapping
    public ResponseEntity<?> registrarMedida(@RequestBody MedidaRequest request){

        // 1. Validar la existencia del usuario en la base de datos a partir del ID recibido
        Optional<Usuario> usuarioOpt =  usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        }

        // 2. Instanciar la nueva medida y mapear los datos recibidos desde el DTO
        Medida nuevaMedida = new Medida();
        nuevaMedida.setUsuario(usuarioOpt.get()); // Asignar la entidad usuario encontrada
        nuevaMedida.setPesoCorporal(request.getPeso());
        nuevaMedida.setPorcentajeGrasa(request.getPorcentajeGrasa());

        // 3. Establecer la fecha actual automáticamente desde el servidor para evitar manipulaciones
        nuevaMedida.setFecha(LocalDate.now());

        // 4. Guardar en base de datos y devolver la respuesta con el ID autogenerado
        Medida medidaGuardada = medidaRepository.save(nuevaMedida);
        return ResponseEntity.ok(medidaGuardada);
    }

    /**
     * Endpoint para consultar el historial de medidas de un usuario en concreto.
     * Método HTTP: GET
     * URL: /api/medidas/usuario/{usuarioId}
     * * @param usuarioId Identificador único del usuario (recibido como variable en la ruta).
     * @return Lista de medidas ordenadas cronológicamente de forma descendente (las más recientes primero).
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Medida>> obtenerHistorial(@PathVariable Long usuarioId){
        // Utiliza un método derivado del repositorio para filtrar por usuario y ordenar directamente en la consulta SQL
        List<Medida> historial = medidaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);

        // Retorna la lista en formato JSON con un código HTTP 200 (OK)
        return ResponseEntity.ok(historial);
    }
}