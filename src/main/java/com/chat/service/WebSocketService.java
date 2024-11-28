package com.chat.service;

import java.lang.reflect.Type;
import java.util.List;
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
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.chat.model.Contact;
import com.chat.util.CurrentUserContext;
import com.chat.util.CurrentViewContext;
import com.chat.view.auth.LoginView;
import com.chat.view.chat.ChatListView;
import com.chat.view.chat.ChatWindowView;
import com.chat.view.contacts.ContactListView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class WebSocketService {
    private final String serverUrl = "ws://54.193.233.72:8080/chat";
    private final WebSocketStompClient stompClient;
    private final String friendTopic;
    private final String chatRoomTopic;
    private final AuthService authService;
    private String messageTopic;
    private StompSession messageSession, friendSession, chatroomSession;

    public WebSocketService(String currentChatRoomId, String userId) throws Exception {
        stompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new StringMessageConverter());
        messageTopic = "/topic/messages/" + currentChatRoomId;
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
            if (currentView instanceof ChatListView) {
                try {
                    ChatListView chatListView = (ChatListView) currentView;
                    chatListView.updateChatListView();
                } catch (Exception e) {
                    System.err.println("Refresh chat list view error");
                    e.printStackTrace();
                }
            }
            else if (currentView instanceof ChatWindowView) {
                try {
                    ChatWindowView chatWindowView = (ChatWindowView) currentView;
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
            if (!(currentView instanceof LoginView || currentView == null)) {
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
            if (currentView instanceof ChatListView) {
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

    private class ChatRoomStompSessionHandler extends StompSessionHandlerAdapter{
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            session.subscribe(chatRoomTopic, new ChatRoomStompFrameHandler());
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

    public void subscribeChatRoomUpdate() throws Exception {
        // Connect to the WebSocket server
        StompSessionHandler sessionHandler = new ChatRoomStompSessionHandler();
        chatroomSession = stompClient.connectAsync(serverUrl, sessionHandler).get(5, TimeUnit.SECONDS);
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

    public void disconnectChatRoomSession() {
        chatroomSession.disconnect();
    }
}
