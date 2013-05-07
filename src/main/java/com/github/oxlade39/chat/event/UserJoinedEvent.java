package com.github.oxlade39.chat.event;

/**
 * @author dan
 */
public class UserJoinedEvent extends UserChatEvent {
    public UserJoinedEvent(String username) {
        super(username);
    }
}
