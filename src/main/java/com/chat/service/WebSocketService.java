package com.chat.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketService {
    private final String serverUrl = "ws://54.193.233.72:8080/chat";
    private final WebSocketStompClient stompClient;
    private final String friendTopic;
    private String messageTopic;
    private StompSession messageSession, friendSession;

    public WebSocketService(String currentChatRoomId, String userId) {
        stompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new StringMessageConverter());
        messageTopic = "/topic/messages/" + currentChatRoomId;
        friendTopic = "/topic/friend/" + userId;
    }

    private class MessageStompFrameHandler implements StompFrameHandler {

        @Override
        public @NonNull Type getPayloadType(@NonNull StompHeaders stompHeaders) {
            return String.class;
        }
    
        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, @Nullable Object payload) {
            // receivedFriendApplicationFuture.complete((String) payload);
            System.out.println("subscribe: " + messageTopic);
            // TODO: update chatroom messages
        }
    }

    private class FriendStompFrameHandler implements StompFrameHandler {

        @Override
        public @NonNull Type getPayloadType(@NonNull StompHeaders stompHeaders) {
            return String.class;
        }
    
        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, @Nullable Object payload) {
            // receivedFriendApplicationFuture.complete((String) payload);
            System.out.println("subscribe: " + friendTopic);
            // TODO: show friend application notice
        }
    }

    private class MessageStompSessionHandler extends StompSessionHandlerAdapter{
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            session.subscribe(messageTopic, new MessageStompFrameHandler());
        }

        @Override
        public void handleException(@NonNull StompSession session, @Nullable StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
            throw new RuntimeException("Failure in WebSocket Stomp Session Handling", exception);
        }
    }

    private class FriendStompSessionHandler extends StompSessionHandlerAdapter{
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            session.subscribe(friendTopic, new FriendStompFrameHandler());
        }

        @Override
        public void handleException(@NonNull StompSession session, @Nullable StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
            throw new RuntimeException("Failure in WebSocket Stomp Session Handling", exception);
        }
    }

    public void subscribeMessages() throws Exception {
        // Connect to the WebSocket server
        StompSessionHandler sessionHandler = new MessageStompSessionHandler();
        messageSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
    }

    public void subscribeFriendApplication() throws Exception {
        // Connect to the WebSocket server
        StompSessionHandler sessionHandler = new FriendStompSessionHandler();
        friendSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
    }

    public void updateCurrentChatRoom(String currentChatRoomId) throws Exception {
        messageTopic = "/topic/messages/" + currentChatRoomId;
        disconnectMessageSession();
        subscribeMessages();
    }

    public void disconnectMessageSession() {
        messageSession.disconnect();
    }
    
    public void disconnectFriendSession() {
        friendSession.disconnect();
    }
}
