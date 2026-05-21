#!/bin/bash
# Script para ejecutar SG-SST (app web con Vaadin)
# Requisitos: Java 17+, Maven, Node.js (para compilar frontend Vaadin)

cd "$(dirname "$0")"

echo "========================================"
echo "  SG-SST - SISTEMA DE GESTION SST"
echo "========================================"

if [ "$1" = "dev" ]; then
    echo "Modo desarrollo: compilando e iniciando..."
    mvn clean compile vaadin:prepare-frontend exec:java
else
    echo "Compilando paquete (modo produccion)..."
    mvn clean package -DskipTests
    if [ $? -eq 0 ]; then
        echo "Iniciando servidor web..."
        java -jar target/sg-sst-1.0.0.jar
    else
        echo "Error de compilacion"
        exit 1
    fi
fi
