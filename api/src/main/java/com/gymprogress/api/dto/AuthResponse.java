package com.gymprogress.api.dto;

import com.gymprogress.api.model.Usuario;

/**
 * Data Transfer Object (DTO) para la respuesta de autenticación.
 * <p>
 * Esta clase sirve como un "paquete" o "envoltorio" para estructurar la respuesta JSON
 * que se envía al cliente cuando el login se realiza con éxito. Combina en un solo objeto
 * el token de seguridad y los datos del perfil del usuario.
 * </p>
 */
public class AuthResponse {

    /**
     * Token JWT (JSON Web Token) generado para autorizar las futuras peticiones del usuario.
     */
    private String token;

    /**
     * Entidad con los datos básicos del usuario que acaba de iniciar sesión.
     */
    private Usuario usuario;

    /**
     * Constructor principal de la respuesta de autenticación.
     *
     * @param token   El token de seguridad generado por el servidor.
     * @param usuario El objeto con los datos del usuario validado en la base de datos.
     */
    public AuthResponse(String token, Usuario usuario){
        this.token = token;
        this.usuario = usuario;
    }

    /**
     * Obtiene el token JWT.
     * * @return String con el token de acceso.
     */
    public String getToken() {
        return token;
    }

    /**
     * Obtiene los datos del usuario autenticado.
     * * @return Objeto {@link Usuario}.
     */
    public Usuario getUsuario() {
        return usuario;
    }
}