package com.github.oxlade39.chat.event;

/**
 * @author dan
 */
public class SendMessageEvent extends UserChatEvent {
    private final String message;

    public SendMessageEvent(String sender, String message) {
        super(sender);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
