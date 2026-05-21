package com.sst;

import com.sst.util.PoolConexiones;
import com.sst.util.CargadorPropiedades;
import com.vaadin.base.devserver.startup.DevModeStartupListener;
import com.vaadin.flow.di.LookupInitializer;
import com.vaadin.flow.server.startup.LookupServletContainerInitializer;
import com.vaadin.flow.server.startup.RouteRegistryInitializer;
import com.vaadin.flow.server.startup.VaadinAppShellInitializer;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.info("========================================");
        LOG.info("  SISTEMA DE GESTION SST - INICIANDO");
        LOG.info("========================================");

        CargadorPropiedades.cargar("application.properties");
        String dbUrl = CargadorPropiedades.get("db.url", "jdbc:postgresql://localhost:5432/sst");
        String dbUsuario = CargadorPropiedades.get("db.usuario", "postgres");
        String dbContrasena = CargadorPropiedades.get("db.contrasena", "0619");

        System.setProperty("db.url", dbUrl);
        System.setProperty("db.usuario", dbUsuario);
        System.setProperty("db.contrasena", dbContrasena);

        PoolConexiones.inicializar();
        LOG.info("Conexion a base de datos establecida");

        int port = Integer.parseInt(CargadorPropiedades.get("server.port", "8080"));
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        // Jetty 12 no setea el ClassLoader automaticamente en modo embebido
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        
        // Configurar el resourceBase para archivos estáticos y frontend
        configureResourceBase(context);
        
        server.setHandler(context);

        ServletHolder holder = context.addServlet(SgSstVaadinServlet.class, "/*");
        holder.setInitParameter("productionMode", "false");
        holder.setInitParameter("pnpm.enable", "false");
        holder.setInitParameter("npm.enable", "true");
        holder.setAsyncSupported(true);

        context.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                try {
                    ServletContext ctx = sce.getServletContext();
                    Set<Class<?>> classes = new HashSet<>();
                    classes.add(LookupInitializer.class);

                    LOG.info("Inicializando Vaadin Lookup...");
                    new LookupServletContainerInitializer().onStartup(classes, ctx);

                    // Agregar todas las vistas con @Route para que RouteRegistryInitializer las registre
                    classes.add(com.sst.vaadin.views.LoginView.class);
                    classes.add(com.sst.vaadin.views.DashboardView.class);
                    classes.add(com.sst.vaadin.views.EventosView.class);
                    classes.add(com.sst.vaadin.views.InvestigacionView.class);
                    classes.add(com.sst.vaadin.views.AccionesCorrectivasView.class);
                    // AppShellConfigurator para VaadinAppShellInitializer
                    classes.add(com.sst.vaadin.AppConfig.class);

                    ServletContainerInitializer[] vaadinSCIs = {
                        new RouteRegistryInitializer(),
                        new VaadinAppShellInitializer(),
                    };
                    for (ServletContainerInitializer sci : vaadinSCIs) {
                        sci.onStartup(classes, ctx);
                    }

                    // Inicializar el DevModeStartupListener (tambien es ServletContextListener)
                    new DevModeStartupListener().contextInitialized(sce);

                    LOG.info("Vaadin inicializado correctamente");
                } catch (Exception e) {
                    LOG.error("Error initializando Vaadin", e);
                }
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                // No op
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Deteniendo servidor...");
            PoolConexiones.cerrar();
            try {
                server.stop();
            } catch (Exception e) {
                LOG.error("Error al detener el servidor", e);
            }
        }));

        server.start();
        LOG.info("========================================");
        LOG.info("  SG-SST corriendo en http://localhost:{}", port);
        LOG.info("  Para detener: Ctrl+C");
        LOG.info("========================================");
        server.join();
    }
    
    private static void configureResourceBase(ServletContextHandler context) {
        try {
            // Intentar usar el directorio target/classes (donde están los recursos compilados)
            File baseDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            // Si estamos en development mode, usar target/classes
            if (baseDir.getAbsolutePath().contains("target")) {
                context.setBaseResource(context.newResource(baseDir.getParentFile().getParentFile().toURI()));
                LOG.info("ResourceBase configurado en: {}", baseDir.getParentFile().getParentFile().getAbsolutePath());
            } else {
                // En modo JAR, el recurso debería estar dentro del JAR
                context.setBaseResource(context.newResource(baseDir.toURI()));
                LOG.info("ResourceBase configurado en: {}", baseDir.getAbsolutePath());
            }
        } catch (Exception e) {
            LOG.warn("No se pudo configurar resourceBase: {}", e.getMessage());
            try {
                // Fallback: usar el directorio actual
                File currentDir = new File(System.getProperty("user.dir"));
                context.setBaseResource(context.newResource(currentDir.toURI()));
                LOG.info("ResourceBase establecido en directorio actual: {}", currentDir.getAbsolutePath());
            } catch (Exception ex) {
                LOG.error("Error fatal configurando resourceBase", ex);
            }
        }
    }
}
