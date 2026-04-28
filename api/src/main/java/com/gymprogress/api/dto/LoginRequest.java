package com.gymprogress.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) para encapsular las credenciales de inicio de sesión.
 * <p>
 * Esta clase se utiliza para recibir y validar automáticamente los datos (email y contraseña)
 * que el usuario envía desde la pantalla de Login de la aplicación cliente antes de
 * procesarlos en el controlador.
 * </p>
 */
public class LoginRequest {

    /**
     * Correo electrónico del usuario.
     * <p>
     * @NotBlank: Asegura que el campo no venga nulo, ni vacío, ni contenga solo espacios en blanco.
     * @Email: Valida internamente que la cadena de texto tenga una estructura de correo válida (ej. usuario@dominio.com).
     * Si alguna de estas condiciones falla, Spring Boot devolverá automáticamente un error 400 (Bad Request)
     * con el mensaje especificado.
     * </p>
     */
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato de email es incorrecto")
    private String email;

    /**
     * Contraseña en texto plano introducida por el usuario.
     * <p>
     * Se valida que no esté vacía. Una vez en el servidor, se comparará con el hash de la base de datos.
     * </p>
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Constructor por defecto vacío.
     * Es obligatorio para que frameworks como Jackson (usado por Spring Boot) puedan
     * instanciar este objeto y volcar en él los datos del JSON recibido en la petición HTTP.
     */
    public LoginRequest() {
    }

    /**
     * Constructor con parámetros.
     * Útil para instanciar rápidamente el objeto desde el código (por ejemplo, al escribir tests unitarios).
     *
     * @param email    El email del usuario.
     * @param password La contraseña introducida.
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // --- Getters y Setters ---

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}