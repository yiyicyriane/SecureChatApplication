package com.chat.view.chat;

import com.chat.view.contacts.ContactListView;
import com.chat.view.settings.ProfileSettingsView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatListView extends Application {

    @Override
    public void start(Stage stage) {
        // Create the layout for the chat list view
        VBox root = new VBox(10); // A VBox layout with 10 pixels of spacing
        root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        // Create the chat items (conversation list)
        HBox chatItem1 = createChatItem("10001","John Doe", "Hey, how are you?", "12:30 PM", 3, "A");
        HBox chatItem2 = createChatItem("10002","Group Chat", "Meeting at 3pm", "11:00 AM", 0, "G");

        // Add chat items to the layout
        VBox chatList = new VBox(10);
        chatList.getChildren().addAll(chatItem1, chatItem2);
        root.getChildren().add(chatList);

        // Add a spacer to push the bottom bar to the bottom of the window
        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS); // Allow the spacer to grow and push bottom bar down
        root.getChildren().add(spacer);

        // Bottom buttons for Chats, Contacts, and Settings
        HBox bottomBar = createBottomBar(stage);
        root.getChildren().add(bottomBar);

        // Set up the scene and stage
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Chat List");
        stage.setScene(scene);
        stage.show();
    }

    // A helper method to create the bottom navigation bar (Chats, Contacts, Settings)
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox(30); // 30 pixels spacing
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        // Set actions for each button (for now, they print the section clicked)
        chatsButton.setOnAction(e -> System.out.println("Chats clicked"));
        contactsButton.setOnAction(e -> {
            System.out.println("Navigating to ContactListView...");
            ContactListView contactListView = new ContactListView();
            try {
                contactListView.start(stage); // 使用当前 Stage 启动 ContactListView
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        settingsButton.setOnAction(e -> {
            ProfileSettingsView profileSettingsView = new ProfileSettingsView(); // No need to pass user
            profileSettingsView.start(stage); // Directly start the ProfileSettingsView
        });

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }

    // A helper method to create a chat item (conversation) layout
    private HBox createChatItem(String chatRoomId,String name, String lastMessage, String lastMessageTime, int unreadCount, String avatar) {
        HBox chatItem = new HBox(10); // HBox with 10 pixels of spacing
        chatItem.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #dcdcdc;");

        // Avatar (use avatar string, could be a URL or path to image in future)
        Label avatarLabel = new Label(avatar); // Placeholder for the avatar (use an image in a real app)
        avatarLabel.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white; -fx-font-size: 20px; -fx-alignment: center; -fx-pref-width: 40px; -fx-pref-height: 40px; -fx-border-radius: 20px;");

        // Name of the contact or group
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Last message preview
        Label lastMessageLabel = new Label(lastMessage);
        lastMessageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        // Last message time
        Label timeLabel = new Label(lastMessageTime);
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888888;");

        // Unread messages count (if any)
        Label unreadLabel = new Label(unreadCount > 0 ? "(" + unreadCount + ")" : "");
        unreadLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");

        // Set the unreadLabel to appear below the timeLabel and aligned right
        VBox timeAndUnread = new VBox(2);  // 2 pixels of space between time and unread count
        timeAndUnread.setAlignment(Pos.CENTER_RIGHT);  // Align to the right
        timeAndUnread.getChildren().addAll(timeLabel, unreadLabel);

        // Make the entire chat item clickable to open ChatWindowView
        chatItem.setOnMouseClicked((MouseEvent event) -> {
            // 调用 ChatWindowView，并传递聊天的相关信息
            ChatWindowView chatWindowView = new ChatWindowView(chatRoomId); // 传递聊天的ID
            Stage chatWindowStage = new Stage();
            try {
                chatWindowView.start(chatWindowStage); // 打开聊天窗口
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Add components to the chat item layout
        VBox chatInfo = new VBox(5);
        chatInfo.getChildren().addAll(nameLabel, lastMessageLabel);
        HBox.setHgrow(chatInfo, javafx.scene.layout.Priority.ALWAYS);

        chatItem.getChildren().addAll(avatarLabel, chatInfo, timeAndUnread);

        // Optional: handle right swipe for delete functionality (this is just a basic concept, might need more code for actual swipe detection)
        chatItem.setOnMouseDragged(e -> {
            System.out.println("Drag detected, consider showing delete button");
        });

        return chatItem;
    }

    public static void main(String[] args) {
        launch(args);
    }
}