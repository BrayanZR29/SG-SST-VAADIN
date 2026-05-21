package com.sst.dao;

import com.sst.modelo.Investigacion;
import com.sst.modelo.EstadoInvestigacion;
import com.sst.util.PoolConexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InvestigacionDao extends DaoBase<Investigacion> {

    @Override
    protected String getSqlSeleccionarTodos() {
        return "SELECT id, evento_id, responsable_id, causa_inmediata, causa_basica, acciones_propuestas, conclusion, " +
               "fecha_investigacion, fecha_cierre, estado FROM investigaciones ORDER BY fecha_investigacion DESC";
    }

    @Override
    protected String getSqlSeleccionarPorId() {
        return "SELECT id, evento_id, responsable_id, causa_inmediata, causa_basica, acciones_propuestas, conclusion, " +
               "fecha_investigacion, fecha_cierre, estado FROM investigaciones WHERE id = ?";
    }

    @Override
    protected String getSqlInsertar() {
        return "INSERT INTO investigaciones (evento_id, responsable_id, causa_inmediata, causa_basica, acciones_propuestas, " +
               "conclusion, fecha_investigacion, fecha_cierre, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlActualizar() {
        return "UPDATE investigaciones SET evento_id = ?, responsable_id = ?, causa_inmediata = ?, causa_basica = ?, " +
               "acciones_propuestas = ?, conclusion = ?, fecha_investigacion = ?, fecha_cierre = ?, estado = ? WHERE id = ?";
    }

    @Override
    protected String getSqlEliminar() {
        return "DELETE FROM investigaciones WHERE id = ?";
    }

    @Override
    protected Investigacion mapearResultado(ResultSet rs) throws SQLException {
        Investigacion investigacion = new Investigacion();
        investigacion.setId(rs.getInt("id"));
        investigacion.setEventoId(rs.getInt("evento_id"));
        investigacion.setResponsableId(rs.getObject("responsable_id", Integer.class));
        investigacion.setCausaInmediata(rs.getString("causa_inmediata"));
        investigacion.setCausaBasica(rs.getString("causa_basica"));
        investigacion.setAccionesPropuestas(rs.getString("acciones_propuestas"));
        investigacion.setConclusion(rs.getString("conclusion"));
        investigacion.setFechaInvestigacion(rs.getTimestamp("fecha_investigacion").toLocalDateTime());
        investigacion.setFechaCierre(rs.getTimestamp("fecha_cierre") != null ? 
            rs.getTimestamp("fecha_cierre").toLocalDateTime() : null);
        investigacion.setEstado(EstadoInvestigacion.valueOf(rs.getString("estado")));
        return investigacion;
    }

    @Override
    protected void asignarParametrosInsertar(PreparedStatement stmt, Investigacion investigacion) throws SQLException {
        stmt.setInt(1, investigacion.getEventoId());
        stmt.setObject(2, investigacion.getResponsableId());
        stmt.setString(3, investigacion.getCausaInmediata());
        stmt.setString(4, investigacion.getCausaBasica());
        stmt.setString(5, investigacion.getAccionesPropuestas());
        stmt.setString(6, investigacion.getConclusion());
        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(investigacion.getFechaInvestigacion()));
        stmt.setTimestamp(8, investigacion.getFechaCierre() != null ? 
            java.sql.Timestamp.valueOf(investigacion.getFechaCierre()) : null);
        stmt.setString(9, investigacion.getEstado().name());
    }

    @Override
    protected void asignarParametrosActualizar(PreparedStatement stmt, Investigacion investigacion) throws SQLException {
        stmt.setInt(1, investigacion.getEventoId());
        stmt.setObject(2, investigacion.getResponsableId());
        stmt.setString(3, investigacion.getCausaInmediata());
        stmt.setString(4, investigacion.getCausaBasica());
        stmt.setString(5, investigacion.getAccionesPropuestas());
        stmt.setString(6, investigacion.getConclusion());
        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(investigacion.getFechaInvestigacion()));
        stmt.setTimestamp(8, investigacion.getFechaCierre() != null ? 
            java.sql.Timestamp.valueOf(investigacion.getFechaCierre()) : null);
        stmt.setString(9, investigacion.getEstado().name());
        stmt.setInt(10, investigacion.getId());
    }

    public Optional<Investigacion> buscarPorEventoId(Integer eventoId) {
        String sql = "SELECT id, evento_id, responsable_id, causa_inmediata, causa_basica, acciones_propuestas, conclusion, " +
                    "fecha_investigacion, fecha_cierre, estado FROM investigaciones WHERE evento_id = ?";
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setInt(1, eventoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar investigacion por evento: {}", e.getMessage());
        }
        return Optional.empty();
    }

    public List<Investigacion> buscarPorEstado(EstadoInvestigacion estado) {
        String sql = "SELECT id, evento_id, responsable_id, causa_inmediata, causa_basica, acciones_propuestas, conclusion, " +
                    "fecha_investigacion, fecha_cierre, estado FROM investigaciones WHERE estado = ? ORDER BY fecha_investigacion DESC";
        
        return buscarPorCampo(sql, "estado", estado.name());
    }

    private List<Investigacion> buscarPorCampo(String sql, String campo, String valor) {
        List<Investigacion> resultados = new java.util.ArrayList<>();
        
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
}