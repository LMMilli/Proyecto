package com.gymprogress.api.dto;

import java.util.List;

public class EntrenamientoRequest {
    private Long usuarioId;
    private Long rutinaId;
    private Integer duracionMinutos;
    private List<SerieRequest> series;

    public EntrenamientoRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getRutinaId() {
        return rutinaId;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public List<SerieRequest> getSeries() {
        return series;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setRutinaId(Long rutinaId) {
        this.rutinaId = rutinaId;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setSeries(List<SerieRequest> series) {
        this.series = series;
    }
}
