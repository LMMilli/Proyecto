package com.gymprogress.api.repository;

import com.gymprogress.api.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Serie}.
 * <p>
 * Además de las operaciones CRUD básicas, este repositorio incluye lógica para
 * realizar análisis de rendimiento histórico, como el cálculo de récords personales.
 * </p>
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

    /**
     * Calcula el Peso Máximo (Récord Personal) levantado por un usuario en un ejercicio concreto.
     * <p>
     * Utiliza una consulta JPQL que navega a través de las relaciones del modelo:
     * Serie -> EjercicioEntrenamiento -> Entrenamiento -> Usuario.
     * </p>
     * * @param usuarioId Identificador del usuario.
     * @param ejercicioId Identificador del ejercicio del que queremos el récord (ej. Press Banca).
     * @return El valor máximo encontrado en la columna peso, o null si no hay registros.
     */
    @Query("SELECT MAX(s.peso) FROM Serie s " +
            "WHERE s.ejercicioEntrenamiento.entrenamiento.usuario.id = :usuarioId " +
            "AND s.ejercicioEntrenamiento.ejercicio.id = :ejercicioId")
    Double obtenerRecordDePeso(@Param("usuarioId") Long usuarioId, @Param("ejercicioId") Long ejercicioId);
}