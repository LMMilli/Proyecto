package com.gymprogress.api.controller;

import com.gymprogress.api.dto.ObjetivoRequest;
import com.gymprogress.api.model.Objetivo;
import com.gymprogress.api.repository.ObjetivioRepository;
import com.gymprogress.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
public class ObjetivoController {
    @Autowired
    private ObjetivioRepository objetivioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> crearObjetivo(@RequestBody ObjetivoRequest request){
        var usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()) return  ResponseEntity.badRequest().body("Usuario no ecntrado");

        Objetivo obj = new Objetivo();
        obj.setUsuario(usuarioOpt.get());
        obj.setTipo(request.getTipo());
        obj.setValorObjetivo(request.getValorObjetivo());
        obj.setFechaLimite(request.getFechaLimite());

        return ResponseEntity.ok(objetivioRepository.save(obj));
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Objetivo> obtenerPorUsuario(@PathVariable Long usuarioId){
        return objetivioRepository.findByUsuarioId(usuarioId);
    }
}
