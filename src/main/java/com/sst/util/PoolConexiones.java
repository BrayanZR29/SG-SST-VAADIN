package com.sst.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PoolConexiones {
    private static final Logger LOG = LoggerFactory.getLogger(PoolConexiones.class);
    private static HikariDataSource dataSource;

    private PoolConexiones() {
    }

    public static void inicializar() {
        String url = System.getProperty("db.url");
        String usuario = System.getProperty("db.usuario");
        String contrasena = System.getProperty("db.contrasena");

        if (url == null || url.isEmpty()) {
            url = "jdbc:postgresql://localhost:5432/sst";
            usuario = "postgres";
            contrasena = "0619";
        }

        HikariConfig configuracion = new HikariConfig();
        configuracion.setJdbcUrl(url);
        configuracion.setUsername(usuario);
        configuracion.setPassword(contrasena);
        configuracion.setDriverClassName("org.postgresql.Driver");
        
        configuracion.setMaximumPoolSize(10);
        configuracion.setMinimumIdle(2);
        configuracion.setConnectionTimeout(30000);
        configuracion.setIdleTimeout(600000);
        configuracion.setMaxLifetime(1800000);
        
        configuracion.setAutoCommit(true);
        configuracion.setConnectionTestQuery("SELECT 1");

        dataSource = new HikariDataSource(configuracion);
        LOG.info("Pool de conexiones inicializado: {}", url);
    }

    public static Connection obtenerConexion() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("El pool de conexiones no ha sido inicializado");
        }
        return dataSource.getConnection();
    }

    public static void cerrar() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOG.info("Pool de conexiones cerrado");
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}