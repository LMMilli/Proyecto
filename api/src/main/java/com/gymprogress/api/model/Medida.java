package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad que representa una medición de la composición corporal del usuario.
 * <p>
 * Se utiliza para registrar el progreso físico a lo largo del tiempo, almacenando
 * el peso y el porcentaje de grasa corporal en una fecha determinada.
 * </p>
 */
@Entity // Indica a Hibernate que esta clase debe mapearse a una tabla en la base de datos.
@Table(name = "medidas") // Especifica explícitamente que la tabla se llamará "medidas" (en plural).
public class Medida {

    /**
     * Identificador único de la medida.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El ID será generado automáticamente por la base de datos (Autoincremental).
    private Long id;

    /**
     * Fecha exacta en la que el usuario se tomó las medidas.
     * <p>
     * Se inicializa por defecto con la fecha actual del sistema. A diferencia de
     * Entrenamiento (que usa LocalDateTime), aquí se usa LocalDate porque normalmente
     * solo nos importa el día del pesaje, no la hora exacta.
     * </p>
     */
    private LocalDate fecha = LocalDate.now();

    /**
     * Peso corporal registrado en kilogramos (o la unidad de medida estándar de tu app).
     */
    private Double pesoCorporal;

    /**
     * Porcentaje de grasa corporal estimado (opcional).
     */
    private Double porcentajeGrasa;

    /**
     * Usuario al que le pertenecen estas medidas físicas.
     * <p>
     * @ManyToOne: Un usuario puede tener registradas cientos de medidas a lo largo de los meses.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id") // Define la clave foránea (FK) en la tabla "medidas" que apunta a la tabla "usuarios".
    private Usuario usuario;

    /**
     * Constructor por defecto vacío. Obligatorio para el correcto funcionamiento de JPA/Hibernate.
     */
    public Medida() {
    }

    /**
     * Constructor con parámetros. Útil para crear instancias rápidas en servicios o tests de forma cómoda.
     */
    public Medida(Long id, LocalDate fecha, Double pesoCorporal, Double porcentajeGrasa, Usuario usuario) {
        this.id = id;
        this.fecha = fecha;
        this.pesoCorporal = pesoCorporal;
        this.porcentajeGrasa = porcentajeGrasa;
        this.usuario = usuario;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getPesoCorporal() {
        return pesoCorporal;
    }

    public void setPesoCorporal(Double pesoCorporal) {
        this.pesoCorporal = pesoCorporal;
    }

    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}