package com.github.oxlade39.chat;

import com.github.oxlade39.chat.event.UserJoinedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Subscription;
import rx.util.functions.Action1;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dan
 */
public class Robot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Robot.class);

    private final ChatSession chatSession;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Subscription helloSubscription;

    public Robot(String username,
                 Chat chat) {
        chatSession = chat.join(username);
        helloSubscription = welcomeNewUsers();
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleLonelyMessage();
    }

    public void quit() {
        LOGGER.info("Robot is quitting");
        chatSession.quit();
        scheduledExecutorService.shutdown();
        helloSubscription.unsubscribe();
    }

    private void scheduleLonelyMessage() {
        scheduledExecutorService.scheduleAtFixedRate(new SendLonelyMessage(), 1, 20, TimeUnit.SECONDS);
    }

    private Subscription welcomeNewUsers() {
        return chatSession.joiningUsers().subscribe(new WelcomeNewUser());
    }

    private class SendLonelyMessage implements Runnable {
        @Override
        public void run() {
            chatSession.send("someone talk to me please, I'm lonely and I always say Hi to new users");
        }
    }

    private class WelcomeNewUser implements Action1<UserJoinedEvent> {
        @Override
        public void call(final UserJoinedEvent userJoinedEvent) {
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    chatSession.send("Hi " + userJoinedEvent.getUsername() + " welcome to the chat");
                }
            }, 1, TimeUnit.SECONDS);
        }
    }
}
