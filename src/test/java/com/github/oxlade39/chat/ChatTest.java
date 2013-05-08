package com.github.oxlade39.chat;

import com.github.oxlade39.chat.event.SendMessageEvent;
import com.github.oxlade39.chat.event.UserJoinedEvent;
import com.github.oxlade39.chat.event.UserQuitEvent;
import com.github.oxlade39.rx.EnqueueAction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author dan
 */
public class ChatTest {

    @Test
    public void canJoinAChat() throws Exception {
        Chat chat = new Chat();
        ChatSession bobsSession = chat.join("bob");
        assertEquals("bob", bobsSession.getUsername());
    }

    @Test
    public void canQuitFromAChatSession() throws Exception {
        Chat chat = new Chat();
        ChatSession bobsSession = chat.join("bob");

        EnqueueAction<UserQuitEvent> results = new EnqueueAction<>();
        bobsSession.quittingUsers().subscribe(results);

        bobsSession.quit();

        assertEquals("bob", results.next().getUsername());
    }

    @Test
    public void notifiedOfAllUsersOnJoin() throws Exception {
        Chat chat = new Chat();
        ChatSession bobsSession = chat.join("bob");

        EnqueueAction<UserJoinedEvent> joinedUsers = new EnqueueAction<UserJoinedEvent>();
        bobsSession.joiningUsers().subscribe(joinedUsers);

        ChatSession briansSession = chat.join("brian");

        assertEquals("bob", joinedUsers.next().getUsername());
        assertEquals("brian", joinedUsers.next().getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void canNotJoinWithExistingUsername() throws Exception {
        Chat chat = new Chat();
        chat.join("bob");
        chat.join("bob");
    }

    @Test
    public void sendingAMessageWillPublishToSubscribers() throws Exception {
        Chat chat = new Chat();
        ChatSession sender = chat.join("sender");
        ChatSession receiver = chat.join("receiver");

        EnqueueAction<SendMessageEvent> messages = new EnqueueAction<SendMessageEvent>();
        receiver.getMessages().subscribe(messages);

        sender.send("publish");
        assertEquals("publish", messages.next().getMessage());
    }

}
