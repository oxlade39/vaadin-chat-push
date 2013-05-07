package com.github.oxlade39.chat;

import com.github.oxlade39.chat.event.SendMessageEvent;
import com.github.oxlade39.chat.event.UserChatEvent;
import com.github.oxlade39.chat.event.UserJoinedEvent;
import com.github.oxlade39.chat.event.UserQuitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import static com.github.oxlade39.rx.Functions.castTo;
import static com.github.oxlade39.rx.Functions.instanceOf;

/**
* @author dan
*/
public class ChatSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSession.class);

    private final String username;
    private final Observable<UserChatEvent> users;
    private final Chat chat;
    private final Observable<SendMessageEvent> messages;

    public ChatSession(String username,
                       Observable<UserChatEvent> users,
                       Chat chat,
                       Observable<SendMessageEvent> messages) {
        this.username = username;
        this.users = users;
        this.chat = chat;
        this.messages = messages;
    }

    public Observable<UserJoinedEvent> joiningUsers() {
        return users.filter(instanceOf(UserJoinedEvent.class)).map(castTo(UserJoinedEvent.class));
    }

    public Observable<UserQuitEvent> quittingUsers() {
        return users.filter(instanceOf(UserQuitEvent.class)).map(castTo(UserQuitEvent.class));
    }

    public void send(String message) {
        LOGGER.debug("{} sent message {}", username, message);
        chat.publishMessage(username, message);
    }

    public void quit() {
        LOGGER.debug("{} quitting", username);
        chat.quit(username);
    }

    public String getUsername() {
        return username;
    }

    public Observable<SendMessageEvent> getMessages() {
        return messages;
    }
}
