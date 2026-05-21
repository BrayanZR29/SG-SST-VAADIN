package com.sst;

import com.vaadin.flow.server.VaadinServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SgSstVaadinServlet extends VaadinServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SgSstVaadinServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOG.info("=== Inicializando VaadinServlet ===");
        LOG.info("ServletContext majorVersion={}, minorVersion={}",
            config.getServletContext().getMajorVersion(),
            config.getServletContext().getMinorVersion());
        LOG.info("Init parameters:");
        var params = config.getInitParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            LOG.info("  {} = {}", name, config.getInitParameter(name));
        }
        try {
            super.init(config);
            LOG.info("=== VaadinServlet inicializado exitosamente ===");
        } catch (Exception e) {
            LOG.error("=== Error en inicializacion de VaadinServlet ===", e);
            throw e;
        }
    }
}
