package com.gymprogress.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "medidas")
public class Medida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha = LocalDate.now();
    private Double pesoCorporal;
    private Double porcentajeGrasa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Medida() {
    }

    public Medida(Long id, LocalDate fecha, Double pesoCorporal, Double porcentajeGrasa, Usuario usuario) {
        this.id = id;
        this.fecha = fecha;
        this.pesoCorporal = pesoCorporal;
        this.porcentajeGrasa = porcentajeGrasa;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Double getPesoCorporal() {
        return pesoCorporal;
    }

    public Double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setPesoCorporal(Double pesoCorporal) {
        this.pesoCorporal = pesoCorporal;
    }

    public void setPorcentajeGrasa(Double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
