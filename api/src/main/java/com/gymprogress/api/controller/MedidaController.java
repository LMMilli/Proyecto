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

@RestController
@RequestMapping("/api/medidas")
public class MedidaController {

    @Autowired
    private MedidaRepository medidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> registarMeidad(@RequestBody MedidaRequest request){
        Optional<Usuario> usuarioOpt =  usuarioRepository.findById(request.getUsuarioId());
        if(usuarioOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
        }

        Medida nuevaMedida = new Medida();
        nuevaMedida.setUsuario(usuarioOpt.get());
        nuevaMedida.setPesoCorporal(request.getPeso());
        nuevaMedida.setPorcentajeGrasa(request.getPorcentajeGrasa());
        nuevaMedida.setFecha(LocalDate.now());

        Medida medidaGuardada = medidaRepository.save(nuevaMedida);
        return ResponseEntity.ok(medidaGuardada);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Medida>> obtenerHistorial(@PathVariable Long usuarioId){
        List<Medida> historial = medidaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return ResponseEntity.ok(historial);
    }
}
