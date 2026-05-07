package com.gymprogress.api.controller;

import com.gymprogress.api.dto.EquipamientoResponse;
import com.gymprogress.api.model.Equipamiento;
import com.gymprogress.api.repository.EquipamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipamiento")
public class EquipamientoController {

    @Autowired
    private EquipamientoRepository equipamientoRepository;

    @GetMapping
    public List<EquipamientoResponse> findAll() {
        List<Equipamiento> equipos = equipamientoRepository.findAll();

        // Mapeamos la entidad a un DTO limpio para evitar bucles con Ejercicios
        return equipos.stream().map(equipo -> {
            EquipamientoResponse dto = new EquipamientoResponse();
            dto.setId(equipo.getId());
            dto.setNombre(equipo.getNombre());
            return dto;
        }).collect(Collectors.toList());
    }
}