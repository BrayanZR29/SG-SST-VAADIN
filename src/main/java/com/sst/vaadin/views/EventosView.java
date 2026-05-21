package com.sst.vaadin.views;

import com.sst.modelo.Evento;
import com.sst.modelo.TipoEvento;
import com.sst.modelo.Gravedad;
import com.sst.modelo.EstadoEvento;
import com.sst.modelo.Usuario;
import com.sst.vaadin.Servicios;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "eventos", layout = MainLayout.class)
@PageTitle("Eventos - SG-SST")
public class EventosView extends VerticalLayout {

    private final Grid<Evento> grid = new Grid<>(Evento.class, false);
    private final ComboBox<String> filtroEstado = new ComboBox<>("Estado");
    private final ComboBox<String> filtroTipo = new ComboBox<>("Tipo");

    public EventosView() {
        setSpacing(true);
        setPadding(true);

        add(new H2("Gestion de Eventos"));

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        filtroEstado.setItems("Todos", "Abierto", "En Proceso", "Cerrado");
        filtroEstado.setValue("Todos");
        filtroEstado.addValueChangeListener(e -> filtrar());

        filtroTipo.setItems("Todos", "Accidente", "Incidente", "Enfermedad Profesional");
        filtroTipo.setValue("Todos");
        filtroTipo.addValueChangeListener(e -> filtrar());

        Button nuevoBtn = new Button("Nuevo Evento", e -> abrirDialogoNuevo());
        nuevoBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refrescarBtn = new Button("Refrescar", e -> cargarEventos());

        toolbar.add(filtroEstado, filtroTipo, refrescarBtn, nuevoBtn);
        add(toolbar);

        grid.addColumn(Evento::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(e -> e.getTipo().getDescripcion()).setHeader("Tipo").setAutoWidth(true);
        grid.addColumn(e -> e.getGravedad().getDescripcion()).setHeader("Gravedad").setAutoWidth(true);
        grid.addColumn(e -> e.getEstado().getDescripcion()).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(e -> e.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .setHeader("Fecha").setAutoWidth(true);
        grid.addColumn(Evento::getDescripcion).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(Evento::getArea).setHeader("Area").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(evento -> {
            HorizontalLayout botones = new HorizontalLayout();
            if (evento.getEstado() == EstadoEvento.ABIERTO) {
                Button investigar = new Button("Investigar", e -> {
                    getUI().ifPresent(ui -> ui.navigate("investigaciones"));
                });
                investigar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
                botones.add(investigar);
            }
            if (evento.getEstado() != EstadoEvento.CERRADO) {
                Button cerrar = new Button("Cerrar", e -> cerrarEvento(evento));
                cerrar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
                botones.add(cerrar);
            }
            Button detalle = new Button("Detalle", e -> abrirDialogoDetalle(evento));
            detalle.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            botones.add(detalle);
            return botones;
        })).setHeader("Acciones").setAutoWidth(true);

        grid.setWidthFull();
        grid.setHeight("500px");

        add(grid);
        cargarEventos();
    }

    private void cargarEventos() {
        List<Evento> eventos = Servicios.EVENTO.obtenerTodos();
        grid.setItems(eventos);
    }

    private void filtrar() {
        List<Evento> eventos = Servicios.EVENTO.obtenerTodos();
        String estadoFiltro = filtroEstado.getValue();
        String tipoFiltro = filtroTipo.getValue();

        List<Evento> filtrados = eventos.stream()
            .filter(e -> "Todos".equals(estadoFiltro) || e.getEstado().getDescripcion().equals(estadoFiltro))
            .filter(e -> "Todos".equals(tipoFiltro) || e.getTipo().getDescripcion().equals(tipoFiltro))
            .toList();

        grid.setItems(filtrados);
    }

    private void abrirDialogoNuevo() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nuevo Evento");
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();

        ComboBox<String> tipoField = new ComboBox<>("Tipo");
        tipoField.setItems("Accidente", "Incidente", "Enfermedad Profesional");
        tipoField.setRequired(true);

        ComboBox<String> gravedadField = new ComboBox<>("Gravedad");
        gravedadField.setItems("Leve", "Grave", "Mortal");
        gravedadField.setRequired(true);

        TextField lugarField = new TextField("Lugar");
        lugarField.setWidthFull();

        TextField areaField = new TextField("Area");

        TextArea personasField = new TextArea("Personas Involucradas");

        TextArea descripcionField = new TextArea("Descripcion");
        descripcionField.setRequired(true);
        descripcionField.setWidthFull();

        TextArea consecuenciasField = new TextArea("Consecuencias");
        consecuenciasField.setWidthFull();

        form.add(tipoField, gravedadField, lugarField, areaField, personasField, descripcionField, consecuenciasField);
        form.setColspan(descripcionField, 2);
        form.setColspan(consecuenciasField, 2);

        HorizontalLayout actions = new HorizontalLayout();
        Button guardar = new Button("Guardar", e -> {
            try {
                if (descripcionField.isEmpty() || tipoField.isEmpty() || gravedadField.isEmpty()) {
                    Notification.show("Complete los campos obligatorios");
                    return;
                }

                Evento evento = new Evento();
                evento.setTipo(TipoEvento.valueOf(
                    tipoField.getValue().toUpperCase().replace(" ", "_")));
                evento.setGravedad(Gravedad.valueOf(
                    gravedadField.getValue().toUpperCase()));
                evento.setDescripcion(descripcionField.getValue());
                evento.setLugar(lugarField.getValue());
                evento.setArea(areaField.getValue());
                evento.setPersonasInvolucradas(personasField.getValue());
                evento.setConsecuencias(consecuenciasField.getValue());
                evento.setFechaHora(LocalDateTime.now());

                Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
                if (usuario != null) {
                    evento.setUsuarioReportaId(usuario.getId());
                }

                Servicios.EVENTO.registrar(evento);
                Notification.show("Evento registrado exitosamente");
                dialog.close();
                cargarEventos();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        actions.add(guardar, cancelar);
        dialog.add(form, actions);
        dialog.open();
    }

    private void abrirDialogoDetalle(Evento evento) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detalle del Evento #" + evento.getId());
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();
        form.add(new Span("Tipo: " + evento.getTipo().getDescripcion()));
        form.add(new Span("Gravedad: " + evento.getGravedad().getDescripcion()));
        form.add(new Span("Estado: " + evento.getEstado().getDescripcion()));
        form.add(new Span("Fecha: " + evento.getFechaHora()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        form.add(new Span("Lugar: " + (evento.getLugar() != null ? evento.getLugar() : "")));
        form.add(new Span("Area: " + (evento.getArea() != null ? evento.getArea() : "")));

        TextArea descArea = new TextArea("Descripcion");
        descArea.setValue(evento.getDescripcion());
        descArea.setReadOnly(true);
        descArea.setWidthFull();
        form.setColspan(descArea, 2);

        TextArea persArea = new TextArea("Personas Involucradas");
        persArea.setValue(evento.getPersonasInvolucradas() != null ? evento.getPersonasInvolucradas() : "");
        persArea.setReadOnly(true);
        persArea.setWidthFull();
        form.setColspan(persArea, 2);

        TextArea consArea = new TextArea("Consecuencias");
        consArea.setValue(evento.getConsecuencias() != null ? evento.getConsecuencias() : "");
        consArea.setReadOnly(true);
        consArea.setWidthFull();
        form.setColspan(consArea, 2);

        Button cerrar = new Button("Cerrar", e -> dialog.close());
        dialog.add(form, cerrar);
        dialog.open();
    }

    private void cerrarEvento(Evento evento) {
        try {
            Servicios.EVENTO.cerrarEvento(evento.getId());
            Notification.show("Evento #" + evento.getId() + " cerrado");
            cargarEventos();
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
        }
    }
}
