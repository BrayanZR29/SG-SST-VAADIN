package com.sst.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Investigacion {
    private Integer id;
    private Integer eventoId;
    private Integer responsableId;
    private String causaInmediata;
    private String causaBasica;
    private String accionesPropuestas;
    private String conclusion;
    private LocalDateTime fechaInvestigacion;
    private LocalDateTime fechaCierre;
    private EstadoInvestigacion estado;
    private List<AccionCorrectiva> accionesCorrectivas;

    public Investigacion() {
        this.accionesCorrectivas = new ArrayList<>();
    }

    public Investigacion(Integer eventoId, Integer responsableId) {
        this.eventoId = eventoId;
        this.responsableId = responsableId;
        this.fechaInvestigacion = LocalDateTime.now();
        this.estado = EstadoInvestigacion.ABIERTA;
        this.accionesCorrectivas = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public Integer getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(Integer responsableId) {
        this.responsableId = responsableId;
    }

    public String getCausaInmediata() {
        return causaInmediata;
    }

    public void setCausaInmediata(String causaInmediata) {
        this.causaInmediata = causaInmediata;
    }

    public String getCausaBasica() {
        return causaBasica;
    }

    public void setCausaBasica(String causaBasica) {
        this.causaBasica = causaBasica;
    }

    public String getAccionesPropuestas() {
        return accionesPropuestas;
    }

    public void setAccionesPropuestas(String accionesPropuestas) {
        this.accionesPropuestas = accionesPropuestas;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public LocalDateTime getFechaInvestigacion() {
        return fechaInvestigacion;
    }

    public void setFechaInvestigacion(LocalDateTime fechaInvestigacion) {
        this.fechaInvestigacion = fechaInvestigacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public EstadoInvestigacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInvestigacion estado) {
        this.estado = estado;
    }

    public List<AccionCorrectiva> getAccionesCorrectivas() {
        return accionesCorrectivas;
    }

    public void setAccionesCorrectivas(List<AccionCorrectiva> accionesCorrectivas) {
        this.accionesCorrectivas = accionesCorrectivas;
    }

    public void agregarAccionCorrectiva(AccionCorrectiva accion) {
        this.accionesCorrectivas.add(accion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Investigacion that = (Investigacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Investigacion{" +
                "id=" + id +
                ", eventoId=" + eventoId +
                ", causaInmediata='" + causaInmediata + '\'' +
                ", estado=" + estado +
                '}';
    }
}