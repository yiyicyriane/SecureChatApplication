package com.chat.view.chat;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ChatWindowView extends Application {

    private String chatRoomId; // Chat room ID 聊天对象ID
    private String chatRoomName; // Chat room name 聊天对象名称
    private String chatRoomImageURL; // Chat room image URL 聊天对象头像
    private List<String> messages; // List of chat messages 聊天消息列表
    private List<String> messageTimes; // List of message timestamps 消息时间列表
    private List<String> senders; // List of message senders 消息发送者列表

    public ChatWindowView(String chatRoomId) {
        this.chatRoomId = chatRoomId; // Initialize chatRoomId 初始化聊天对象ID
    }

    @Override
    public void start(Stage primaryStage) {
        // Load chat data for simulation // 加载模拟聊天数据
        loadChatData(chatRoomId);

        // Build chat window layout // 创建聊天界面布局
        VBox mainLayout = createChatLayout();

        // Configure scene and stage // 配置场景和舞台
        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat with " + chatRoomName);
        primaryStage.show();
    }

    private void loadChatData(String chatRoomId) {
        chatRoomName = chatRoomId.equals("group1") ? "Study Group" : "Jane Smith";
        chatRoomImageURL = chatRoomId.equals("group1")
                ? "https://via.placeholder.com/40?text=G"
                : "https://via.placeholder.com/40?text=J";

        messages = new ArrayList<>();
        messageTimes = new ArrayList<>();
        senders = new ArrayList<>();

        if (chatRoomId.equals("group1")) {
            senders.add("John Doe");
            messages.add("Hi, team! What's the status of the project?");
            messageTimes.add("10:00 AM");

            senders.add("Jane Smith");
            messages.add("I've finished the report and sent it to everyone.");
            messageTimes.add("10:15 AM");
        } else {
            senders.add("Jane Smith");
            messages.add("Hi, how are you?");
            messageTimes.add("10:30 AM");

            senders.add("You");
            messages.add("I'm good, thanks! How about you?");
            messageTimes.add("10:32 AM");
        }
    }

    private VBox createChatLayout() {
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header with chat room image and name 顶部部分：头像和名称
        HBox header = new HBox(10);
        header.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        header.setAlignment(Pos.CENTER_LEFT);

        Image chatRoomImage = new Image(chatRoomImageURL);
        ImageView chatRoomImageView = new ImageView(chatRoomImage);
        chatRoomImageView.setFitWidth(40);
        chatRoomImageView.setFitHeight(40);

        Text chatRoomNameText = new Text(chatRoomName);
        chatRoomNameText.setFont(Font.font("Arial", 18));
        chatRoomNameText.setFill(Color.WHITE);

        header.getChildren().addAll(chatRoomImageView, chatRoomNameText);

        // Chat history 中间部分：聊天记录
        ScrollPane chatScrollPane = new ScrollPane();
        VBox chatBox = new VBox(10);
        chatBox.setStyle("-fx-padding: 10;");

        for (int i = 0; i < messages.size(); i++) {
            VBox messageItem = new VBox(5);

            // Sender and time in one row // 发送人和时间在同一行
            HBox senderAndTimeBox = new HBox();
            senderAndTimeBox.setPrefWidth(400); // Expand to full width // 扩展到全宽
            senderAndTimeBox.setAlignment(Pos.CENTER_LEFT);

            Text senderText = new Text(senders.get(i));
            senderText.setFont(Font.font("Arial", 14));
            senderText.setStyle("-fx-font-weight: bold;");

            Region spacer = new Region(); // Spacer to push time to the right // 占位符将时间推到右侧
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Text timeText = new Text(messageTimes.get(i));
            timeText.setFont(Font.font("Arial", 10));
            timeText.setFill(Color.GRAY);

            senderAndTimeBox.getChildren().addAll(senderText, spacer, timeText);

            // Chat message content // 聊天内容
            Text messageText = new Text(messages.get(i));
            messageText.setFont(Font.font("Arial", 12));

            messageItem.getChildren().addAll(senderAndTimeBox, messageText);
            chatBox.getChildren().add(messageItem);
        }

        chatScrollPane.setContent(chatBox);
        chatScrollPane.setFitToWidth(true);
        VBox.setVgrow(chatScrollPane, Priority.ALWAYS);

        // Input box at the bottom 底部部分：输入框和发送按钮
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
                messages.add(newMessage);
                messageTimes.add("Now");
                senders.add("You");
                inputField.clear();
            }
        });

        inputBox.getChildren().addAll(inputField, sendButton);

        mainLayout.getChildren().addAll(header, chatScrollPane, inputBox);
        return mainLayout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
