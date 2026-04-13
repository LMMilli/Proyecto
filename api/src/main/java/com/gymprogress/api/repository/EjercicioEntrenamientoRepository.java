package com.gymprogress.api.repository;

import com.gymprogress.api.model.EjercicioEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EjercicioEntrenamientoRepository extends JpaRepository<EjercicioEntrenamiento, Long> {
    List<EjercicioEntrenamiento> findByEntrenamientoId(Long entrenamientoId);
}
