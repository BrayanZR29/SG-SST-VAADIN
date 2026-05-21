package com.sst.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class UtilidadesDebug {
    private static final Logger LOG = LoggerFactory.getLogger(UtilidadesDebug.class);
    public static final boolean MODO_DEBUG = true;

    public static void logSql(String sql, Object... parametros) {
        if (MODO_DEBUG) {
            LOG.debug("SQL: {}", sql);
            if (parametros.length > 0) {
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < parametros.length; i++) {
                    params.append("[").append(i + 1).append("]=").append(parametros[i]).append(" ");
                }
                LOG.debug("Parametros: {}", params);
            }
        }
    }

    public static void logResultado(String operacion, int filasAfectadas) {
        if (MODO_DEBUG) {
            LOG.debug("{} - Filas afectadas: {}", operacion, filasAfectadas);
        }
    }

    public static void logTiempo(String operacion, long tiempoMs) {
        if (MODO_DEBUG) {
            if (tiempoMs > 1000) {
                LOG.warn("{} tardo {} ms (LENTO)", operacion, tiempoMs);
            } else {
                LOG.debug("{} tardo {} ms", operacion, tiempoMs);
            }
        }
    }

    public static void logError(String operacion, SQLException e) {
        LOG.error("{} - Error SQL: {} | Estado: {} | Codigo: {}",
            operacion, e.getMessage(), e.getSQLState(), e.getErrorCode(), e);
    }

    public static void logConexion(String operacion, Connection conn) {
        if (MODO_DEBUG) {
            try {
                LOG.debug("{} - Connection valid: {}, autoCommit: {}",
                    operacion, !conn.isClosed(), conn.getAutoCommit());
            } catch (SQLException e) {
                LOG.error("Error al validar conexion: {}", e.getMessage());
            }
        }
    }

    public static void verificarPool() {
        if (MODO_DEBUG) {
            try {
                if (PoolConexiones.getDataSource() != null) {
                    var ds = PoolConexiones.getDataSource();
                    LOG.info("=== POOL DEBUG ===");
                    LOG.info("Active: {}", ds.getHikariPoolMXBean().getActiveConnections());
                    LOG.info("Idle: {}", ds.getHikariPoolMXBean().getIdleConnections());
                    LOG.info("Waiting: {}", ds.getHikariPoolMXBean().getThreadsAwaitingConnection());
                    LOG.info("Total: {}", ds.getHikariPoolMXBean().getTotalConnections());
                    LOG.info("=================");
                } else {
                    LOG.warn("POOL no inicializado");
                }
            } catch (Exception e) {
                LOG.error("Error al verificar pool: {}", e.getMessage());
            }
        }
    }

    public static void inicioOperacion(String operacion) {
        if (MODO_DEBUG) {
            LOG.info(">>> INICIO: {}", operacion);
        }
    }

    public static void finOperacion(String operacion) {
        if (MODO_DEBUG) {
            LOG.info("<<< FIN: {}", operacion);
        }
    }

    public static void info(String mensaje) {
        if (MODO_DEBUG) {
            LOG.info("INFO: {}", mensaje);
        }
    }

    public static void debug(String mensaje) {
        if (MODO_DEBUG) {
            LOG.debug("DEBUG: {}", mensaje);
        }
    }
}