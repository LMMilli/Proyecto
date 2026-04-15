package com.gymprogress.api.controller;

import com.gymprogress.api.dto.AuthResponse;
import com.gymprogress.api.dto.LoginRequest;
import com.gymprogress.api.model.Usuario;
import com.gymprogress.api.repository.UsuarioRepository;
import com.gymprogress.api.utils.JwtUtil;
import com.gymprogress.api.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private JwtUtil jwtUtil;


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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

        // 1. Buscar en la base de datos si existe algún usuario registrado con ese email exacto
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioOpt.isPresent()){
            Usuario usuarioBD = usuarioOpt.get();

            System.out.println("--- DEBUG LOGIN ---");
            System.out.println("Email enviado: [" + request.getEmail() + "]");
            System.out.println("Password plana enviada: [" + request.getPassword() + "]");
            System.out.println("Hash en BD: [" + usuarioBD.getPassword() + "]");

            //Usamos el verivicado BCrypt
            boolean esCorrecto = SecurityUtil.verificarPassword(request.getPassword(), usuarioBD.getPassword());

            System.out.println("¿Coinciden?: " + esCorrecto);
            System.out.println("-------------------");


            if(esCorrecto){
                String tokenGenerado = jwtUtil.generarToken(usuarioBD.getEmail());
                AuthResponse respuesta = new AuthResponse(tokenGenerado, usuarioBD);
                return ResponseEntity.ok(respuesta);
            }
        }

        // 4. Si el email no existe o la contraseña no coincide, devolvemos un error 401 (No Autorizado)
        // Se usa un mensaje genérico por seguridad (para no dar pistas a posibles atacantes de qué ha fallado)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña incorrectos");
    }
}