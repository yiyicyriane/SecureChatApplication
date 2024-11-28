package com.chat.service;

import java.lang.reflect.Type;
import java.util.Optional;
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

import com.chat.util.CurrentChatWindowViewContext;
import com.chat.util.CurrentUserContext;
import com.chat.util.CurrentViewContext;
import com.chat.view.auth.LoginView;
import com.chat.view.chat.ChatListView;
import com.chat.view.chat.ChatWindowView;
import com.chat.view.contacts.ContactListView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WebSocketService {
    private static WebSocketService instance;

    private final String serverUrl = "ws://54.153.42.91:8080/chat";
    private final WebSocketStompClient stompClient;
    private final String friendTopic;
    private final String chatRoomTopic;
    private final AuthService authService;
    private String messageTopic;
    private StompSession messageSession, friendSession, chatroomSession;

    // private WebSocketService() {}

    public static synchronized WebSocketService getInstance() throws Exception {
        if (instance == null) {
            instance = new WebSocketService();
        }
        return instance;
    }

    private WebSocketService() throws Exception {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());
        String userId = CurrentUserContext.getInstance().getCurrentUser().getUserId();
        // messageTopic = "/topic/messages/currentChatRoomId";
        friendTopic = "/topic/friend/" + userId;
        chatRoomTopic = "/topic/chatroom/" + userId;
        authService = new AuthService();
    }

    private class MessageStompFrameHandler implements StompFrameHandler {
        @Override
        public @NonNull Type getPayloadType(@NonNull StompHeaders stompHeaders) {
            return String.class;
        }
    
        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, @Nullable Object payload) {
            System.out.println("subscribe: " + messageTopic);
            // update chatroom messages
            Object currentView = CurrentViewContext.getInstance().getCurrentView();
            // if (currentView instanceof ChatListView) {
            //     try {
            //         ChatListView chatListView = (ChatListView) currentView;
            //         chatListView.updateChatListView();
            //     } catch (Exception e) {
            //         System.err.println("Refresh chat list view error");
            //         e.printStackTrace();
            //     }
            // }
            // else 
            if (currentView != null && currentView instanceof ChatWindowView) {
                try {
                    ChatWindowView chatWindowView = CurrentChatWindowViewContext.getInstance().getChatWindowView();
                    chatWindowView.updateChatWindow();
                } catch (Exception e) {
                    System.err.println("Refresh chat window view error");
                    e.printStackTrace();
                }
            }
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
            // TODO: show friend application notice, update contactview
            String friendId = (String) payload;
            Object currentView = CurrentViewContext.getInstance().getCurrentView();
            if (!(currentView == null || currentView instanceof LoginView)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("New Friend Application");
                alert.setHeaderText(null);
                alert.setContentText("Do you accept the friend application from the user ID: " + friendId);
                Optional<ButtonType> result = alert.showAndWait();
                try {
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        authService.postNewFriendApplicationSenderIdSet(CurrentUserContext.getInstance().getCurrentUser().getUserId(), friendId);
                        if (currentView instanceof ContactListView) {
                            ContactListView contactListView = (ContactListView) currentView;   
                            // TODO: contactListView.update();                     
                        }            
                    }
                } catch (Exception e) {
                    System.err.println("Refresh contact list view error");
                    e.printStackTrace();
                }
            }
        }
    }

    private class ChatRoomStompFrameHandler implements StompFrameHandler {

        @Override
        public @NonNull Type getPayloadType(@NonNull StompHeaders stompHeaders) {
            return String.class;
        }
    
        @Override
        public void handleFrame(@NonNull StompHeaders stompHeaders, @Nullable Object payload) {
            // receivedFriendApplicationFuture.complete((String) payload);
            System.out.println("subscribe: " + chatRoomTopic);
            // TODO: update chatlistview
            Object currentView = CurrentViewContext.getInstance().getCurrentView();
            if (currentView != null && currentView instanceof ChatListView) {
                try {
                    ChatListView chatListView = (ChatListView) currentView;
                    chatListView.updateChatListView();
                } catch (Exception e) {
                    System.err.println("Refresh chat list view error");
                    e.printStackTrace();
                }
            }
            else if (currentView instanceof ContactListView) {
                try {
                    ContactListView contactListView = (ContactListView) currentView;
                    // TODO: contactListView.update();
                } catch (Exception e) {
                    System.err.println("Refresh contact list view error");
                    e.printStackTrace();
                }
            }
        }
    }

    private class MessageStompSessionHandler extends StompSessionHandlerAdapter{
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            System.out.println("connected to message topic");
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
            System.out.println("connected to friend topic");
            session.subscribe(friendTopic, new FriendStompFrameHandler());
        }

        @Override
        public void handleException(@NonNull StompSession session, @Nullable StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
            throw new RuntimeException("Failure in WebSocket Stomp Session Handling", exception);
        }
    }

    private class ChatRoomStompSessionHandler extends StompSessionHandlerAdapter{
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            System.out.println("connected to chatroom topic");
            session.subscribe(chatRoomTopic, new ChatRoomStompFrameHandler());
        }

        @Override
        public void handleException(@NonNull StompSession session, @Nullable StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
            throw new RuntimeException("Failure in WebSocket Stomp Session Handling", exception);
        }
    }

    private void subscribeMessages() throws Exception {
        // Connect to the WebSocket server
        StompSessionHandler sessionHandler = new MessageStompSessionHandler();
        messageSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
    }

    public void subscribeFriendApplication() throws Exception {
        // Connect to the WebSocket server
        disconnectFriendSession();
        StompSessionHandler sessionHandler = new FriendStompSessionHandler();
        friendSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
    }

    public void subscribeChatRoomUpdate() throws Exception {
        // Connect to the WebSocket server
        disconnectChatRoomSession();
        StompSessionHandler sessionHandler = new ChatRoomStompSessionHandler();
        chatroomSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
    }

    public void subscribeCurrentChatRoomMessage(String currentChatRoomId) throws Exception {
        messageTopic = "/topic/messages/" + currentChatRoomId;
        disconnectMessageSession();
        subscribeMessages();
    }

    public void disconnectMessageSession() {
        if (messageSession != null)
            messageSession.disconnect();
    }
    
    public void disconnectFriendSession() {
        if (friendSession != null)
            friendSession.disconnect();
    }

    public void disconnectChatRoomSession() {
        if (chatroomSession != null)
            chatroomSession.disconnect();
    }
}
