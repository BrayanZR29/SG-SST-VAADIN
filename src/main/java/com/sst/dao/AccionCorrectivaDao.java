package com.sst.dao;

import com.sst.modelo.AccionCorrectiva;
import com.sst.modelo.EstadoAccionCorrectiva;
import com.sst.util.PoolConexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccionCorrectivaDao extends DaoBase<AccionCorrectiva> {

    @Override
    protected String getSqlSeleccionarTodos() {
        return "SELECT id, investigacion_id, descripcion, responsable, fecha_plazo, fecha_implementacion, estado, observaciones " +
               "FROM acciones_correctivas ORDER BY fecha_plazo ASC";
    }

    @Override
    protected String getSqlSeleccionarPorId() {
        return "SELECT id, investigacion_id, descripcion, responsable, fecha_plazo, fecha_implementacion, estado, observaciones " +
               "FROM acciones_correctivas WHERE id = ?";
    }

    @Override
    protected String getSqlInsertar() {
        return "INSERT INTO acciones_correctivas (investigacion_id, descripcion, responsable, fecha_plazo, " +
               "fecha_implementacion, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlActualizar() {
        return "UPDATE acciones_correctivas SET investigacion_id = ?, descripcion = ?, responsable = ?, " +
               "fecha_plazo = ?, fecha_implementacion = ?, estado = ?, observaciones = ? WHERE id = ?";
    }

    @Override
    protected String getSqlEliminar() {
        return "DELETE FROM acciones_correctivas WHERE id = ?";
    }

    @Override
    protected AccionCorrectiva mapearResultado(ResultSet rs) throws SQLException {
        AccionCorrectiva accion = new AccionCorrectiva();
        accion.setId(rs.getInt("id"));
        accion.setInvestigacionId(rs.getInt("investigacion_id"));
        accion.setDescripcion(rs.getString("descripcion"));
        accion.setResponsable(rs.getString("responsable"));
        accion.setFechaPlazo(rs.getTimestamp("fecha_plazo").toLocalDateTime());
        accion.setFechaImplementacion(rs.getTimestamp("fecha_implementacion") != null ? 
            rs.getTimestamp("fecha_implementacion").toLocalDateTime() : null);
        accion.setEstado(EstadoAccionCorrectiva.valueOf(rs.getString("estado")));
        accion.setObservaciones(rs.getString("observaciones"));
        return accion;
    }

    @Override
    protected void asignarParametrosInsertar(PreparedStatement stmt, AccionCorrectiva accion) throws SQLException {
        stmt.setInt(1, accion.getInvestigacionId());
        stmt.setString(2, accion.getDescripcion());
        stmt.setString(3, accion.getResponsable());
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(accion.getFechaPlazo()));
        stmt.setTimestamp(5, accion.getFechaImplementacion() != null ? 
            java.sql.Timestamp.valueOf(accion.getFechaImplementacion()) : null);
        stmt.setString(6, accion.getEstado().name());
        stmt.setString(7, accion.getObservaciones());
    }

    @Override
    protected void asignarParametrosActualizar(PreparedStatement stmt, AccionCorrectiva accion) throws SQLException {
        stmt.setInt(1, accion.getInvestigacionId());
        stmt.setString(2, accion.getDescripcion());
        stmt.setString(3, accion.getResponsable());
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(accion.getFechaPlazo()));
        stmt.setTimestamp(5, accion.getFechaImplementacion() != null ? 
            java.sql.Timestamp.valueOf(accion.getFechaImplementacion()) : null);
        stmt.setString(6, accion.getEstado().name());
        stmt.setString(7, accion.getObservaciones());
        stmt.setInt(8, accion.getId());
    }

    public List<AccionCorrectiva> buscarPorInvestigacionId(Integer investigacionId) {
        String sql = "SELECT id, investigacion_id, descripcion, responsable, fecha_plazo, fecha_implementacion, estado, " +
                    "observaciones FROM acciones_correctivas WHERE investigacion_id = ? ORDER BY fecha_plazo ASC";
        
        List<AccionCorrectiva> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setInt(1, investigacionId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar acciones por investigacion: {}", e.getMessage());
        }
        return resultados;
    }

    public List<AccionCorrectiva> buscarPorEstado(EstadoAccionCorrectiva estado) {
        String sql = "SELECT id, investigacion_id, descripcion, responsable, fecha_plazo, fecha_implementacion, estado, " +
                    "observaciones FROM acciones_correctivas WHERE estado = ? ORDER BY fecha_plazo ASC";
        
        List<AccionCorrectiva> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar acciones por estado: {}", e.getMessage());
        }
        return resultados;
    }

    public List<AccionCorrectiva> buscarVencidas() {
        String sql = "SELECT id, investigacion_id, descripcion, responsable, fecha_plazo, fecha_implementacion, estado, " +
                    "observaciones FROM acciones_correctivas WHERE estado NOT IN ('COMPLETADA', 'CANCELADA') " +
                    "AND fecha_plazo < CURRENT_TIMESTAMP ORDER BY fecha_plazo ASC";
        
        List<AccionCorrectiva> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                resultados.add(mapearResultado(rs));
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar acciones vencidas: {}", e.getMessage());
        }
        return resultados;
    }
}