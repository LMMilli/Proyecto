package com.gymprogress.api.repository;

import com.gymprogress.api.model.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Objetivo}.
 * <p>
 * Proporciona el acceso a la base de datos para gestionar las metas físicas de los usuarios,
 * permitiendo crear nuevos retos y consultar los existentes.
 * </p>
 */
public interface ObjetivioRepository extends JpaRepository<Objetivo, Long> {

    /**
     * Recupera la lista de todos los objetivos asociados a un usuario específico.
     * <p>
     * Genera automáticamente la consulta SQL:
     * {@code SELECT * FROM objetivos WHERE usuario_id = ?}
     * </p>
     *
     * @param usuarioId Identificador único del usuario.
     * @return Una lista de {@link Objetivo} que pertenecen al usuario indicado.
     */
    List<Objetivo> findByUsuarioId(Long usuarioId);
}