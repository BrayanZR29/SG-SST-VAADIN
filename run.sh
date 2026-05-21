#!/bin/bash
# Script para ejecutar SG-SST en Linux (Void Linux)

cd "$(dirname "$0")"

echo "========================================"
echo "  SG-SST - SISTEMA DE GESTION SST"
echo "========================================"

# 1. Buscar mvn
MVN=""
if command -v mvn &>/dev/null; then
    MVN="mvn"
else
    for DIR in /opt /usr/local /usr/share "$HOME" "$HOME/Downloads" "$HOME/Documents"; do
        for F in "$DIR"/apache-maven*/bin/mvn "$DIR"/maven*/bin/mvn "$DIR"/Maven*/bin/mvn; do
            if [ -x "$F" ]; then
                MVN="$F"
                echo "Maven encontrado en: $F"
                break 2
            fi
        done
    done
fi

if [ -z "$MVN" ]; then
    echo "No se encontro Maven. Instalalo con: sudo xbps-install apache-maven"
    exit 1
fi

# 2. Ejecutar
if [ "$1" = "prod" ]; then
    echo "Compilando modo produccion..."
    "$MVN" clean package -DskipTests && java -jar target/sg-sst-1.0.0.jar
else
    echo "Modo desarrollo: compilando e iniciando..."
    "$MVN" clean compile vaadin:prepare-frontend exec:java
fi
