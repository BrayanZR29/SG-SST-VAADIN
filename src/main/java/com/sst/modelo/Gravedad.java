package com.sst.modelo;

public enum Gravedad {
    LEVE("Leve"),
    GRAVE("Grave"),
    MORTAL("Mortal");

    private final String descripcion;

    Gravedad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}