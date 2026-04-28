package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad que representa una meta u objetivo físico fijado por el usuario.
 * <p>
 * Mapea contra la tabla "objetivos" y almacena qué quiere lograr el usuario
 * (ej. levantar X peso, llegar a X kilos corporales), para cuándo y si ya lo ha conseguido.
 * </p>
 */
@Entity // Indica a JPA/Hibernate que esta clase es una entidad que se guardará en la base de datos.
@Table(name = "objetivos") // Especifica el nombre en plural para la tabla en SQL.
public class Objetivo {

    /**
     * Identificador único del objetivo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental gestionado por el motor de base de datos.
    private Long id;

    /**
     * Categoría o tipo de meta (ej. "PESO_CORPORAL", "FUERZA_PRESS_BANCA").
     * <p>
     * @Column(nullable = false): Fuerza a nivel de esquema SQL (NOT NULL) que todo objetivo
     * deba tener un tipo definido para que no haya registros inválidos.
     * </p>
     */
    @Column(nullable = false)
    private String tipo;

    /**
     * Valor numérico a alcanzar (los kilos a levantar, el peso a alcanzar, etc.).
     * <p>
     * También protegido con nullable = false, ya que un objetivo sin un valor a alcanzar carece de sentido.
     * </p>
     */
    @Column(nullable = false)
    private Double valorObjetivo;

    /**
     * Fecha límite fijada para cumplir la meta (opcional, el usuario podría querer un objetivo sin límite de tiempo).
     */
    private LocalDate fechaLimite;

    /**
     * Bandera (flag) que indica si el usuario ya ha superado esta meta.
     * <p>
     * Al inicializarse en 'false', cualquier objeto nuevo creado en Java y guardado
     * en la base de datos nacerá automáticamente como "No completado".
     * </p>
     */
    private boolean completado = false;

    /**
     * El usuario dueño de este objetivo.
     * <p>
     * @ManyToOne: Varios objetivos pueden pertenecer a un único perfil de usuario.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id") // Clave ajena (FK) apuntando a la tabla 'usuarios'.
    private Usuario usuario;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(Double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    /**
     * Fíjate que el getter de un boolean en Java por convención se llama "isAlgo()" en lugar de "getAlgo()".
     */
    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}