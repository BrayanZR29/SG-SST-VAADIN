package com.sst.dao;

import com.sst.modelo.Usuario;
import com.sst.modelo.RolUsuario;
import com.sst.util.PoolConexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioDao extends DaoBase<Usuario> {

    @Override
    protected String getSqlSeleccionarTodos() {
        return "SELECT id, nombre_usuario, contrasena, nombre_completo, correo, rol, activo, fecha_creacion FROM usuarios WHERE activo = true";
    }

    @Override
    protected String getSqlSeleccionarPorId() {
        return "SELECT id, nombre_usuario, contrasena, nombre_completo, correo, rol, activo, fecha_creacion FROM usuarios WHERE id = ?";
    }

    @Override
    protected String getSqlInsertar() {
        return "INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, correo, rol, activo, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSqlActualizar() {
        return "UPDATE usuarios SET nombre_usuario = ?, contrasena = ?, nombre_completo = ?, correo = ?, rol = ?, activo = ? WHERE id = ?";
    }

    @Override
    protected String getSqlEliminar() {
        return "UPDATE usuarios SET activo = false WHERE id = ?";
    }

    @Override
    protected Usuario mapearResultado(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setNombreCompleto(rs.getString("nombre_completo"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        return usuario;
    }

    @Override
    protected void asignarParametrosInsertar(PreparedStatement stmt, Usuario usuario) throws SQLException {
        stmt.setString(1, usuario.getNombreUsuario());
        stmt.setString(2, usuario.getContrasena());
        stmt.setString(3, usuario.getNombreCompleto());
        stmt.setString(4, usuario.getCorreo());
        stmt.setString(5, usuario.getRol().name());
        stmt.setBoolean(6, usuario.getActivo());
        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(usuario.getFechaCreacion()));
    }

    @Override
    protected void asignarParametrosActualizar(PreparedStatement stmt, Usuario usuario) throws SQLException {
        stmt.setString(1, usuario.getNombreUsuario());
        stmt.setString(2, usuario.getContrasena());
        stmt.setString(3, usuario.getNombreCompleto());
        stmt.setString(4, usuario.getCorreo());
        stmt.setString(5, usuario.getRol().name());
        stmt.setBoolean(6, usuario.getActivo());
        stmt.setInt(7, usuario.getId());
    }

    public Optional<Usuario> autenticar(String nombreUsuario, String contrasena) {
        String sql = "SELECT id, nombre_usuario, contrasena, nombre_completo, correo, rol, activo, fecha_creacion " +
                     "FROM usuarios WHERE nombre_usuario = ? AND contrasena = ? AND activo = true";
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LOG.debug("Usuario autenticado: {}", nombreUsuario);
                    return Optional.of(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al autenticar usuario: {}", e.getMessage());
        }
        LOG.warn("Autenticacion fallida para usuario: {}", nombreUsuario);
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id, nombre_usuario, contrasena, nombre_completo, correo, rol, activo, fecha_creacion " +
                     "FROM usuarios WHERE nombre_usuario = ? AND activo = true";
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultado(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Error al buscar usuario por nombre: {}", e.getMessage());
        }
        return Optional.empty();
    }
}