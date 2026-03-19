package com.gymprogress.api.repository;

import com.gymprogress.api.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    List<Rutina> findByUsuarioId(Long usuarioId);
}
