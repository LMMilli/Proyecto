package com.gymprogress.api.repository;

import com.gymprogress.api.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    @Query("SELECT MAX(s.peso) FROM Serie s WHERE s.entrenamiento.usuario.id = :usuarioId AND s.ejercicio.id = :ejercicioId")
    Double obtenerRecordDePeso(@Param("usuarioId") Long usuarioId, @Param("ejercicioId") Long ejercicioId);
}
