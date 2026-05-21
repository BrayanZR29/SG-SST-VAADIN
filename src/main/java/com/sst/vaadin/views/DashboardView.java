package com.sst.vaadin.views;

import com.sst.modelo.Evento;
import com.sst.modelo.Usuario;
import com.sst.modelo.TipoEvento;
import com.sst.modelo.EstadoEvento;
import com.sst.modelo.Gravedad;
import com.sst.vaadin.Servicios;
import com.sst.servicio.EventoServicio;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard - SG-SST")
public class DashboardView extends VerticalLayout {

    private final EventoServicio eventoServicio = Servicios.EVENTO;

    public DashboardView() {
        Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        setSpacing(true);
        setPadding(true);

        add(new H2("Bienvenido, " + (usuario != null ? usuario.getNombreCompleto() : "")));

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);

        List<Evento> eventos = eventoServicio.obtenerTodos();
        Map<String, Long> porTipo = eventoServicio.obtenerEstadisticasPorTipo();
        Map<String, Long> porEstado = eventoServicio.obtenerEstadisticasPorEstado();

        long total = eventos.size();
        long abiertos = porEstado.getOrDefault("Abierto", 0L);
        long enProceso = porEstado.getOrDefault("En Proceso", 0L);
        long cerrados = porEstado.getOrDefault("Cerrado", 0L);

        cards.add(crearCard("Total Eventos", String.valueOf(total), "#1a237e"));
        cards.add(crearCard("Abiertos", String.valueOf(abiertos), "#e65100"));
        cards.add(crearCard("En Proceso", String.valueOf(enProceso), "#1565c0"));
        cards.add(crearCard("Cerrados", String.valueOf(cerrados), "#2e7d32"));

        add(cards);

        HorizontalLayout tipoCards = new HorizontalLayout();
        tipoCards.setWidthFull();
        tipoCards.setSpacing(true);
        for (Map.Entry<String, Long> entry : porTipo.entrySet()) {
            tipoCards.add(crearCard(entry.getKey(), String.valueOf(entry.getValue()), "#4a148c"));
        }
        add(tipoCards);

        add(new H2("Eventos Recientes"));

        Grid<Evento> grid = new Grid<>(Evento.class, false);
        grid.setItems(eventos.size() > 10 ? eventos.subList(0, 10) : eventos);
        grid.addColumn(Evento::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(e -> e.getTipo().getDescripcion()).setHeader("Tipo").setAutoWidth(true);
        grid.addColumn(e -> e.getGravedad().getDescripcion()).setHeader("Gravedad").setAutoWidth(true);
        grid.addColumn(e -> e.getEstado().getDescripcion()).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(e -> e.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setHeader("Fecha").setAutoWidth(true);
        grid.addColumn(Evento::getDescripcion).setHeader("Descripcion").setAutoWidth(true);
        grid.setWidthFull();

        add(grid);
    }

    private Div crearCard(String titulo, String valor, String color) {
        Div card = new Div();
        card.getStyle().set("background", color);
        card.getStyle().set("color", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("padding", "1.5rem");
        card.getStyle().set("text-align", "center");
        card.getStyle().set("flex", "1");
        card.getStyle().set("min-width", "150px");
        card.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)");

        Span tituloSpan = new Span(titulo);
        tituloSpan.getStyle().set("font-size", "0.9rem");
        tituloSpan.getStyle().set("opacity", "0.9");
        tituloSpan.getStyle().set("display", "block");

        Span valorSpan = new Span(valor);
        valorSpan.getStyle().set("font-size", "2.5rem");
        valorSpan.getStyle().set("font-weight", "bold");
        valorSpan.getStyle().set("display", "block");

        card.add(tituloSpan, valorSpan);
        return card;
    }
}
