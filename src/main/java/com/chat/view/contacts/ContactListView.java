package com.chat.view.contacts;

import com.chat.view.chat.ChatListView;
import com.chat.view.settings.ProfileSettingsView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ContactListView extends Application {

    // List to store personal contacts
    private final List<String> personalContacts = new ArrayList<>();
    // List to store group contacts
    private final List<String> groupContacts = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Initialize window layout
        VBox root = new VBox();
        root.setFillWidth(true);

        // Main content area
        VBox mainContent = new VBox();
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(20);

        // New chat section at the top
        VBox newChatSection = createNewChatSection(primaryStage);

        // Contacts container
        HBox contactsContainer = new HBox();
        contactsContainer.setFillHeight(true);

        VBox personalContactsSection = createContactsSection("Personal Contacts", personalContacts);
        personalContactsSection.setPrefWidth(285);
        VBox groupContactsSection = createContactsSection("Group Contacts", groupContacts);
        groupContactsSection.setPrefWidth(285);

        contactsContainer.setSpacing(20);
        contactsContainer.getChildren().addAll(personalContactsSection, groupContactsSection);

        mainContent.getChildren().addAll(newChatSection, contactsContainer);

        // Bottom navigation bar
        HBox bottomBar = createBottomBar(primaryStage);

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        root.getChildren().addAll(mainContent, bottomBar);

        // Set the scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Contact List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create new chat section
    private VBox createNewChatSection(Stage primaryStage) {
        VBox newChatSection = new VBox();
        newChatSection.setStyle("-fx-background-color: #95D2B3; -fx-padding: 10;");

        Text newChatText = new Text("New Chat");
        newChatText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addContactButton = new Button("Add Contact");
        Button joinGroupButton = new Button("Join Group");
        Button createNewGroupButton = new Button("Create New Group");

        // Set button widths
        addContactButton.setMinWidth(150);
        joinGroupButton.setMinWidth(150);
        createNewGroupButton.setMinWidth(150);

        newChatSection.setAlignment(Pos.CENTER);
        newChatSection.setSpacing(15);
        newChatSection.getChildren().addAll(newChatText, addContactButton, joinGroupButton, createNewGroupButton);

        // Add functionality to the Add Contact button
        addContactButton.setOnAction(e -> {
            AddContactView addContactView = new AddContactView();
            addContactView.start(new Stage(), newContactId -> {
                // Update the personal contacts list
                personalContacts.add(newContactId);
                refreshContactList(primaryStage);
            });
        });

        // Add functionality to the Join Group button
        joinGroupButton.setOnAction(e -> {
            AddContactView addContactView = new AddContactView();
            addContactView.start(new Stage(), newGroupId -> {
                // Update the group contacts list
                groupContacts.add(newGroupId);
                refreshContactList(primaryStage);
            });
        });
        // Add functionality to the Create New Group button
        createNewGroupButton.setOnAction(e -> {
            NewGroupView newGroupView = new NewGroupView();
            newGroupView.start(new Stage(), newGroupId -> {
                // Update the group contacts list
                groupContacts.add(newGroupId);
                refreshContactList(primaryStage);
            });
        });

        return newChatSection;
    }

    // Create contacts section
    private VBox createContactsSection(String sectionName, List<String> contacts) {
        VBox contactsSection = new VBox();
        contactsSection.setStyle("-fx-padding: 10; -fx-background-color: #95D2B3;");

        Text sectionTitle = new Text(sectionName);
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox contactsList = new VBox(10);
        for (String contact : contacts) {
            contactsList.getChildren().add(createContactItem(contact, contacts));
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contactsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefHeight(200);

        contactsSection.setAlignment(Pos.CENTER);
        contactsSection.setSpacing(20);
        contactsSection.getChildren().addAll(sectionTitle, scrollPane);

        return contactsSection;
    }

    // Create contact item
    private HBox createContactItem(String contact, List<String> contacts) {
        HBox contactItem = new HBox();
        contactItem.setAlignment(Pos.CENTER_LEFT);
        contactItem.setSpacing(10);
        contactItem.setPadding(new Insets(5, 10, 5, 10));

        Text contactName = new Text(contact);
        contactName.setStyle("-fx-font-size: 16px;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-font-size: 12px;");
        deleteButton.setVisible(false);

        deleteButton.setOnAction(e -> {
            contacts.remove(contact);
            contactItem.setVisible(false);
        });

        contactItem.setOnMouseEntered(e -> deleteButton.setVisible(true));
        contactItem.setOnMouseExited(e -> deleteButton.setVisible(false));

        contactItem.getChildren().addAll(contactName, deleteButton);
        return contactItem;
    }

    // Create bottom navigation bar
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        bottomBar.setPrefHeight(50);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        HBox.setHgrow(chatsButton, Priority.ALWAYS);
        HBox.setHgrow(contactsButton, Priority.ALWAYS);
        HBox.setHgrow(settingsButton, Priority.ALWAYS);

        chatsButton.setOnAction(e -> openView(new ChatListView(), stage));
        contactsButton.setOnAction(e -> openView(new ContactListView(), stage));
        settingsButton.setOnAction(e -> {
            System.out.println("Settings button clicked");
            ProfileSettingsView profileSettingsView = new ProfileSettingsView();
            Stage settingsStage = new Stage();  // Create a new Stage
            profileSettingsView.start(settingsStage); // Pass the new Stage
        });

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }

    // Generic method to open a new view
    private void openView(Application view, Stage stage) {
        try {
            view.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Refresh the contact list
    private void refreshContactList(Stage stage) {
        start(stage); // Reload the interface
    }

    public static void main(String[] args) {
        launch(args);
    }
}