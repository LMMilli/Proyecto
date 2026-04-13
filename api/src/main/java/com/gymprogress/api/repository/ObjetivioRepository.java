package com.gymprogress.api.repository;

import com.gymprogress.api.model.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjetivioRepository extends JpaRepository<Objetivo, Long> {
    List<Objetivo> findByUsuarioId(Long usuarioId);
}
