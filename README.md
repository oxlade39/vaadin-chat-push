# Vaadin Push Example

This is my take on the commonly distributed demo websocket application - a chat application.
It's very basic and still work in progress but I wanted to play with the combination of [rxjava](https://github.com/Netflix/RxJava) and [vaadin](https://vaadin.com/home)'s newly support push feature.

## Simple functional reactive webapp

Using [rxjava](https://github.com/Netflix/RxJava) to power the main [Chat](https://github.com/oxlade39/vaadin-chat-push/blob/master/src/main/java/com/github/oxlade39/chat/Chat.java) class is extremely powerful. 
I think this combination of technologies could prove to be extremely productive.

## Why did I write this?

I wrote the first version of this while up late waiting for the first trades of the day to enter the system where I work. 
Ensuring that the weekend release had gone out as expected.

More importantly I wanted to experiment with the new [vaadin](https://vaadin.com/home) push support.

## Build

mvn clean install

## Run

- run the [_com.github.oxlade39.vaadin.Main_](https://github.com/oxlade39/vaadin-chat-push/blob/master/src/main/java/com/github/oxlade39/vaadin/Main.java) class. or
- run mvn exec:java

I've written it to use an Embedded Jetty instance, just because I like a pure java solution.

## Try it out

I've deployed it to [heroku](http://vaadinchatpush.herokuapp.com/) which doesn't support websockets but the vaadin should
 gracfully degrade to polling.

TODO add more documentation...
