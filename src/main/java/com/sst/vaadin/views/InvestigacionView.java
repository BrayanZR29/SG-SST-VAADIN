package com.sst.vaadin.views;

import com.sst.modelo.Evento;
import com.sst.modelo.EstadoEvento;
import com.sst.modelo.Investigacion;
import com.sst.modelo.EstadoInvestigacion;
import com.sst.modelo.Usuario;
import com.sst.modelo.RolUsuario;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "investigaciones", layout = MainLayout.class)
@PageTitle("Investigaciones - SG-SST")
public class InvestigacionView extends VerticalLayout {

    private final Grid<Investigacion> grid = new Grid<>(Investigacion.class, false);

    public InvestigacionView() {
        setSpacing(true);
        setPadding(true);

        add(new H2("Investigaciones"));

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();

        Button nuevaBtn = new Button("Nueva Investigacion", e -> abrirDialogoNueva());
        nuevaBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refrescarBtn = new Button("Refrescar", e -> cargarInvestigaciones());
        toolbar.add(refrescarBtn, nuevaBtn);
        add(toolbar);

        grid.addColumn(Investigacion::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(inv -> "Evento #" + inv.getEventoId()).setHeader("Evento").setAutoWidth(true);
        grid.addColumn(inv -> "Usuario #" + inv.getResponsableId()).setHeader("Responsable").setAutoWidth(true);
        grid.addColumn(inv -> inv.getEstado().getDescripcion()).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(inv -> inv.getFechaInvestigacion()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setHeader("Fecha Creacion").setAutoWidth(true);
        grid.addColumn(inv -> inv.getFechaCierre() != null
            ? inv.getFechaCierre().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "")
            .setHeader("Fecha Cierre").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(inv -> {
            HorizontalLayout botones = new HorizontalLayout();
            Button detalle = new Button("Ver detalle", e -> abrirDialogoDetalle(inv));
            detalle.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            botones.add(detalle);

            if (inv.getEstado() == EstadoInvestigacion.ABIERTA) {
                Button completar = new Button("Completar", e -> abrirDialogoCompletar(inv));
                completar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
                botones.add(completar);
            }
            return botones;
        })).setHeader("Acciones").setAutoWidth(true);

        grid.setWidthFull();
        grid.setHeight("500px");

        add(grid);
        cargarInvestigaciones();
    }

    private void cargarInvestigaciones() {
        List<Investigacion> investigaciones = Servicios.INVESTIGACION.obtenerTodos();
        grid.setItems(investigaciones);
    }

    private void abrirDialogoNueva() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nueva Investigacion");
        dialog.setWidth("500px");

        FormLayout form = new FormLayout();

        List<Evento> eventosDisponibles = Servicios.EVENTO.obtenerTodos().stream()
            .filter(e -> e.getEstado() == EstadoEvento.ABIERTO)
            .toList();

        ComboBox<String> eventoField = new ComboBox<>("Evento");
        eventoField.setItems(eventosDisponibles.stream()
            .map(e -> "#" + e.getId() + " - " + e.getTipo().getDescripcion() + " - " + e.getDescripcion())
            .collect(Collectors.toList()));
        eventoField.setRequired(true);
        eventoField.setWidthFull();

        List<Usuario> responsables = Servicios.USUARIO.obtenerTodos().stream()
            .filter(u -> u.getActivo() && (u.getRol() == RolUsuario.ADMINISTRADOR
                || u.getRol() == RolUsuario.RESPONSABLE_SST))
            .toList();

        ComboBox<String> responsableField = new ComboBox<>("Responsable");
        responsableField.setItems(responsables.stream()
            .map(u -> "#" + u.getId() + " - " + u.getNombreCompleto() + " (" + u.getRol().getDescripcion() + ")")
            .collect(Collectors.toList()));
        responsableField.setRequired(true);
        responsableField.setWidthFull();

        form.add(eventoField, responsableField);

        HorizontalLayout actions = new HorizontalLayout();
        Button guardar = new Button("Crear Investigacion", e -> {
            try {
                if (eventoField.isEmpty() || responsableField.isEmpty()) {
                    Notification.show("Seleccione evento y responsable");
                    return;
                }

                int eventoId = Integer.parseInt(eventoField.getValue().split(" - ")[0].replace("#", ""));
                int responsableId = Integer.parseInt(responsableField.getValue().split(" - ")[0].replace("#", ""));

                Servicios.INVESTIGACION.crearInvestigacion(eventoId, responsableId);
                Notification.show("Investigacion creada exitosamente");
                dialog.close();
                cargarInvestigaciones();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        actions.add(guardar, cancelar);
        dialog.add(form, actions);
        dialog.open();
    }

    private void abrirDialogoDetalle(Investigacion inv) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Investigacion #" + inv.getId());
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();
        form.add(new Span("Evento: #" + inv.getEventoId()));
        form.add(new Span("Responsable ID: #" + inv.getResponsableId()));
        form.add(new Span("Estado: " + inv.getEstado().getDescripcion()));
        form.add(new Span("Fecha: " + inv.getFechaInvestigacion()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        form.add(new Span("Fecha Cierre: " + (inv.getFechaCierre() != null
            ? inv.getFechaCierre().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A")));

        if (inv.getCausaInmediata() != null) {
            TextArea ci = new TextArea("Causa Inmediata");
            ci.setValue(inv.getCausaInmediata());
            ci.setReadOnly(true);
            ci.setWidthFull();
            form.setColspan(ci, 2);
            form.add(ci);
        }
        if (inv.getCausaBasica() != null) {
            TextArea cb = new TextArea("Causa Basica");
            cb.setValue(inv.getCausaBasica());
            cb.setReadOnly(true);
            cb.setWidthFull();
            form.setColspan(cb, 2);
            form.add(cb);
        }
        if (inv.getAccionesPropuestas() != null) {
            TextArea ap = new TextArea("Acciones Propuestas");
            ap.setValue(inv.getAccionesPropuestas());
            ap.setReadOnly(true);
            ap.setWidthFull();
            form.setColspan(ap, 2);
            form.add(ap);
        }
        if (inv.getConclusion() != null) {
            TextArea conc = new TextArea("Conclusion");
            conc.setValue(inv.getConclusion());
            conc.setReadOnly(true);
            conc.setWidthFull();
            form.setColspan(conc, 2);
            form.add(conc);
        }

        Button cerrar = new Button("Cerrar", e -> dialog.close());
        dialog.add(form, cerrar);
        dialog.open();
    }

    private void abrirDialogoCompletar(Investigacion inv) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Completar Investigacion #" + inv.getId());
        dialog.setWidth("600px");

        FormLayout form = new FormLayout();

        TextArea causaInmediata = new TextArea("Causa Inmediata");
        causaInmediata.setWidthFull();
        causaInmediata.setRequired(true);
        form.setColspan(causaInmediata, 2);

        TextArea causaBasica = new TextArea("Causa Basica");
        causaBasica.setWidthFull();
        causaBasica.setRequired(true);
        form.setColspan(causaBasica, 2);

        TextArea accionesPropuestas = new TextArea("Acciones Propuestas");
        accionesPropuestas.setWidthFull();
        accionesPropuestas.setRequired(true);
        form.setColspan(accionesPropuestas, 2);

        TextArea conclusion = new TextArea("Conclusion");
        conclusion.setWidthFull();
        conclusion.setRequired(true);
        form.setColspan(conclusion, 2);

        form.add(causaInmediata, causaBasica, accionesPropuestas, conclusion);

        HorizontalLayout actions = new HorizontalLayout();
        Button guardar = new Button("Completar y Cerrar", e -> {
            try {
                if (causaInmediata.isEmpty() || causaBasica.isEmpty()
                    || accionesPropuestas.isEmpty() || conclusion.isEmpty()) {
                    Notification.show("Complete todos los campos");
                    return;
                }

                Servicios.INVESTIGACION.completarInvestigacion(
                    inv.getId(),
                    causaInmediata.getValue(),
                    causaBasica.getValue(),
                    accionesPropuestas.getValue(),
                    conclusion.getValue()
                );
                Notification.show("Investigacion completada y cerrada");
                dialog.close();
                cargarInvestigaciones();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        actions.add(guardar, cancelar);
        dialog.add(form, actions);
        dialog.open();
    }
}
