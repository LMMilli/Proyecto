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
 * <p>
 * Se encarga del registro de nuevas cuentas (Sign Up) y del proceso de autenticación (Login),
 * garantizando la encriptación de credenciales (hash) y la emisión de tokens JWT para
 * asegurar las comunicaciones posteriores.
 * </p>
 */
@RestController // Define la clase como controlador de la API RESTful.
@RequestMapping("/api/usuarios") // Ruta base para todos los endpoints relacionados con usuarios.
public class UsuarioController {

    /**
     * Repositorio para acceder a las operaciones de la base de datos de la entidad Usuario.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Componente utilitario encargado de la generación y validación de los tokens JWT.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para registrar un nuevo usuario en el sistema (Sign Up).
     * <p>
     * Responde a peticiones HTTP POST en la ruta "/api/usuarios".
     * Intercepta la contraseña en texto plano, la encripta usando un algoritmo seguro
     * y guarda el usuario en la base de datos.
     * </p>
     *
     * @param usuario Objeto {@link Usuario} construido a partir del cuerpo de la petición.
     * @return El usuario guardado en la base de datos (con su ID generado).
     */
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        // 1. Extraer la contraseña en texto plano recibida desde el cliente (ej. aplicación móvil Android).
        String passwordLimpia = usuario.getPassword();

        // 2. Aplicar el algoritmo de cifrado (Hash) para no guardar contraseñas vulnerables en texto plano.
        String passwordCifrada = SecurityUtil.cifrarPassword(passwordLimpia);

        // 3. Sustituir la contraseña plana por la versión cifrada en el objeto usuario.
        usuario.setPassword(passwordCifrada);

        // 4. Persistir el usuario de forma segura en la base de datos.
        return usuarioRepository.save(usuario);
    }

    /**
     * Endpoint para autenticar a un usuario existente (Log In).
     * <p>
     * Responde a peticiones HTTP POST en la ruta "/api/usuarios/login".
     * Verifica las credenciales contra la base de datos y, si son correctas,
     * emite un token JWT para la sesión del usuario.
     * </p>
     *
     * @param request Objeto DTO {@link LoginRequest} que contiene únicamente el email y la contraseña introducidos. Validado mediante @Valid.
     * @return {@link ResponseEntity} con el token y datos del usuario (200 OK), o un error genérico de credenciales (401 Unauthorized).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

        // 1. Buscar en la base de datos si existe algún usuario registrado con ese email exacto.
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioOpt.isPresent()){
            Usuario usuarioBD = usuarioOpt.get();

            // Bloque de depuración (DEBUG) para comprobar por consola los datos entrantes frente a los de la BD.
            System.out.println("--- DEBUG LOGIN ---");
            System.out.println("Email enviado: [" + request.getEmail() + "]");
            System.out.println("Password plana enviada: [" + request.getPassword() + "]");
            System.out.println("Hash en BD: [" + usuarioBD.getPassword() + "]");

            // 2. Comprobar la contraseña. Usamos la utilidad (basada en BCrypt) que compara la contraseña plana
            // enviada en el request con el Hash seguro almacenado en la base de datos.
            boolean esCorrecto = SecurityUtil.verificarPassword(request.getPassword(), usuarioBD.getPassword());

            System.out.println("¿Coinciden?: " + esCorrecto);
            System.out.println("-------------------");

            if(esCorrecto){
                // 3. Si la contraseña es correcta, generamos un token JWT (JSON Web Token) asociado al email del usuario.
                String tokenGenerado = jwtUtil.generarToken(usuarioBD.getEmail());

                // Preparamos un DTO de respuesta que encapsula tanto el Token como la información básica del usuario.
                AuthResponse respuesta = new AuthResponse(tokenGenerado, usuarioBD);

                // Devolvemos el objeto con un estado 200 OK.
                return ResponseEntity.ok(respuesta);
            }
        }

        // 4. Si el email no existe en la BD o la contraseña no coincide, devolvemos un error 401 (No Autorizado).
        // Se usa un mensaje genérico por seguridad (para no dar pistas a posibles atacantes de qué parte ha fallado, si el email o la clave).
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña incorrectos");
    }
}