package com.chat.view.contacts;

import com.chat.view.chat.ChatListView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ContactListView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create root layout (VBox for vertical stacking)
        VBox root = new VBox();
        root.setFillWidth(true);

        // Create main content area that will contain everything except bottom bar
        VBox mainContent = new VBox();
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(20); // Consistent spacing between sections

        // Create New Chat Section
        VBox newChatSection = createNewChatSection();

        // Create contacts container for both Personal and Group contacts
        HBox contactsContainer = new HBox();
        contactsContainer.setFillHeight(true);

        // Create Personal Contacts Section with adjusted width
        VBox personalContactsSection = createContactsSection("Personal Contacts", false);
        personalContactsSection.setPrefWidth(285); // Make it wider
        HBox.setHgrow(personalContactsSection, Priority.ALWAYS);

        // Create Group Contacts Section with adjusted width
        VBox groupContactsSection = createContactsSection("Group Contacts", true);
        groupContactsSection.setPrefWidth(285); // Make it wider
        HBox.setHgrow(groupContactsSection, Priority.ALWAYS);

        // Add spacing between personal and group contacts
        contactsContainer.setSpacing(20);
        contactsContainer.getChildren().addAll(personalContactsSection, groupContactsSection);

        // Add components to main content
        mainContent.getChildren().addAll(newChatSection, contactsContainer);

        // Create bottom navigation bar
        HBox bottomBar = createBottomBar(primaryStage);

        // Create spacer to push bottom bar to bottom
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Add everything to the root layout
        root.getChildren().addAll(mainContent, bottomBar);

        // Set root layout and scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Contact List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createNewChatSection() {
        VBox newChatSection = new VBox();
        newChatSection.setStyle("-fx-background-color: #95D2B3; -fx-padding: 10;");

        Text newChatText = new Text("New Chat");
        newChatText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addContactButton = new Button("Add Contact");
        Button joinGroupButton = new Button("Join Group");
        Button createNewGroupButton = new Button("Create New Group");

        addContactButton.setMinWidth(150);
        joinGroupButton.setMinWidth(150);
        createNewGroupButton.setMinWidth(150);

        newChatSection.setSpacing(15);

        addContactButton.setStyle("-fx-alignment: left;");
        joinGroupButton.setStyle("-fx-alignment: left;");
        createNewGroupButton.setStyle("-fx-alignment: left;");

        newChatSection.getChildren().addAll(newChatText, addContactButton, joinGroupButton, createNewGroupButton);

        return newChatSection;
    }

    private VBox createContactsSection(String sectionName, boolean isGroup) {
        VBox contactsSection = new VBox();
        contactsSection.setStyle("-fx-padding: 10; -fx-background-color: #95D2B3;");

        Text sectionTitle = new Text(sectionName);
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Simulate more contacts (you can replace this with real data when connected to backend)
        String[] contacts = isGroup ?
                new String[]{"Group 1", "Group 2", "Group 3", "Group 4", "Group 5", "Group 6", "Group 7"} :
                new String[]{"Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Hannah"};

        VBox contactsList = new VBox(10);
        for (String contact : contacts) {
            Text contactName = new Text(contact);
            contactName.setStyle("-fx-font-size: 16px;");
            contactsList.getChildren().add(contactName);
        }

        // Add scroll functionality to the contacts section
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contactsList);
        scrollPane.setFitToWidth(true); // Make the scroll pane fill the width of the section
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Always show vertical scrollbar if needed
        scrollPane.setPrefHeight(200); // Set fixed height for scroll area, adjust based on the available space

        contactsSection.setSpacing(10);
        contactsSection.getChildren().addAll(sectionTitle, scrollPane);

        return contactsSection;
    }

    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #333; -fx-padding: 10;");
        bottomBar.setPrefHeight(50);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        // Set buttons to have equal width
        chatsButton.setMaxWidth(Double.MAX_VALUE);
        contactsButton.setMaxWidth(Double.MAX_VALUE);
        settingsButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(chatsButton, Priority.ALWAYS);
        HBox.setHgrow(contactsButton, Priority.ALWAYS);
        HBox.setHgrow(settingsButton, Priority.ALWAYS);

        chatsButton.setOnAction(e -> {
            ChatListView chatListView = new ChatListView();
            try {
                chatListView.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        contactsButton.setOnAction(e -> {
            ContactListView contactListView = new ContactListView();
            try {
                contactListView.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        settingsButton.setOnAction(e -> {
            System.out.println("Navigating to Settings...");
        });

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);

        return bottomBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
