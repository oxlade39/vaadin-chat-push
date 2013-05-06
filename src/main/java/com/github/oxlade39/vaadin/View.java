package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.Chat;
import com.github.oxlade39.chat.ChatSession;
import com.vaadin.annotations.Title;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import static com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.ui.Button.ClickShortcut;

/**
 * @author dan
 */
@Title("Welcome")
public class View extends UI {

    private final Label label = new Label("User name");
    private final TextField username = new TextField("Username:");
    private final Button submit = new Button("Sign in");

    @Override
    protected void init(final VaadinRequest request) {

        VerticalLayout root=new VerticalLayout();
        root.setSizeFull();

        label.setSizeUndefined();


        root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout loginForm = new VerticalLayout();
        loginForm.addComponent(label);
        loginForm.addComponent(username);
        username.addValidator(new StringLengthValidator("Username must be between 1 and 20 characters", 1, 20, false));
        username.setValidationVisible(false);
        loginForm.addComponent(submit);
        loginForm.setSizeUndefined();

        root.addComponent(loginForm);
        root.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

        setContent(root);

        final Chat chat = ((CustomVaadinServletService) request.getService()).getChat();

        final UI parent = this;

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(username.isValid()) {
                    String submittedUsername = username.getValue();
                    ChatSession chatSession = chat.join(submittedUsername);
                    setContent(new ChatView(chatSession, parent));
                }
            }
        });
        username.addShortcutListener(new ClickShortcut(submit, KeyCode.ENTER));

    }

}
