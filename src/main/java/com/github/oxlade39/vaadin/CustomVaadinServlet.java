package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.Chat;
import com.github.oxlade39.chat.Robot;
import com.vaadin.server.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dan
 */
public class CustomVaadinServlet extends VaadinServlet
        implements SessionDestroyListener {

    public static final String USERNAME_PARAM = CustomVaadinServlet.class.getName() + ".username";
    private Robot robot;

    @Override
    protected CustomVaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        Chat chat = new Chat();
        CustomVaadinServletService service = new CustomVaadinServletService(this, deploymentConfiguration, chat);
        service.init();
        robot = new Robot("WALL·E", chat);
        return service;
    }

    @Override
    public void destroy() {
        robot.quit();
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        String username = (String) event.getSession().getAttribute(USERNAME_PARAM);
        if(username != null && !username.isEmpty()) {
            ((CustomVaadinServletService)event.getService()).getChat().quit(username);
        }
    }
}
