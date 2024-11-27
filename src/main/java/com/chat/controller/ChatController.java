package com.chat.controller;

import com.chat.view.chat.ChatListView;
import com.chat.view.chat.ChatWindowView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.chat.view.contacts.ContactListView;
import com.chat.view.settings.ProfileSettingsView;

public class ChatController {

    private ChatListView chatListView;
    private Stage stage;

    public ChatController(ChatListView chatListView, Stage stage) {
        this.chatListView = chatListView;
        this.stage = stage;
    }

    // Method to load chat list (TODO: Fetch chat list from service or database)
    public void loadChatList() {
        // TODO: Fetch chat list from database or service
        // Placeholder for adding chat items to the view
        chatListView.addChatItem(chatListView.createChatItem("10001", "John Doe", "Hey, how are you?", "12:30 PM", "A"));
        chatListView.addChatItem(chatListView.createChatItem("10002", "Group Chat", "Meeting at 3pm", "11:00 AM", "G"));
    }

    // Method to handle click on "Chats" button
    public void onChatsClicked() {
        System.out.println("Chats clicked");
    }

    // Method to handle click on "Contacts" button
    public void onContactsClicked() {
        System.out.println("Navigating to ContactListView...");
        ContactListView contactListView = new ContactListView();
        try {
            contactListView.start(stage); // Navigate to the contact list view
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to handle click on "Settings" button
    public void onSettingsClicked() {
        System.out.println("Navigating to ProfileSettingsView...");
        ProfileSettingsView profileSettingsView = new ProfileSettingsView();
        try {
            profileSettingsView.start(stage); // Navigate to the profile settings view
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to handle click on a chat item
    public void onChatItemClicked(String chatRoomId) {
        System.out.println("Opening chat window for chat room: " + chatRoomId);
        // Create a new instance of ChatWindowView and pass the chatRoomId to it
        ChatWindowView chatWindowView = new ChatWindowView(chatRoomId);

        try {
            chatWindowView.start(stage); // Navigate to the chat window view for the selected chat room
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
