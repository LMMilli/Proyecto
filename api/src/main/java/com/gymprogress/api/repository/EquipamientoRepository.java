package com.gymprogress.api.repository;

import com.gymprogress.api.model.Equipamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Equipamiento}.
 * <p>
 * Proporciona acceso a la tabla de materiales y máquinas del gimnasio.
 * Además de los métodos CRUD estándar, permite la búsqueda por nombre único.
 * </p>
 */
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    /**
     * Busca un equipamiento específico por su nombre exacto.
     * <p>
     * Al devolver un {@link Optional}, obligamos al programador a gestionar el caso
     * en el que el equipo no exista (evitando el NullPointerException).
     * Es ideal para validaciones antes de insertar duplicados.
     * </p>
     *
     * @param nombre El nombre del material (ej. "Mancuerna", "Barra Z").
     * @return Un Optional que contiene el {@link Equipamiento} si se encuentra, o vacío si no.
     */
    Optional<Equipamiento> findByNombre(String nombre);
}