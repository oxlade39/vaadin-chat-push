package com.github.oxlade39.vaadin;

import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dan
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String VMC_APP_PORT = "VMC_APP_PORT";

    public static void main(String[] args) throws Exception {
        int port = resolvePort();
        LOGGER.debug("resolved port to {}", port);
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);

        ServletHolder servletHolder = new ServletHolder(new CustomVaadinServlet());
        servletHolder.setInitParameter(VaadinSession.UI_PARAMETER, View.class.getName());
        servletHolder.setInitParameter("pushmode", "automatic");
        servletHolder.setInitOrder(0);
        servletHolder.setAsyncSupported(true);
        context.addServlet(servletHolder,"/*");

        server.start();
        server.join();
    }

    private static int resolvePort() {
        String cloudfoundryPort = System.getenv().get(VMC_APP_PORT);
        String herokuPort = System.getenv("PORT");
        if(cloudfoundryPort != null) return Integer.parseInt(cloudfoundryPort);
        else if(herokuPort != null) return Integer.parseInt(herokuPort);
        else return 8080;
    }
}
