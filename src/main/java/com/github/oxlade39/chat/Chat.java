package com.github.oxlade39.chat;

import rx.Observable;
import rx.subjects.Subject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dan
 */
public class Chat {

    private final Subject<String> messages = Subject.create();
    private final Subject<String> users = Subject.create();
    private final ConcurrentMap<String, ChatSession> sessions = new ConcurrentHashMap<>();

    public ChatSession join(String username) {

        Observable<String> nowAndFutureUsers = Observable.concat(Observable.from(sessions.keySet()), users);
        ChatSession newSession = new ChatSession(username, nowAndFutureUsers, messages);
        ChatSession existingSession = sessions.putIfAbsent(username, newSession);

        if(existingSession == null) {
            users.onNext(username);
        } else {
            throw new IllegalArgumentException(username + " already exists");
        }

        return newSession;
    }

}
