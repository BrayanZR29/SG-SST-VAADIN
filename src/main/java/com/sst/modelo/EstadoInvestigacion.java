package com.sst.modelo;

public enum EstadoInvestigacion {
    ABIERTA("Abierta"),
    CERRADA("Cerrada");

    private final String descripcion;

    EstadoInvestigacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}