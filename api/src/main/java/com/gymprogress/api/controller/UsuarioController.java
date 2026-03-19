package com.gymprogress.api.controller;

import com.gymprogress.api.dto.LoginRequest;
import com.gymprogress.api.model.Ejercicio;
import com.gymprogress.api.model.Usuario;
import com.gymprogress.api.repository.UsuarioRepository;
import com.gymprogress.api.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        String passwordLimpia = usuario.getPassword();
        String passwordCifrada = SecurityUtil.cifrarPassword(passwordLimpia);
        usuario.setPassword(passwordCifrada);
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if(usuarioOpt.isPresent()){
            Usuario usuarioBD = usuarioOpt.get();

            String passwordCifrada = SecurityUtil.cifrarPassword(request.getPassword());

            if(passwordCifrada.equals(usuarioBD.getPassword())){
                return ResponseEntity.ok(usuarioBD);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña incorrectos");
    }
}
