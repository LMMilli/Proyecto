package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "entrenamiento")
public class Entrenamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha = LocalDateTime.now();
    private Integer duracionMinutos;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rutina_id")
    private Rutina rutina;

    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series;

    public Entrenamiento() {
    }

    public Entrenamiento(Long id, LocalDateTime fecha, Integer duracionMinutos, Usuario usuario, Rutina rutina) {
        this.id = id;
        this.fecha = fecha;
        this.duracionMinutos = duracionMinutos;
        this.usuario = usuario;
        this.rutina = rutina;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
}
