package com.gymprogress.api.controller;

import com.gymprogress.api.dto.LoginRequest;
import com.gymprogress.api.model.Usuario;
import com.gymprogress.api.repository.UsuarioRepository;
import com.gymprogress.api.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Usuarios y Seguridad.
 * Se encarga del registro de nuevas cuentas y del proceso de autenticación (Login),
 * garantizando la encriptación de credenciales.
 */
@RestController // Define la clase como controlador de la API RESTful
@RequestMapping("/api/usuarios") // Ruta base para los endpoints de usuarios
public class UsuarioController {

    /* * Inyección de dependencia para acceder a la tabla de usuarios en la base de datos. */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para obtener la lista de todos los usuarios registrados.
     * Método HTTP: GET
     * URL: /api/usuarios
     * * @return Lista completa de usuarios. (Útil para fines de administración o pruebas).
     */
    @GetMapping
    public List<Usuario> findAll() {
        // Recupera todos los registros de la tabla usuarios
        return usuarioRepository.findAll();
    }

    /**
     * Endpoint para registrar un nuevo usuario en el sistema (Sign Up).
     * Método HTTP: POST
     * URL: /api/usuarios
     * * @param usuario Objeto Usuario con los datos enviados desde el formulario de registro.
     * @return El usuario guardado en la base de datos (con su ID generado).
     */
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        // 1. Extraer la contraseña en texto plano recibida desde la aplicación móvil
        String passwordLimpia = usuario.getPassword();

        // 2. Aplicar el algoritmo de cifrado (Hash) para no guardar contraseñas vulnerables
        String passwordCifrada = SecurityUtil.cifrarPassword(passwordLimpia);

        // 3. Sustituir la contraseña plana por la cifrada en el objeto usuario
        usuario.setPassword(passwordCifrada);

        // 4. Persistir el usuario de forma segura en la base de datos
        return usuarioRepository.save(usuario);
    }

    /**
     * Endpoint para autenticar a un usuario existente (Log In).
     * Método HTTP: POST
     * URL: /api/usuarios/login
     * * @param request Objeto DTO que contiene únicamente el email y la contraseña introducidos.
     * @return ResponseEntity con los datos del usuario si hay éxito (HTTP 200), o error de credenciales (HTTP 401).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){

        // 1. Buscar en la base de datos si existe algún usuario registrado con ese email exacto
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioOpt.isPresent()){
            Usuario usuarioBD = usuarioOpt.get();

            // 2. Cifrar la contraseña que ha introducido el usuario al intentar entrar
            String passwordCifrada = SecurityUtil.cifrarPassword(request.getPassword());

            // 3. Comparar si el Hash de la contraseña introducida coincide con el Hash guardado en la BD
            if(passwordCifrada.equals(usuarioBD.getPassword())){
                // Las credenciales son válidas: devolvemos el objeto usuario (para que la app móvil guarde su ID)
                return ResponseEntity.ok(usuarioBD);
            }
        }

        // 4. Si el email no existe o la contraseña no coincide, devolvemos un error 401 (No Autorizado)
        // Se usa un mensaje genérico por seguridad (para no dar pistas a posibles atacantes de qué ha fallado)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña incorrectos");
    }
}