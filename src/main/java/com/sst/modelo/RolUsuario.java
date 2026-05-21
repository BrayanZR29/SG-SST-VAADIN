package com.sst.modelo;

public enum RolUsuario {
    ADMINISTRADOR("Administrador"),
    RESPONSABLE_SST("Responsable SST"),
    JEFE_AREA("Jefe de Area"),
    TRABAJADOR("Trabajador");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}