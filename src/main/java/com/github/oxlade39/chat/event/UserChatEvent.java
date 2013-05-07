package com.github.oxlade39.chat.event;

/**
 * @author dan
 */
public class UserChatEvent implements ChatEvent {
    private final String username;

    public UserChatEvent(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
