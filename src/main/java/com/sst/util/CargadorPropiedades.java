package com.sst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class CargadorPropiedades {
    private static final Logger LOG = LoggerFactory.getLogger(CargadorPropiedades.class);
    private static Properties propiedades;

    private CargadorPropiedades() {
    }

    public static void cargar(String nombreArchivo) {
        propiedades = new Properties();
        try (InputStream entrada = CargadorPropiedades.class.getClassLoader()
                .getResourceAsStream(nombreArchivo)) {
            if (entrada == null) {
                LOG.warn("No se encontro el archivo de propiedades: {}", nombreArchivo);
                return;
            }
            propiedades.load(entrada);
            LOG.info("Propiedades cargadas desde: {}", nombreArchivo);
        } catch (Exception e) {
            LOG.error("Error al cargar propiedades: {}", e.getMessage());
        }
    }

    public static String get(String clave) {
        return propiedades != null ? propiedades.getProperty(clave) : null;
    }

    public static String get(String clave, String valorDefecto) {
        return propiedades != null ? propiedades.getProperty(clave, valorDefecto) : valorDefecto;
    }

    public static Properties getPropiedades() {
        return propiedades;
    }
}