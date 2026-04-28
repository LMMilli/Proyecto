package com.gymprogress.api.repository;

import com.gymprogress.api.model.EjercicioEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link EjercicioEntrenamiento}.
 * <p>
 * Proporciona los métodos necesarios para realizar operaciones CRUD sobre la tabla
 * que vincula los ejercicios con las sesiones de entrenamiento. Al extender de JpaRepository,
 * obtenemos automáticamente métodos como save(), findById() y delete().
 * </p>
 */
public interface EjercicioEntrenamientoRepository extends JpaRepository<EjercicioEntrenamiento, Long> {

    /**
     * Busca todos los registros de ejercicios realizados en una sesión de entrenamiento específica.
     * <p>
     * Este es un "Query Method" derivado: Spring Data JPA interpreta el nombre del método
     * y genera automáticamente la consulta SQL:
     * {@code SELECT * FROM ejercicio_entrenamiento WHERE entrenamiento_id = ?}
     * </p>
     *
     * @param entrenamientoId Identificador de la sesión de entrenamiento.
     * @return Una lista de {@link EjercicioEntrenamiento} asociados a esa sesión.
     */
    List<EjercicioEntrenamiento> findByEntrenamientoId(Long entrenamientoId);
}