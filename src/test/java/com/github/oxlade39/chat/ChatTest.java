package com.github.oxlade39.chat;

import com.github.oxlade39.chat.event.UserJoinedEvent;
import com.github.oxlade39.chat.event.UserQuitEvent;
import org.junit.Test;
import rx.util.functions.Action1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    private static class EnqueueAction<T> implements Action1<T> {

        private final BlockingQueue<T> enqueued;

        public EnqueueAction() {
            this.enqueued = new LinkedBlockingQueue<>();
        }

        @Override
        public void call(T item) {
            enqueued.add(item);
        }

        public BlockingQueue<T> getEnqueued() {
            return enqueued;
        }

        public T next() {
            return enqueued.poll();
        }
    }
}
