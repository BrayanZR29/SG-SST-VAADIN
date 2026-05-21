package com.sst.modelo;

public enum EstadoAccionCorrectiva {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoAccionCorrectiva(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}