package com.gymprogress.api.repository;

import com.gymprogress.api.model.Medida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedidaRepository extends JpaRepository<Medida, Long> {

    List<Medida> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
