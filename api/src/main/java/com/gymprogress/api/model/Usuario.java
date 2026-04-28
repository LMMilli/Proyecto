package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad que representa a un usuario registrado en la aplicación.
 * <p>
 * Mapea contra la tabla "usuarios" y almacena tanto los datos del perfil
 * (nombre, fecha de registro) como las credenciales de acceso (email y contraseña).
 * </p>
 */
@Entity // Define la clase como una entidad gestionada por JPA.
@Table(name = "usuarios") // Nombra la tabla como "usuarios" en la base de datos SQL.
public class Usuario {

    /**
     * Identificador único del usuario (Clave Primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental gestionado por la BD.
    private Long id;

    /**
     * Correo electrónico del usuario, utilizado como nombre de usuario para el inicio de sesión.
     * <p>
     * Se aplican dos restricciones cruciales a nivel de base de datos:
     * - unique = true: Evita que dos personas se registren con el mismo email.
     * - nullable = false: El email es un campo obligatorio.
     * </p>
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Contraseña del usuario (almacenada en formato Hash o cifrada, nunca en texto plano).
     * <p>
     * @JsonIgnore: Es una medida de seguridad fundamental. Evita que Jackson (el serializador de Spring)
     * incluya este campo al convertir el objeto a JSON. Así, la contraseña (incluso estando cifrada)
     * nunca sale del servidor hacia el cliente en las respuestas HTTP.
     * </p>
     */
    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    /**
     * Nombre de visualización o alias del usuario en la aplicación.
     */
    private String nombre;

    /**
     * Fecha en la que el usuario se registró en el sistema por primera vez.
     * <p>
     * Se inicializa automáticamente con la fecha de hoy al momento de crear la instancia.
     * </p>
     */
    private LocalDate fechaRegistro = LocalDate.now();

    /**
     * Constructor por defecto vacío. Obligatorio para JPA/Hibernate.
     */
    public Usuario() {
    }

    /**
     * Constructor con todos los parámetros.
     */
    public Usuario(Long id, String email, String password, String nombre, LocalDate fechaRegistro) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.fechaRegistro = fechaRegistro;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}