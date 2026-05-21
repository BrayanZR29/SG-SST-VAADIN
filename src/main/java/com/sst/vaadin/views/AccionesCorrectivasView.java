package com.sst.vaadin.views;

import com.sst.modelo.AccionCorrectiva;
import com.sst.modelo.EstadoAccionCorrectiva;
import com.sst.modelo.Investigacion;
import com.sst.vaadin.Servicios;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "acciones", layout = MainLayout.class)
@PageTitle("Acciones Correctivas - SG-SST")
public class AccionesCorrectivasView extends VerticalLayout {

    private final Grid<AccionCorrectiva> grid = new Grid<>(AccionCorrectiva.class, false);
    private final ComboBox<String> filtroInvestigacion = new ComboBox<>("Investigacion");

    public AccionesCorrectivasView() {
        setSpacing(true);
        setPadding(true);

        add(new H2("Acciones Correctivas"));

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        filtroInvestigacion.setItems("Todas");
        filtroInvestigacion.setValue("Todas");
        filtroInvestigacion.addValueChangeListener(e -> filtrar());

        Button nuevaBtn = new Button("Nueva Accion", e -> abrirDialogoNueva());
        nuevaBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button refrescarBtn = new Button("Refrescar", e -> cargarAcciones());
        toolbar.add(filtroInvestigacion, refrescarBtn, nuevaBtn);
        add(toolbar);

        grid.addColumn(AccionCorrectiva::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(ac -> "Inv #" + ac.getInvestigacionId()).setHeader("Investigacion").setAutoWidth(true);
        grid.addColumn(AccionCorrectiva::getDescripcion).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(AccionCorrectiva::getResponsable).setHeader("Responsable").setAutoWidth(true);
        grid.addColumn(ac -> ac.getEstado().getDescripcion()).setHeader("Estado").setAutoWidth(true);
        grid.addColumn(ac -> ac.getFechaPlazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .setHeader("Fecha Plazo").setAutoWidth(true);
        grid.addColumn(ac -> ac.getFechaImplementacion() != null
            ? ac.getFechaImplementacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "")
            .setHeader("Implementada").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(ac -> {
            HorizontalLayout botones = new HorizontalLayout();
            if (ac.getEstado() == EstadoAccionCorrectiva.PENDIENTE) {
                Button iniciar = new Button("Iniciar", e -> cambiarEstado(ac, EstadoAccionCorrectiva.EN_PROCESO));
                iniciar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
                botones.add(iniciar);
            }
            if (ac.getEstado() == EstadoAccionCorrectiva.EN_PROCESO) {
                Button completar = new Button("Completar", e -> cambiarEstado(ac, EstadoAccionCorrectiva.COMPLETADA));
                completar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
                botones.add(completar);
            }
            if (ac.getEstado() == EstadoAccionCorrectiva.PENDIENTE
                || ac.getEstado() == EstadoAccionCorrectiva.EN_PROCESO) {
                Button cancelar = new Button("Cancelar", e -> cambiarEstado(ac, EstadoAccionCorrectiva.CANCELADA));
                cancelar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
                botones.add(cancelar);
            }
            return botones;
        })).setHeader("Acciones").setAutoWidth(true);

        grid.setWidthFull();
        grid.setHeight("500px");

        add(grid);
        cargarAcciones();
        cargarFiltros();
    }

    private void cargarAcciones() {
        List<AccionCorrectiva> acciones = Servicios.ACCION_CORRECTIVA.seleccionarTodos();
        grid.setItems(acciones);
    }

    private void cargarFiltros() {
        List<Investigacion> investigaciones = Servicios.INVESTIGACION.obtenerTodos();
        List<String> items = investigaciones.stream()
            .map(i -> "#" + i.getId() + " - Evento #" + i.getEventoId())
            .collect(Collectors.toList());
        items.add(0, "Todas");
        filtroInvestigacion.setItems(items);
    }

    private void filtrar() {
        String valor = filtroInvestigacion.getValue();
        if (valor == null || "Todas".equals(valor)) {
            cargarAcciones();
            return;
        }
        int invId = Integer.parseInt(valor.split(" - ")[0].replace("#", ""));
        List<AccionCorrectiva> acciones = Servicios.ACCION_CORRECTIVA.buscarPorInvestigacionId(invId);
        grid.setItems(acciones);
    }

    private void abrirDialogoNueva() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nueva Accion Correctiva");
        dialog.setWidth("550px");

        FormLayout form = new FormLayout();

        List<Investigacion> investigaciones = Servicios.INVESTIGACION.obtenerTodos();
        ComboBox<String> invField = new ComboBox<>("Investigacion");
        invField.setItems(investigaciones.stream()
            .map(i -> "#" + i.getId() + " - Evento #" + i.getEventoId())
            .collect(Collectors.toList()));
        invField.setRequired(true);
        invField.setWidthFull();

        TextArea descripcionField = new TextArea("Descripcion");
        descripcionField.setRequired(true);
        descripcionField.setWidthFull();
        form.setColspan(descripcionField, 2);

        TextField responsableField = new TextField("Responsable");
        responsableField.setRequired(true);

        DatePicker fechaPlazoField = new DatePicker("Fecha Plazo");
        fechaPlazoField.setRequired(true);

        TextArea observacionesField = new TextArea("Observaciones");
        observacionesField.setWidthFull();
        form.setColspan(observacionesField, 2);

        form.add(invField, descripcionField, responsableField, fechaPlazoField, observacionesField);

        HorizontalLayout actions = new HorizontalLayout();
        Button guardar = new Button("Guardar", e -> {
            try {
                if (invField.isEmpty() || descripcionField.isEmpty()
                    || responsableField.isEmpty() || fechaPlazoField.isEmpty()) {
                    Notification.show("Complete todos los campos obligatorios");
                    return;
                }

                int invId = Integer.parseInt(invField.getValue().split(" - ")[0].replace("#", ""));
                AccionCorrectiva accion = new AccionCorrectiva();
                accion.setInvestigacionId(invId);
                accion.setDescripcion(descripcionField.getValue());
                accion.setResponsable(responsableField.getValue());
                accion.setFechaPlazo(fechaPlazoField.getValue().atStartOfDay());
                accion.setEstado(EstadoAccionCorrectiva.PENDIENTE);
                accion.setObservaciones(observacionesField.getValue());

                Servicios.ACCION_CORRECTIVA.insertar(accion);
                Notification.show("Accion correctiva creada exitosamente");
                dialog.close();
                cargarAcciones();
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

    private void cambiarEstado(AccionCorrectiva ac, EstadoAccionCorrectiva nuevoEstado) {
        try {
            Optional<AccionCorrectiva> opt = Servicios.ACCION_CORRECTIVA.seleccionarPorId(ac.getId());
            if (opt.isPresent()) {
                AccionCorrectiva actualizada = opt.get();
                actualizada.setEstado(nuevoEstado);
                if (nuevoEstado == EstadoAccionCorrectiva.COMPLETADA) {
                    actualizada.setFechaImplementacion(LocalDateTime.now());
                }
                Servicios.ACCION_CORRECTIVA.actualizar(actualizada);
                Notification.show("Estado actualizado a: " + nuevoEstado.getDescripcion());
                cargarAcciones();
            }
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage());
        }
    }
}
