package com.sst.servicio;

import com.sst.dao.InvestigacionDao;
import com.sst.dao.AccionCorrectivaDao;
import com.sst.modelo.Investigacion;
import com.sst.modelo.EstadoInvestigacion;
import com.sst.modelo.Evento;
import com.sst.modelo.EstadoEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class InvestigacionServicio {
    private static final Logger LOG = LoggerFactory.getLogger(InvestigacionServicio.class);
    private final InvestigacionDao investigacionDao;
    private final AccionCorrectivaDao accionCorrectivaDao;
    private final EventoServicio eventoServicio;

    public InvestigacionServicio() {
        this.investigacionDao = new InvestigacionDao();
        this.accionCorrectivaDao = new AccionCorrectivaDao();
        this.eventoServicio = new EventoServicio();
    }

    public Investigacion crearInvestigacion(int eventoId, int responsableId) {
        Optional<Evento> eventoOpt = eventoServicio.obtenerPorId(eventoId);
        if (eventoOpt.isEmpty()) {
            throw new IllegalArgumentException("Evento no encontrado: " + eventoId);
        }
        Evento evento = eventoOpt.get();
        if (evento.getEstado() == EstadoEvento.CERRADO) {
            throw new IllegalStateException("No se puede investigar un evento cerrado");
        }

        Optional<Investigacion> investigacionExistente = investigacionDao.buscarPorEventoId(eventoId);
        if (investigacionExistente.isPresent()) {
            throw new IllegalStateException("El evento ya tiene una investigacion asociada");
        }

        LOG.info("Creando investigacion para evento: {}", eventoId);
        Investigacion investigacion = new Investigacion(eventoId, responsableId);
        investigacionDao.insertar(investigacion);

        evento.setEstado(EstadoEvento.EN_PROCESO);
        evento.setResponsableId(responsableId);
        eventoServicio.actualizar(evento);

        LOG.info("Investigacion creada con exito para evento: {}", eventoId);
        return investigacion;
    }

    public Optional<Investigacion> obtenerPorId(Integer id) {
        LOG.debug("Obteniendo investigacion por id: {}", id);
        return investigacionDao.seleccionarPorId(id);
    }

    public Optional<Investigacion> obtenerPorEventoId(Integer eventoId) {
        LOG.debug("Obteniendo investigacion para evento: {}", eventoId);
        return investigacionDao.buscarPorEventoId(eventoId);
    }

    public List<Investigacion> obtenerTodos() {
        LOG.debug("Obteniendo todas las investigaciones");
        return investigacionDao.seleccionarTodos();
    }

    public List<Investigacion> obtenerPorEstado(EstadoInvestigacion estado) {
        LOG.debug("Buscando investigaciones por estado: {}", estado);
        return investigacionDao.buscarPorEstado(estado);
    }

    public void actualizar(Investigacion investigacion) {
        if (investigacion.getId() == null) {
            throw new IllegalArgumentException("El ID de investigacion es requerido");
        }
        validarInvestigacion(investigacion);
        LOG.info("Actualizando investigacion: {}", investigacion.getId());
        investigacionDao.actualizar(investigacion);
    }

    public Investigacion completarInvestigacion(Integer investigacionId, String causaInmediata, String causaBasica,
            String accionesPropuestas, String conclusion) {
        Optional<Investigacion> investigacionOpt = investigacionDao.seleccionarPorId(investigacionId);
        if (investigacionOpt.isEmpty()) {
            throw new IllegalArgumentException("Investigacion no encontrada: " + investigacionId);
        }
        Investigacion investigacion = investigacionOpt.get();
        if (investigacion.getEstado() == EstadoInvestigacion.CERRADA) {
            throw new IllegalStateException("La investigacion ya esta cerrada");
        }
        investigacion.setCausaInmediata(causaInmediata);
        investigacion.setCausaBasica(causaBasica);
        investigacion.setAccionesPropuestas(accionesPropuestas);
        investigacion.setConclusion(conclusion);
        investigacion.setEstado(EstadoInvestigacion.CERRADA);
        investigacion.setFechaCierre(LocalDateTime.now());
        investigacionDao.actualizar(investigacion);

        Optional<Evento> eventoOpt = eventoServicio.obtenerPorId(investigacion.getEventoId());
        if (eventoOpt.isPresent()) {
            Evento evento = eventoOpt.get();
            if (evento.getEstado() != EstadoEvento.CERRADO) {
                evento.setEstado(EstadoEvento.CERRADO);
                evento.setFechaActualizacion(LocalDateTime.now());
                eventoServicio.actualizar(evento);
            }
        }
        LOG.info("Investigacion {} completada y cerrada", investigacionId);
        return investigacion;
    }

    public void eliminar(Integer id) {
        Optional<Investigacion> investigacionOpt = investigacionDao.seleccionarPorId(id);
        if (investigacionOpt.isPresent()) {
            Investigacion investigacion = investigacionOpt.get();
            Optional<Evento> eventoOpt = eventoServicio.obtenerPorId(investigacion.getEventoId());
            if (eventoOpt.isPresent()) {
                Evento evento = eventoOpt.get();
                if (evento.getEstado() == EstadoEvento.EN_PROCESO) {
                    evento.setEstado(EstadoEvento.ABIERTO);
                    evento.setFechaActualizacion(LocalDateTime.now());
                    eventoServicio.actualizar(evento);
                }
            }
        }
        LOG.info("Eliminando investigacion: {}", id);
        investigacionDao.eliminar(id);
    }

    private void validarInvestigacion(Investigacion investigacion) {
        if (investigacion == null) {
            throw new IllegalArgumentException("La investigacion no puede ser null");
        }
        if (investigacion.getEventoId() == null) {
            throw new IllegalArgumentException("El ID del evento es requerido");
        }
        if (investigacion.getResponsableId() == null) {
            throw new IllegalArgumentException("El responsable es requerido");
        }
    }
}