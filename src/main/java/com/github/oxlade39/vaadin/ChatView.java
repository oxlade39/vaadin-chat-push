package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.ChatSession;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.util.functions.Action1;

import static com.github.oxlade39.rx.Actions.runActionSafely;

/**
 * @author dan
 */
public class ChatView extends VerticalLayout {
    private static final String CHAT_MESSAGE = "chat message";
    private static final String USER_ID = "user id";
    private static Logger LOGGER = LoggerFactory.getLogger(ChatView.class);

    private final Table messages = new Table();
    private final Table users = new Table();
    private final TextField chatText = new TextField();
    private final Button submit = new Button("Send");
    private final Container chatContainer = new IndexedContainer();
    private final Container userContainer = new IndexedContainer();

    public ChatView(final ChatSession chatSession, final UI parent) {
        HorizontalLayout top = new HorizontalLayout();
        HorizontalLayout middle = new HorizontalLayout();
        HorizontalLayout bottom = new HorizontalLayout();
        addComponent(top);
        addComponent(middle);
        addComponent(bottom);

        top.addComponent(new Label("Vaadin Push Chat example"));

        middle.addComponent(messages);
        middle.addComponent(users);
        middle.setExpandRatio(messages, 8);
        middle.setExpandRatio(users, 2);
        messages.setSizeFull();
        users.setSizeFull();
        middle.setComponentAlignment(users, Alignment.TOP_RIGHT);
        middle.setWidth("100%");

        bottom.addComponent(chatText);
        bottom.addComponent(submit);
        bottom.setExpandRatio(chatText, 8);
        bottom.setExpandRatio(submit, 2);
        bottom.setSizeFull();

        chatText.setSizeFull();
        submit.setSizeFull();

        chatContainer.addContainerProperty(CHAT_MESSAGE, String.class, null);
        userContainer.addContainerProperty(USER_ID, String.class, null);

        messages.setContainerDataSource(chatContainer);
        users.setContainerDataSource(userContainer);

        chatSession.getUsers().subscribe(runActionSafely(parent, new UserAddedAction()));
        chatSession.getMessages().subscribe(runActionSafely(parent, new NewMessageAction()));

        chatText.addShortcutListener(new Button.ClickShortcut(submit, ShortcutAction.KeyCode.ENTER));
        chatText.addValidator(new StringLengthValidator("Must provide message", 1, Integer.MAX_VALUE, false));
        chatText.setValidationVisible(false);

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                LOGGER.debug("Button clicked");
                if(chatText.isValid()) {
                    chatSession.send(chatText.getValue());
                    chatText.setValue("");
                }
            }
        });
    }

    private class UserAddedAction implements Action1<String> {
        @Override
        public void call(String userAdded) {
            Object itemId = userContainer.addItem();
            userContainer.getItem(itemId).getItemProperty(USER_ID).setValue(userAdded);
        }
    }

    private class NewMessageAction implements Action1<String> {
        @Override
        public void call(String newMessage) {
            Object itemId = chatContainer.addItem();
            chatContainer.getItem(itemId).getItemProperty(CHAT_MESSAGE).setValue(newMessage);
        }
    }
}
