package com.sst.servicio;

import com.sst.dao.UsuarioDao;
import com.sst.modelo.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UsuarioServicio {
    private static final Logger LOG = LoggerFactory.getLogger(UsuarioServicio.class);
    private final UsuarioDao usuarioDao;

    public UsuarioServicio() {
        this.usuarioDao = new UsuarioDao();
    }

    public Optional<Usuario> autenticar(String nombreUsuario, String contrasena) {
        LOG.info("Intentando autenticar usuario: {}", nombreUsuario);
        Optional<Usuario> usuario = usuarioDao.autenticar(nombreUsuario, contrasena);
        if (usuario.isPresent()) {
            LOG.info("Autenticacion exitosa para: {}", nombreUsuario);
        }
        return usuario;
    }

    public List<Usuario> obtenerTodos() {
        LOG.debug("Obteniendo todos los usuarios");
        return usuarioDao.seleccionarTodos();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        LOG.debug("Obteniendo usuario por id: {}", id);
        return usuarioDao.seleccionarPorId(id);
    }

    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        LOG.debug("Buscando usuario: {}", nombreUsuario);
        return usuarioDao.buscarPorNombreUsuario(nombreUsuario);
    }

    public void registrar(Usuario usuario) {
        validarUsuario(usuario);
        LOG.info("Registrando nuevo usuario: {}", usuario.getNombreUsuario());
        usuarioDao.insertar(usuario);
    }

    public void actualizar(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El ID del usuario es requerido para actualizar");
        }
        validarUsuario(usuario);
        LOG.info("Actualizando usuario: {}", usuario.getId());
        usuarioDao.actualizar(usuario);
    }

    public void eliminar(Integer id) {
        LOG.info("Eliminando usuario: {}", id);
        usuarioDao.eliminar(id);
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioDao.buscarPorNombreUsuario(nombreUsuario).isPresent();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contrasena es requerida");
        }
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es requerido");
        }
    }
}