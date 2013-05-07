package com.github.oxlade39.chat;

import com.github.oxlade39.chat.event.SendMessageEvent;
import com.github.oxlade39.chat.event.UserChatEvent;
import com.github.oxlade39.chat.event.UserJoinedEvent;
import com.github.oxlade39.chat.event.UserQuitEvent;
import rx.Observable;
import rx.subjects.Subject;
import rx.util.functions.Action1;
import rx.util.functions.Func1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dan
 */
public class Chat {

    private final Subject<SendMessageEvent> messages = Subject.create();
    private final Subject<UserChatEvent> users = Subject.create();
    private final ConcurrentMap<String, ChatSession> sessions = new ConcurrentHashMap<>();

    public ChatSession join(String username) {

        Observable<UserChatEvent> nowAndFutureUsers = Observable.concat(existingUsers(), users);
        ChatSession newSession = new ChatSession(username,
                                                 nowAndFutureUsers,
                                                 this,
                                                 messages);
        ChatSession existingSession = sessions.putIfAbsent(username, newSession);

        if(existingSession == null) {
            users.onNext(new UserJoinedEvent(username));
        } else {
            throw new IllegalArgumentException(username + " already exists");
        }

        return newSession;
    }

    public void publishMessage(String username, String message) {
        messages.onNext(new SendMessageEvent(username, message));
    }

    public void quit(String username) {
        ChatSession removed = sessions.remove(username);
        if(removed != null) {
            users.onNext(new UserQuitEvent(username));
        }
    }

    private Observable<UserChatEvent> existingUsers() {
        return Observable.from(sessions.keySet()).map(userJoinedEvent());
    }

    private static Func1<String, UserChatEvent> userJoinedEvent() {
        return new Func1<String, UserChatEvent>() {
            @Override
            public UserChatEvent call(String username) {
                return new UserJoinedEvent(username);
            }
        };
    }

}
