package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.Chat;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 * @author dan
 */
public class CustomVaadinServletService extends VaadinServletService {
    private final Chat chat;

    public CustomVaadinServletService(VaadinServlet servlet,
                                      DeploymentConfiguration deploymentConfiguration,
                                      Chat chat) throws ServiceException {
        super(servlet, deploymentConfiguration);
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }


}
