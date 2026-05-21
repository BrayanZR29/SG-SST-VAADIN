package com.sst.vaadin.views;

import com.sst.modelo.Usuario;
import com.sst.vaadin.Servicios;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.util.Optional;

@Route("login")
@PageTitle("Iniciar Sesion - SG-SST")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final TextField usuarioField;
    private final PasswordField contrasenaField;
    private final Button loginButton;
    private final Span errorLabel;

    public LoginView() {
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setSpacing(true);
        getStyle().set("background", "linear-gradient(135deg, #1a237e 0%, #0d47a1 100%)");

        VerticalLayout card = crearTarjetaLogin();
        add(card);

        usuarioField = new TextField("Nombre de usuario");
        contrasenaField = new PasswordField("Contrasena");
        loginButton = new Button("Iniciar sesion", e -> iniciarSesion());
        errorLabel = new Span();
        errorLabel.getStyle().set("color", "red");

        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();

        usuarioField.setWidthFull();
        contrasenaField.setWidthFull();

        card.add(
            new H1("SG-SST"),
            new Span("Sistema de Gestion de Seguridad y Salud en el Trabajo"),
            usuarioField,
            contrasenaField,
            loginButton,
            errorLabel
        );
    }

    private VerticalLayout crearTarjetaLogin() {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("400px");
        card.setMaxWidth("90%");
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("padding", "2rem");
        card.getStyle().set("box-shadow", "0 4px 24px rgba(0,0,0,0.15)");
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        return card;
    }

    private void iniciarSesion() {
        String usuario = usuarioField.getValue().trim();
        String contrasena = contrasenaField.getValue().trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            errorLabel.setText("Ingrese usuario y contrasena");
            return;
        }

        Optional<Usuario> usuarioOpt = Servicios.USUARIO.autenticar(usuario, contrasena);

        if (usuarioOpt.isPresent()) {
            VaadinSession.getCurrent().setAttribute(Usuario.class, usuarioOpt.get());
            getUI().ifPresent(ui -> ui.navigate(""));
        } else {
            errorLabel.setText("Credenciales invalidas");
            contrasenaField.clear();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute(Usuario.class) != null) {
            event.rerouteTo("");
        }
    }
}
