package com.github.oxlade39.chat.event;

/**
 * @author dan
 */
public class UserQuitEvent extends UserChatEvent {
    public UserQuitEvent(String username) {
        super(username);
    }
}
