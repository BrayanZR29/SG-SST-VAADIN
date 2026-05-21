package com.sst.dao;

import com.sst.modelo.Evento;
import com.sst.modelo.TipoEvento;
import com.sst.modelo.Gravedad;
import com.sst.modelo.EstadoEvento;
import com.sst.util.PoolConexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EventoDao extends DaoBase<Evento> {

    @Override
    protected String getSqlSeleccionarTodos() {
        return "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
               "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
               "FROM eventos ORDER BY fecha_hora DESC";
    }

    @Override
    protected String getSqlSeleccionarPorId() {
        return "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
               "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
               "FROM eventos WHERE id = ?";
    }

@Override
protected String getSqlInsertar() {
        return "INSERT INTO eventos (fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
               "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlActualizar() {
        return "UPDATE eventos SET fecha_hora = ?, lugar = ?, area = ?, responsable_id = ?, personas_involucradas = ?, " +
               "descripcion = ?, consecuencias = ?, tipo = ?, gravedad = ?, estado = ?, fecha_actualizacion = ? " +
               "WHERE id = ?";
    }

    @Override
    protected String getSqlEliminar() {
        return "DELETE FROM eventos WHERE id = ?";
    }

    @Override
    protected Evento mapearResultado(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        evento.setLugar(rs.getString("lugar"));
        evento.setArea(rs.getString("area"));
        evento.setResponsableId(rs.getObject("responsable_id", Integer.class));
        evento.setPersonasInvolucradas(rs.getString("personas_involucradas"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setConsecuencias(rs.getString("consecuencias"));
        evento.setTipo(TipoEvento.valueOf(rs.getString("tipo")));
        evento.setGravedad(Gravedad.valueOf(rs.getString("gravedad")));
        evento.setEstado(EstadoEvento.valueOf(rs.getString("estado")));
        evento.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        evento.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion") != null ? 
            rs.getTimestamp("fecha_actualizacion").toLocalDateTime() : null);
        evento.setUsuarioReportaId(rs.getObject("usuario_reporta_id", Integer.class));
        return evento;
    }

    @Override
    protected void asignarParametrosInsertar(PreparedStatement stmt, Evento evento) throws SQLException {
        stmt.setTimestamp(1, java.sql.Timestamp.valueOf(evento.getFechaHora()));
        stmt.setString(2, evento.getLugar());
        stmt.setString(3, evento.getArea());
        stmt.setObject(4, evento.getResponsableId());
        stmt.setString(5, evento.getPersonasInvolucradas());
        stmt.setString(6, evento.getDescripcion());
        stmt.setString(7, evento.getConsecuencias());
        stmt.setString(8, evento.getTipo().name());
        stmt.setString(9, evento.getGravedad().name());
        stmt.setString(10, evento.getEstado().name());
        stmt.setTimestamp(11, java.sql.Timestamp.valueOf(evento.getFechaCreacion()));
        stmt.setTimestamp(12, java.sql.Timestamp.valueOf(evento.getFechaActualizacion()));
        stmt.setObject(13, evento.getUsuarioReportaId());
    }

    @Override
    protected void asignarParametrosActualizar(PreparedStatement stmt, Evento evento) throws SQLException {
        stmt.setTimestamp(1, java.sql.Timestamp.valueOf(evento.getFechaHora()));
        stmt.setString(2, evento.getLugar());
        stmt.setString(3, evento.getArea());
        stmt.setObject(4, evento.getResponsableId());
        stmt.setString(5, evento.getPersonasInvolucradas());
        stmt.setString(6, evento.getDescripcion());
        stmt.setString(7, evento.getConsecuencias());
        stmt.setString(8, evento.getTipo().name());
        stmt.setString(9, evento.getGravedad().name());
        stmt.setString(10, evento.getEstado().name());
        stmt.setTimestamp(11, java.sql.Timestamp.valueOf(evento.getFechaActualizacion()));
        stmt.setInt(12, evento.getId());
    }

    public List<Evento> buscarPorEstado(EstadoEvento estado) {
        String sql = "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
                    "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
                    "FROM eventos WHERE estado = ? ORDER BY fecha_hora DESC";
        
        return buscarPorCampo(sql, "estado", estado.name());
    }

    public List<Evento> buscarPorTipo(TipoEvento tipo) {
        String sql = "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
                    "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
                    "FROM eventos WHERE tipo = ? ORDER BY fecha_hora DESC";
        
        return buscarPorCampo(sql, "tipo", tipo.name());
    }

    public List<Evento> buscarPorArea(String area) {
        String sql = "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
                    "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
                    "FROM eventos WHERE UPPER(area) LIKE ? ORDER BY fecha_hora DESC";
        
        return buscarPorCampoLike(sql, "area", area);
    }

    public List<Evento> buscarPorRangoFechas(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin) {
        String sql = "SELECT id, fecha_hora, lugar, area, responsable_id, personas_involucradas, descripcion, consecuencias, " +
                    "tipo, gravedad, estado, fecha_creacion, fecha_actualizacion, usuario_reporta_id " +
                    "FROM eventos WHERE fecha_hora BETWEEN ? AND ? ORDER BY fecha_hora DESC";
        
        List<Evento> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar por rango de fechas: {}", e.getMessage());
        }
        return resultados;
    }

    public long contarPorEstado(EstadoEvento estado) {
        return contarPorCampo("estado", estado.name());
    }

    public long contarPorTipo(TipoEvento tipo) {
        return contarPorCampo("tipo", tipo.name());
    }

    private List<Evento> buscarPorCampo(String sql, String campo, String valor) {
        List<Evento> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, valor);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar por {}: {}", campo, e.getMessage());
        }
        return resultados;
    }

    private List<Evento> buscarPorCampoLike(String sql, String campo, String valor) {
        List<Evento> resultados = new java.util.ArrayList<>();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + valor.toUpperCase() + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultados.add(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar por {}: {}", campo, e.getMessage());
        }
        return resultados;
    }

    public long contarPorCampo(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM eventos WHERE " + campo + " = ?";
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, valor);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al contar por {}: {}", campo, e.getMessage());
        }
        return 0;
    }
}