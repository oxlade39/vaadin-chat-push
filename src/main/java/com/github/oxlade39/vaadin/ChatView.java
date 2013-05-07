package com.github.oxlade39.vaadin;

import com.github.oxlade39.chat.ChatSession;
import com.github.oxlade39.chat.event.SendMessageEvent;
import com.github.oxlade39.chat.event.UserJoinedEvent;
import com.github.oxlade39.chat.event.UserQuitEvent;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Subscription;
import rx.util.functions.Action1;

import java.util.ArrayList;
import java.util.List;

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
        parent.getSession().setAttribute(CustomVaadinServlet.USERNAME_PARAM, chatSession.getUsername());

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
        middle.setComponentAlignment(users, Alignment.TOP_RIGHT);

        bottom.addComponent(chatText);
        bottom.addComponent(submit);
        bottom.setExpandRatio(chatText, 8);
        bottom.setExpandRatio(submit, 2);

        messages.setSizeFull();
        users.setSizeFull();
        setExpandRatio(middle, 1);
        middle.setSizeFull();
        chatText.setSizeFull();
        submit.setSizeFull();
        bottom.setWidth("100%");

        parent.getContent().setSizeFull();
        setSizeFull();

        chatContainer.addContainerProperty(CHAT_MESSAGE, Label.class, null);
        userContainer.addContainerProperty(USER_ID, Label.class, null);

        messages.setContainerDataSource(chatContainer);
        users.setContainerDataSource(userContainer);

        final List<Subscription> modelSubscriptions = new ArrayList<>();
        modelSubscriptions.add(chatSession.joiningUsers().subscribe(runActionSafely(parent, new UserAddedAction(chatSession.getUsername()))));
        modelSubscriptions.add(chatSession.quittingUsers().subscribe(runActionSafely(parent, new UserQuitAction())));
        modelSubscriptions.add(chatSession.getMessages().subscribe(runActionSafely(parent, new NewMessageAction(chatSession.getUsername()))));

        chatText.addShortcutListener(new Button.ClickShortcut(submit, ShortcutAction.KeyCode.ENTER));
        chatText.addValidator(new StringLengthValidator("Must provide message", 1, Integer.MAX_VALUE, false));
        chatText.setValidationVisible(false);
        chatText.focus();

        addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent event) {
                chatSession.quit();
                for (Subscription modelSubscription : modelSubscriptions) {
                    modelSubscription.unsubscribe();
                }
            }
        });

        submit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                LOGGER.debug("Button clicked");
                if(chatText.isValid()) {
                    chatSession.send(chatText.getValue());
                    chatText.setValue("");
                    chatText.focus();
                }
            }
        });
    }

    private class UserAddedAction implements Action1<UserJoinedEvent> {
        private final String myUsername;

        public UserAddedAction(String myUsername) {
            this.myUsername = myUsername;
        }

        @Override
        public void call(UserJoinedEvent userJoinedEvent) {
            Object itemId = userContainer.addItem();
            String username = userJoinedEvent.getUsername();
            if (myUsername.equals(username)) {
                username = bold(username);
            }
            Label userLabel = new Label(span(username));
            userLabel.setContentMode(ContentMode.HTML);
            userContainer.getItem(itemId).getItemProperty(USER_ID).setValue(userLabel);
        }
    }

    private class UserQuitAction implements Action1<UserQuitEvent> {
        @Override
        public void call(UserQuitEvent userQuitEvent) {
            Object itemId = findByUsername(userQuitEvent.getUsername());
            if(itemId != null) {
                LOGGER.debug("{} quit, removing from view", userQuitEvent.getUsername());
                userContainer.removeItem(itemId);
            } else {
                LOGGER.warn("couldn't find user {} in view for quit event", userQuitEvent.getUsername());
            }
        }

        private Object findByUsername(String username) {
            for (Object itemId : userContainer.getItemIds()) {
                Object userId = userContainer.getItem(itemId).getItemProperty(USER_ID).getValue();
                if(username.equals(userId)) {
                    return itemId;
                }
            }
            return null;
        }
    }

    private class NewMessageAction implements Action1<SendMessageEvent> {
        private final String myUsername;

        private NewMessageAction(String myUsername) {
            this.myUsername = myUsername;
        }

        @Override
        public void call(SendMessageEvent newMessage) {
            Object itemId = chatContainer.addItem();
            String username = newMessage.getUsername();
            if(myUsername.equals(username)) {
                username = bold(username);
            }
            Label usernameLabel = new Label(span(username + ": " + newMessage.getMessage()));
            usernameLabel.setContentMode(ContentMode.HTML);
            chatContainer.getItem(itemId).getItemProperty(CHAT_MESSAGE).setValue(usernameLabel);
        }
    }

    private static String bold(String value) {
        return "<b>" + value + "</b>";
    }

    private static String span(String content) {
        return "<span>" + content + "</span>";
    }
}
