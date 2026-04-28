package com.gymprogress.api.repository;

import com.gymprogress.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Usuario}.
 * <p>
 * Es una pieza crítica para la seguridad del sistema, ya que permite la
 * localización de usuarios durante los procesos de login y validación de credenciales.
 * </p>
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario en la base de datos a través de su dirección de correo electrónico.
     * <p>
     * Se utiliza principalmente en el proceso de autenticación para comprobar si el
     * email introducido existe y, posteriormente, validar su contraseña.
     * </p>
     *
     * @param email Correo electrónico único del usuario.
     * @return Un {@link Optional} que contiene al usuario si existe, o vacío en caso contrario.
     */
    Optional<Usuario> findByEmail(String email);
}