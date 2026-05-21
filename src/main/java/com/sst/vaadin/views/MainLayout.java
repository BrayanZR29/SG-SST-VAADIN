package com.sst.vaadin.views;

import com.sst.modelo.Usuario;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private final Usuario usuario;

    public MainLayout() {
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);

        DrawerToggle toggle = new DrawerToggle();
        H2 titulo = new H2("SG-SST");
        titulo.getStyle().set("margin", "0");

        Span userInfo = new Span(usuario != null ? usuario.getNombreCompleto() : "");
        userInfo.getStyle().set("margin-right", "1rem");

        Button logoutBtn = new Button("Cerrar sesion", new Icon(VaadinIcon.SIGN_OUT), e -> {
            VaadinSession.getCurrent().setAttribute(Usuario.class, null);
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate("login");
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        logoutBtn.getStyle().set("margin-left", "auto");

        HorizontalLayout header = new HorizontalLayout(toggle, titulo, userInfo, logoutBtn);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(titulo);
        addToNavbar(header);

        Tabs tabs = crearTabs();
        addToDrawer(tabs);
    }

    private Tabs crearTabs() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setWidthFull();

        tabs.add(crearTab(VaadinIcon.DASHBOARD, "Dashboard", DashboardView.class));
        tabs.add(crearTab(VaadinIcon.FILE_TEXT, "Eventos", EventosView.class));
        tabs.add(crearTab(VaadinIcon.SEARCH, "Investigaciones", InvestigacionView.class));
        tabs.add(crearTab(VaadinIcon.CHECK_CIRCLE, "Acciones Correctivas", AccionesCorrectivasView.class));

        return tabs;
    }

    private Tab crearTab(VaadinIcon icono, String texto, Class<? extends Component> vista) {
        RouterLink link = new RouterLink();
        link.add(new Icon(icono), new Span("  " + texto));
        link.setRoute(vista);
        link.getStyle().set("display", "flex");
        link.getStyle().set("align-items", "center");
        link.getStyle().set("padding", "0.5rem 1rem");
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("color", "var(--lumo-body-text-color)");
        return new Tab(link);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute(Usuario.class) == null) {
            event.rerouteTo("login");
        }
    }
}
