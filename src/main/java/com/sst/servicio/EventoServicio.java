package com.sst.servicio;

import com.sst.dao.EventoDao;
import com.sst.modelo.Evento;
import com.sst.modelo.TipoEvento;
import com.sst.modelo.Gravedad;
import com.sst.modelo.EstadoEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventoServicio {
    private static final Logger LOG = LoggerFactory.getLogger(EventoServicio.class);
    private final EventoDao eventoDao;

    public EventoServicio() {
        this.eventoDao = new EventoDao();
    }

    public Evento registrar(Evento evento) {
        validarEvento(evento);
        LOG.info("Registrando nuevo evento: {} - {}", evento.getTipo(), evento.getFechaHora());
        evento.setFechaCreacion(LocalDateTime.now());
        evento.setFechaActualizacion(LocalDateTime.now());
        evento.setEstado(EstadoEvento.ABIERTO);
        eventoDao.insertar(evento);
        LOG.info("Evento registrado con exito");
        return evento;
    }

    public List<Evento> obtenerTodos() {
        LOG.debug("Obteniendo todos los eventos");
        return eventoDao.seleccionarTodos();
    }

    public Optional<Evento> obtenerPorId(Integer id) {
        LOG.debug("Obteniendo evento por id: {}", id);
        return eventoDao.seleccionarPorId(id);
    }

    public void actualizar(Evento evento) {
        if (evento.getId() == null) {
            throw new IllegalArgumentException("El ID del evento es requerido");
        }
        validarEvento(evento);
        LOG.info("Actualizando evento: {}", evento.getId());
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
    }

    public void eliminar(Integer id) {
        LOG.info("Eliminando evento: {}", id);
        eventoDao.eliminar(id);
    }

    public void cambiarEstado(Integer id, EstadoEvento nuevoEstado) {
        Optional<Evento> eventoOpt = eventoDao.seleccionarPorId(id);
        if (eventoOpt.isEmpty()) {
            throw new IllegalArgumentException("Evento no encontrado: " + id);
        }
        Evento evento = eventoOpt.get();
        EstadoEvento estadoAnterior = evento.getEstado();
        evento.setEstado(nuevoEstado);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
        LOG.info("Estado del evento {} cambiado de {} a {}", id, estadoAnterior, nuevoEstado);
    }

    public void cerrarEvento(Integer id) {
        Optional<Evento> eventoOpt = eventoDao.seleccionarPorId(id);
        if (eventoOpt.isEmpty()) {
            throw new IllegalArgumentException("Evento no encontrado: " + id);
        }
        Evento evento = eventoOpt.get();
        if (evento.getEstado() == EstadoEvento.CERRADO) {
            throw new IllegalStateException("El evento ya esta cerrado");
        }
        evento.setEstado(EstadoEvento.CERRADO);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
        LOG.info("Evento {} cerrado", id);
    }

    public List<Evento> buscarPorEstado(EstadoEvento estado) {
        LOG.debug("Buscando eventos por estado: {}", estado);
        return eventoDao.buscarPorEstado(estado);
    }

    public List<Evento> buscarPorTipo(TipoEvento tipo) {
        LOG.debug("Buscando eventos por tipo: {}", tipo);
        return eventoDao.buscarPorTipo(tipo);
    }

    public List<Evento> buscarPorArea(String area) {
        LOG.debug("Buscando eventos por area: {}", area);
        return eventoDao.buscarPorArea(area);
    }

    public List<Evento> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        LOG.debug("Buscando eventos entre {} y {}", fechaInicio, fechaFin);
        return eventoDao.buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    public Map<String, Long> obtenerEstadisticasPorTipo() {
        Map<String, Long> estadisticas = new HashMap<>();
        for (TipoEvento tipo : TipoEvento.values()) {
            long count = eventoDao.contarPorTipo(tipo);
            estadisticas.put(tipo.getDescripcion(), count);
            LOG.debug("Tipo {}: {} eventos", tipo, count);
        }
        return estadisticas;
    }

    public Map<String, Long> obtenerEstadisticasPorEstado() {
        Map<String, Long> estadisticas = new HashMap<>();
        for (EstadoEvento estado : EstadoEvento.values()) {
            long count = eventoDao.contarPorEstado(estado);
            estadisticas.put(estado.getDescripcion(), count);
            LOG.debug("Estado {}: {} eventos", estado, count);
        }
        return estadisticas;
    }

    public long contarPorGravedad(Gravedad gravedad) {
        return eventoDao.contarPorCampo("gravedad", gravedad.name());
    }

    private void validarEvento(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser null");
        }
        if (evento.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora es requerida");
        }
        if (evento.getDescripcion() == null || evento.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es requerida");
        }
        if (evento.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de evento es requerido");
        }
        if (evento.getGravedad() == null) {
            throw new IllegalArgumentException("La gravedad es requerida");
        }
    }
}