package com.gymprogress.api.controller;

import com.gymprogress.api.model.Equipamiento;
import com.gymprogress.api.repository.EquipamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipamiento")
public class EquipamientoController {
    @Autowired
    private EquipamientoRepository equipamientoRepository;

    @GetMapping
    public List<Equipamiento> findAll(){
        return equipamientoRepository.findAll();
    }
}
