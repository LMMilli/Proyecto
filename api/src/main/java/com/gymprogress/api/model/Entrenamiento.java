package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una sesión de entrenamiento completada por un usuario.
 * <p>
 * Actúa como la raíz del registro histórico. Agrupa la fecha, la duración, el usuario
 * que lo realizó y la lista detallada de los ejercicios ejecutados durante dicha sesión.
 * </p>
 */
@Entity // Define la clase como una entidad gestionada por JPA.
@Table(name = "entrenamiento") // Mapea la entidad a la tabla "entrenamiento" en la base de datos.
public class Entrenamiento {

    /**
     * Identificador único de la sesión de entrenamiento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora exactas en las que se registró el entrenamiento.
     * <p>
     * Se inicializa por defecto con la fecha y hora actuales del servidor al instanciar el objeto.
     * </p>
     */
    private LocalDateTime fecha = LocalDateTime.now();

    /**
     * Duración total de la sesión expresada en minutos.
     */
    private Integer duracionMinutos;

    /**
     * Usuario que ha realizado y registrado este entrenamiento.
     * <p>
     * @ManyToOne: Un usuario puede tener muchísimos entrenamientos a lo largo del tiempo.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id") // Crea una columna de clave foránea (FK) apuntando a la tabla usuarios.
    private Usuario usuario;

    /**
     * Rutina base que el usuario ha seguido durante esta sesión (opcional).
     * <p>
     * Si el usuario hace un entrenamiento "libre" o improvisado, este campo puede quedar nulo.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "rutina_id") // Clave foránea apuntando al catálogo global de rutinas.
    private Rutina rutina;

    /**
     * Lista de los ejercicios concretos que se realizaron durante la sesión.
     * <p>
     * Es una relación bidireccional gestionada por el atributo 'entrenamiento' en la clase EjercicioEntrenamiento.
     * Al tener cascade = CascadeType.ALL, cuando guardas este Entrenamiento, Hibernate guarda automáticamente
     * todos los ejercicios (y sus respectivas series) que contenga esta lista.
     * </p>
     */
    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EjercicioEntrenamiento> ejerciciosEntrenamiento;

    /**
     * Constructor por defecto vacío. Requerido por la especificación de JPA/Hibernate.
     */
    public Entrenamiento() {
    }

    /**
     * Constructor con parámetros básicos para facilitar la instanciación rápida.
     */
    public Entrenamiento(Long id, LocalDateTime fecha, Integer duracionMinutos, Usuario usuario, Rutina rutina) {
        this.id = id;
        this.fecha = fecha;
        this.duracionMinutos = duracionMinutos;
        this.usuario = usuario;
        this.rutina = rutina;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public List<EjercicioEntrenamiento> getEjerciciosEntrenamiento() {
        return ejerciciosEntrenamiento;
    }

    public void setEjerciciosEntrenamiento(List<EjercicioEntrenamiento> ejerciciosEntrenamiento) {
        this.ejerciciosEntrenamiento = ejerciciosEntrenamiento;
    }
}