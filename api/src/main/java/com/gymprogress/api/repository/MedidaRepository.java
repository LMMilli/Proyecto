package com.gymprogress.api.repository;

import com.gymprogress.api.model.Medida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Medida}.
 * <p>
 * Se encarga de almacenar y recuperar los registros de composición corporal
 * (peso y grasa) de los usuarios. Proporciona acceso directo al historial
 * antropométrico del atleta.
 * </p>
 */
public interface MedidaRepository extends JpaRepository<Medida, Long> {

    /**
     * Recupera todas las mediciones de un usuario concreto, ordenadas de la más reciente a la más antigua.
     * <p>
     * Utiliza la convención de nombres de Spring Data JPA para generar la consulta:
     * {@code SELECT * FROM medidas WHERE usuario_id = ? ORDER BY fecha DESC}
     * </p>
     *
     * @param usuarioId Identificador del usuario cuyas medidas se desean consultar.
     * @return Lista de objetos {@link Medida} en orden cronológico descendente.
     */
    List<Medida> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}