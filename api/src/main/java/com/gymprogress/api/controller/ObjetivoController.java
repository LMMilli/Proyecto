package com.gymprogress.api.controller;

import com.gymprogress.api.dto.ObjetivoRequest;
import com.gymprogress.api.model.Objetivo;
import com.gymprogress.api.repository.ObjetivioRepository;
import com.gymprogress.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de los objetivos de los usuarios.
 * <p>
 * Permite a los usuarios establecer nuevas metas (por ejemplo, alcanzar un peso concreto
 * o levantar cierto peso en un ejercicio) y consultar sus objetivos activos.
 * </p>
 */
@RestController // Indica que es un controlador REST y sus métodos devuelven datos (JSON).
@RequestMapping("/api/objetivos") // Ruta base para todos los endpoints de objetivos.
public class ObjetivoController {

    /**
     * Repositorio para persistir y consultar los objetivos en la base de datos.
     */
    @Autowired
    private ObjetivioRepository objetivioRepository;

    /**
     * Repositorio para acceder a los datos de los usuarios y validar su existencia.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para crear y registrar un nuevo objetivo para un usuario.
     * <p>
     * Responde a peticiones HTTP POST en la ruta "/api/objetivos".
     * </p>
     *
     * @param request Objeto DTO con los datos del objetivo (usuarioId, tipo, valorObjetivo, fechaLimite).
     * @return {@link ResponseEntity} con el objetivo guardado (200 OK) o un error (400 Bad Request) si el usuario no existe.
     */
    @PostMapping
    public ResponseEntity<?> crearObjetivo(@RequestBody ObjetivoRequest request){
        // 1. Validar si el usuario existe usando la palabra reservada 'var' (inferencia de tipos de Java)
        var usuarioOpt = usuarioRepository.findById(request.getUsuarioId());

        // Si el usuario no existe, se aborta la operación y se devuelve un error 400
        if(usuarioOpt.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        // 2. Instanciar la entidad Objetivo y mapear manualmente los datos del DTO
        Objetivo obj = new Objetivo();
        obj.setUsuario(usuarioOpt.get()); // Extraemos el usuario validado
        obj.setTipo(request.getTipo());
        obj.setValorObjetivo(request.getValorObjetivo());
        obj.setFechaLimite(request.getFechaLimite());

        // 3. Guardar el objetivo en la base de datos y devolverlo envuelto en un 200 OK
        return ResponseEntity.ok(objetivioRepository.save(obj));
    }

    /**
     * Endpoint para obtener la lista de objetivos de un usuario específico.
     * <p>
     * Responde a peticiones HTTP GET en la ruta "/api/objetivos/usuario/{usuarioId}".
     * </p>
     *
     * @param usuarioId ID del usuario del que se quieren consultar los objetivos (extraído de la URL).
     * @return Lista (List) de los objetivos asociados a ese usuario.
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<Objetivo> obtenerPorUsuario(@PathVariable Long usuarioId){
        // A diferencia de otros controladores, aquí devolvemos directamente la lista.
        // Spring Boot automáticamente lo envolverá en un 200 OK y lo serializará a JSON.
        return objetivioRepository.findByUsuarioId(usuarioId);
    }
}