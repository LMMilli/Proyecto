package com.gymprogress.api.repository;

import com.gymprogress.api.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Rutina}.
 * <p>
 * Actúa como el punto de acceso a la tabla "rutinas", permitiendo almacenar y recuperar
 * las plantillas de ejercicios predefinidas.
 * </p>
 */
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    /**
     * Al heredar de JpaRepository, ya disponemos de:
     * - save(): Para crear o editar rutinas.
     * - findAll(): Para listar todas las rutinas disponibles.
     * - findById(): Para obtener una rutina específica y sus ejercicios asociados.
     * - deleteById(): Para eliminar rutinas.
     */
}