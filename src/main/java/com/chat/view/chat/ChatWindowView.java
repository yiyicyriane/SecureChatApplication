package com.chat.view.chat;

import com.chat.model.ChatWindow;
import com.chat.controller.ChatController;
import com.chat.util.ControllerManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ChatWindowView extends Application {

    private String chatRoomId; // Chat room ID
    private ChatWindow chatWindow; // ChatWindow object holding data
    private List<String> messageList; // List of chat messages
    private long timestamp;

    private ChatController chatController;

    public ChatWindowView(String chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.chatController = ControllerManager.getInstance().getChatController();
        this.chatWindow = chatController.getChatWindowById(chatRoomId); // Get chat window data
        this.messageList = chatWindow.getMessageList(); // Get message list from ChatWindow
    }

    @Override
    public void start(Stage primaryStage) {
        // Build chat window layout
        VBox mainLayout = createChatLayout();

        // Configure scene and stage
        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat with " + chatWindow.getChatRoomName());
        primaryStage.show();
    }

    private VBox createChatLayout() {
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header with chat room image, name, and members button
        HBox header = new HBox(10);
        header.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        header.setAlignment(Pos.CENTER_LEFT);

        Image chatRoomImage = new Image(chatWindow.getProfilePicture());
        ImageView chatRoomImageView = new ImageView(chatRoomImage);
        chatRoomImageView.setFitWidth(40);
        chatRoomImageView.setFitHeight(40);

        Text chatRoomNameText = new Text(chatWindow.getChatRoomName());
        chatRoomNameText.setFont(Font.font("Arial", 18));
        chatRoomNameText.setFill(Color.WHITE);

        // Button to show group members
        Button membersButton = new Button("Members");
        membersButton.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #55AD9B;");
        membersButton.setOnAction(event -> showGroupMembers());

        header.getChildren().addAll(chatRoomImageView, chatRoomNameText, membersButton);

        // Chat history
        ScrollPane chatScrollPane = new ScrollPane();
        VBox chatBox = new VBox(10);
        chatBox.setStyle("-fx-padding: 10;");

        // Loop through message list and generate chat messages
        for (String messageData : messageList) {
            // message structure: "String chatRoomId, String senderId,String content,long timestamp"
            String[] messageParts = messageData.split("\\|");
            String senderId = messageParts[1];
            String content = messageParts[2];
            String timestamp = messageParts[3];

            VBox messageBox = new VBox(5);

            // Sender and time in one row
            HBox senderAndTimeBox = new HBox();
            senderAndTimeBox.setPrefWidth(400);
            senderAndTimeBox.setAlignment(Pos.CENTER_LEFT);

            Text senderText = new Text(senderId);
            senderText.setFont(Font.font("Arial", 14));
            senderText.setStyle("-fx-font-weight: bold;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Text timeText = new Text(timestamp);
            timeText.setFont(Font.font("Arial", 10));
            timeText.setFill(Color.GRAY);

            senderAndTimeBox.getChildren().addAll(senderText, spacer, timeText);

            // Chat message content
            Text messageText = new Text(content);
            messageText.setFont(Font.font("Arial", 12));

            // Add components to message box
            messageBox.getChildren().addAll(senderAndTimeBox, messageText);

            // Add delete button for user messages 如果senderId和当前用户id相等，则在它的那条聊天记录上加delete键
            if (senderId.equals(chatController.currentUserId())) {
                messageBox.setOnMouseEntered(event -> addDeleteOption(messageBox, messageList.indexOf(messageData)));
                messageBox.setOnMouseExited(event -> removeDeleteOption(messageBox));
            }

            chatBox.getChildren().add(messageBox);
        }

        chatScrollPane.setContent(chatBox);
        chatScrollPane.setFitToWidth(true);
        VBox.setVgrow(chatScrollPane, Priority.ALWAYS);

        // Input box at the bottom
        HBox inputBox = new HBox(10);
        inputBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextArea inputField = new TextArea();
        inputField.setPromptText("Type a message...");
        inputField.setPrefHeight(50);
        inputField.setPrefWidth(300);
        inputField.setWrapText(true);

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");
        sendButton.setOnAction(event -> {
            String newMessage = inputField.getText();
            if (!newMessage.trim().isEmpty()) {
                chatController.addMessageToChatRoom(chatRoomId, chatController.currentUserId(), newMessage, timestamp); // Add message using controller
                inputField.clear();
                updateChatWindow(); // Update chat window after sending a message
            }
        });

        inputBox.getChildren().addAll(inputField, sendButton);

        mainLayout.getChildren().addAll(header, chatScrollPane, inputBox);
        return mainLayout;
    }

    private void showGroupMembers() {
        Stage membersStage = new Stage();
        VBox membersBox = new VBox(10);
        membersBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff;");
        membersBox.setAlignment(Pos.CENTER_LEFT);

        for (String memberId : chatWindow.getMemberIdList()) {
            Text memberText = new Text(memberId);
            memberText.setFont(Font.font("Arial", 14));
            membersBox.getChildren().add(memberText);
        }

        Scene membersScene = new Scene(membersBox, 300, 200);
        membersStage.setScene(membersScene);
        membersStage.setTitle("Group Members");
        membersStage.show();
    }

    private void addDeleteOption(VBox messageBox, int index) {
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> {
            chatController.removeMessageFromChatRoom(chatRoomId, index); // Remove message using controller
            updateChatWindow(); // Update chat window after deletion
        });
        messageBox.getChildren().add(deleteButton);
    }

    private void removeDeleteOption(VBox messageBox) {
        if (messageBox.getChildren().size() > 2) {
            messageBox.getChildren().remove(messageBox.getChildren().size() - 1);
        }
    }

    public void updateChatWindow() {
        this.chatWindow = chatController.getChatWindowById(chatRoomId); // Get updated chat window data
        this.messageList = chatWindow.getMessageList(); // Update message list
        start(new Stage()); // Refresh the chat window
    }

}
