package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.Chat;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dan
 */
public class CustomVaadinServlet extends VaadinServlet {

    @Override
    protected CustomVaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        CustomVaadinServletService service = new CustomVaadinServletService(this, deploymentConfiguration, new Chat());
        service.init();
        return service;
    }
}
