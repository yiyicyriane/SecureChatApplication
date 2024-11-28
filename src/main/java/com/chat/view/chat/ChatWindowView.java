package com.chat.view.chat;

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
import lombok.Getter;

//需要传入chatroomid
public class ChatWindowView {

    private VBox mainLayout; // 主布局
    // 提供公共方法供 Controller 调用
    @Getter
    private VBox chatBox; // 消息列表容器
    private TextArea inputField; // 输入框
    private Button sendButton; // 发送按钮
    private Button membersButton; // 查看成员按钮
    private Label chatRoomNameLabel; // 聊天室名称

    public ChatWindowView(Stage primaryStage) {
        // 初始化布局
        mainLayout = createChatLayout();

        // 配置场景和舞台
        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat");
        primaryStage.show();
    }

    private VBox createChatLayout() {
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header with chat room image, name, and members button
        HBox header = new HBox(10);
        header.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        header.setAlignment(Pos.CENTER_LEFT);

        Image chatRoomImage = new Image("https://via.placeholder.com/40?text=C");
        ImageView chatRoomImageView = new ImageView(chatRoomImage);
        chatRoomImageView.setFitWidth(40);
        chatRoomImageView.setFitHeight(40);

        chatRoomNameLabel = new Label("Chat Room");
        chatRoomNameLabel.setFont(Font.font("Arial", 18));
        chatRoomNameLabel.setTextFill(Color.WHITE);

        // Button to show group members
        membersButton = new Button("Members");
        membersButton.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #55AD9B;");

        header.getChildren().addAll(chatRoomImageView, chatRoomNameLabel, membersButton);

        // Chat history
        ScrollPane chatScrollPane = new ScrollPane();
        chatBox = new VBox(10);
        chatBox.setStyle("-fx-padding: 10;");

        chatScrollPane.setContent(chatBox);
        chatScrollPane.setFitToWidth(true);
        VBox.setVgrow(chatScrollPane, Priority.ALWAYS);

        // Input box at the bottom
        HBox inputBox = new HBox(10);
        inputBox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");
        inputBox.setAlignment(Pos.CENTER_LEFT);

        inputField = new TextArea();
        inputField.setPromptText("Type a message...");
        inputField.setPrefHeight(50);
        inputField.setPrefWidth(300);
        inputField.setWrapText(true);

        sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");

        inputBox.getChildren().addAll(inputField, sendButton);

        mainLayout.getChildren().addAll(header, chatScrollPane, inputBox);
        return mainLayout;
    }

    public TextArea getInputField() {
        return inputField;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public Button getMembersButton() {
        return membersButton;
    }

    public Label getChatRoomNameLabel() {
        return chatRoomNameLabel;
    }
}
