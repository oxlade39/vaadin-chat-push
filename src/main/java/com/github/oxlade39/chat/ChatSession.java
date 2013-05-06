package com.github.oxlade39.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.subjects.Subject;

/**
* @author dan
*/
public class ChatSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSession.class);

    private final String username;
    private final Observable<String> users;
    private final Subject<String> messages;

    public ChatSession(String username,
                       Observable<String> users,
                       Subject<String> messages) {
        this.username = username;
        this.users = users;
        this.messages = messages;
    }

    public Observable<String> getUsers() {
        return users;
    }

    public Observable<String> getMessages() {
        return messages;
    }

    public void send(String message) {
        LOGGER.debug("{} sent message {}", username, message);
        messages.onNext(username + ": " + message);
    }
}
