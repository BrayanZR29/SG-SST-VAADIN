package com.sst.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Evento {
    private Integer id;
    private LocalDateTime fechaHora;
    private String lugar;
    private String area;
    private Integer responsableId;
    private String personasInvolucradas;
    private String descripcion;
    private String consecuencias;
    private TipoEvento tipo;
    private Gravedad gravedad;
    private EstadoEvento estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Integer usuarioReportaId;

    public Evento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(Integer responsableId) {
        this.responsableId = responsableId;
    }

    public String getPersonasInvolucradas() {
        return personasInvolucradas;
    }

    public void setPersonasInvolucradas(String personasInvolucradas) {
        this.personasInvolucradas = personasInvolucradas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getConsecuencias() {
        return consecuencias;
    }

    public void setConsecuencias(String consecuencias) {
        this.consecuencias = consecuencias;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public Gravedad getGravedad() {
        return gravedad;
    }

    public void setGravedad(Gravedad gravedad) {
        this.gravedad = gravedad;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getUsuarioReportaId() {
        return usuarioReportaId;
    }

    public void setUsuarioReportaId(Integer usuarioReportaId) {
        this.usuarioReportaId = usuarioReportaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", fechaHora=" + fechaHora +
                ", lugar='" + lugar + '\'' +
                ", area='" + area + '\'' +
                ", tipo=" + tipo +
                ", gravedad=" + gravedad +
                ", estado=" + estado +
                '}';
    }
}