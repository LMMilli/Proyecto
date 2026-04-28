package com.gymprogress.api.repository;

import com.gymprogress.api.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Ejercicio}.
 * <p>
 * Hereda de {@link JpaRepository}, lo que proporciona de forma automática métodos
 * estándar para el manejo de la base de datos:
 * <ul>
 * <li><b>save(entidad):</b> Para crear o actualizar ejercicios.</li>
 * <li><b>findAll():</b> Para obtener el catálogo completo.</li>
 * <li><b>findById(id):</b> Para buscar un ejercicio específico por su clave primaria.</li>
 * <li><b>deleteById(id):</b> Para eliminar ejercicios del catálogo.</li>
 * </ul>
 * </p>
 */
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    // Al ser un catálogo general, por ahora no requiere consultas personalizadas (Query Methods).
}