package com.sst.dao;

import com.sst.util.PoolConexiones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DaoBase<T> {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected abstract String getSqlSeleccionarTodos();
    protected abstract String getSqlSeleccionarPorId();
    protected abstract String getSqlInsertar();
    protected abstract String getSqlActualizar();
    protected abstract String getSqlEliminar();
    protected abstract T mapearResultado(ResultSet resultSet) throws SQLException;
    protected abstract void asignarParametrosInsertar(PreparedStatement stmt, T entidad) throws SQLException;
    protected abstract void asignarParametrosActualizar(PreparedStatement stmt, T entidad) throws SQLException;

    public List<T> seleccionarTodos() {
        List<T> resultados = new ArrayList<>();
        String sql = getSqlSeleccionarTodos();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                resultados.add(mapearResultado(rs));
            }
            LOG.debug("Seleccionados {} registros", resultados.size());
        } catch (SQLException e) {
            LOG.error("Error al seleccionar todos: {}", e.getMessage());
            throw new RuntimeException("Error al seleccionar registros", e);
        }
        return resultados;
    }

    public Optional<T> seleccionarPorId(Integer id) {
        String sql = getSqlSeleccionarPorId();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LOG.debug("Registro encontrado con id: {}", id);
                    return Optional.of(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al seleccionar por id: {}", e.getMessage());
            throw new RuntimeException("Error al seleccionar registro por id", e);
        }
        LOG.debug("No se encontro registro con id: {}", id);
        return Optional.empty();
    }

public void insertar(T entidad) {
        String sql = getSqlInsertar();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
             
            asignarParametrosInsertar(stmt, entidad);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        LOG.debug("Registro insertado con id: {}", generatedKeys.getInt(1));
                    }
                }
            }
            LOG.info("Registro insertado exitosamente");
        } catch (SQLException e) {
            LOG.error("Error al insertar: {}", e.getMessage());
            throw new RuntimeException("Error al insertar registro", e);
        }
    }

    public void actualizar(T entidad) {
        String sql = getSqlActualizar();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            asignarParametrosActualizar(stmt, entidad);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOG.info("Registro actualizado exitosamente");
            } else {
                LOG.warn("No se actualizo ningun registro");
            }
        } catch (SQLException e) {
            LOG.error("Error al actualizar: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar registro", e);
        }
    }

    public void eliminar(Integer id) {
        String sql = getSqlEliminar();
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                LOG.info("Registro eliminado con id: {}", id);
            } else {
                LOG.warn("No se eliminó ningún registro con id: {}", id);
            }
        } catch (SQLException e) {
            LOG.error("Error al eliminar: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar registro", e);
        }
    }
}