package com.gymprogress.api.repository;

import com.gymprogress.api.model.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Entrenamiento}.
 * <p>
 * Se encarga de las operaciones CRUD sobre las sesiones de entrenamiento.
 * Permite recuperar el historial de actividad física de los usuarios de forma eficiente.
 * </p>
 */
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {

    /**
     * Recupera el historial completo de entrenamientos de un usuario,
     * ordenado desde el más reciente al más antiguo.
     * <p>
     * Spring Data JPA traduce este nombre de método a una consulta SQL similar a:
     * {@code SELECT * FROM entrenamiento WHERE usuario_id = ? ORDER BY fecha DESC}
     * </p>
     *
     * @param usuarioId El identificador único del usuario.
     * @return Una lista de {@link Entrenamiento} ordenada cronológicamente de forma descendente.
     */
    List<Entrenamiento> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

}