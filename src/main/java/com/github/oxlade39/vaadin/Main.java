package com.github.oxlade39.vaadin;

import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author dan
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

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
}
