package com.sst.vaadin;

import com.sst.servicio.UsuarioServicio;
import com.sst.servicio.EventoServicio;
import com.sst.servicio.InvestigacionServicio;
import com.sst.dao.AccionCorrectivaDao;

public final class Servicios {

    public static final UsuarioServicio USUARIO = new UsuarioServicio();
    public static final EventoServicio EVENTO = new EventoServicio();
    public static final InvestigacionServicio INVESTIGACION = new InvestigacionServicio();
    public static final AccionCorrectivaDao ACCION_CORRECTIVA = new AccionCorrectivaDao();

    private Servicios() {
    }
}
