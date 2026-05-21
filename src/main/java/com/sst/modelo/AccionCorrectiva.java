package com.sst.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class AccionCorrectiva {
    private Integer id;
    private Integer investigacionId;
    private String descripcion;
    private String responsable;
    private LocalDateTime fechaPlazo;
    private LocalDateTime fechaImplementacion;
    private EstadoAccionCorrectiva estado;
    private String observaciones;

    public AccionCorrectiva() {
    }

    public AccionCorrectiva(Integer investigacionId, String descripcion, String responsable, LocalDateTime fechaPlazo) {
        this.investigacionId = investigacionId;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.fechaPlazo = fechaPlazo;
        this.estado = EstadoAccionCorrectiva.PENDIENTE;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvestigacionId() {
        return investigacionId;
    }

    public void setInvestigacionId(Integer investigacionId) {
        this.investigacionId = investigacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getFechaPlazo() {
        return fechaPlazo;
    }

    public void setFechaPlazo(LocalDateTime fechaPlazo) {
        this.fechaPlazo = fechaPlazo;
    }

    public LocalDateTime getFechaImplementacion() {
        return fechaImplementacion;
    }

    public void setFechaImplementacion(LocalDateTime fechaImplementacion) {
        this.fechaImplementacion = fechaImplementacion;
    }

    public EstadoAccionCorrectiva getEstado() {
        return estado;
    }

    public void setEstado(EstadoAccionCorrectiva estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean estaVencida() {
        if (estado == EstadoAccionCorrectiva.COMPLETADA || estado == EstadoAccionCorrectiva.CANCELADA) {
            return false;
        }
        return fechaPlazo != null && fechaPlazo.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccionCorrectiva that = (AccionCorrectiva) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AccionCorrectiva{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", responsable='" + responsable + '\'' +
                ", estado=" + estado +
                '}';
    }
}