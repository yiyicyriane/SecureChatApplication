package com.chat.view.contacts;

import com.chat.controller.ContactController;
import com.chat.model.MembersInContactList;
import com.chat.util.ControllerManager;
import com.chat.view.chat.ChatListView;
import com.chat.view.chat.ChatWindowView;
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

import java.util.List;

public class ContactListView extends Application {

    private ContactController contactController;
    private HBox contactsContainer; // Contacts container to update dynamically
    private Stage stage;

    public ContactListView() throws Exception{
        this.contactController = ControllerManager.getInstance().getContactController();
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize layout
        VBox root = new VBox();
        root.setFillWidth(true);

        VBox mainContent = new VBox();
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(20);

        // New chat section
        VBox newChatSection = createNewChatSection(primaryStage);

        // Contacts list section
        contactsContainer = new HBox();
        contactsContainer.setFillHeight(true);

        VBox personalContactsSection = createContactsSection("Personal Contacts");
        personalContactsSection.setPrefWidth(285);
        VBox groupContactsSection = createContactsSection("Group Contacts");
        groupContactsSection.setPrefWidth(285);

        contactsContainer.setSpacing(20);
        contactsContainer.getChildren().addAll(personalContactsSection, groupContactsSection);

        mainContent.getChildren().addAll(newChatSection, contactsContainer);

        // Bottom navigation bar
        HBox bottomBar = createBottomBar(primaryStage);

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        root.getChildren().addAll(mainContent, bottomBar);

        // Set the scene and display the window
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Contact List");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial update to display contacts
        updateContacts();
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

        // Set button width
        addContactButton.setMinWidth(150);
        joinGroupButton.setMinWidth(150);
        createNewGroupButton.setMinWidth(150);

        newChatSection.setAlignment(Pos.CENTER);
        newChatSection.setSpacing(15);
        newChatSection.getChildren().addAll(newChatText, addContactButton, joinGroupButton, createNewGroupButton);

        // Set actions for buttons
        addContactButton.setOnAction(e -> {
            Stage addContactStage = new Stage();
            AddContactView addContactView = new AddContactView();
            addContactView.show(addContactStage, newContact -> {
                System.out.println("New contact added: " + newContact);
                contactController.addContact(contactController.currentUserId(),newContact);
                updateContacts();  // Refresh the contact list
            });
        });

        joinGroupButton.setOnAction(e -> {
            Stage joinGroupStage = new Stage();
            AddContactView joinGroupView = new AddContactView();
            joinGroupView.show(joinGroupStage, newContact -> {
                System.out.println("New group contact added: " + newContact);
                contactController.joinGroupContact(contactController.currentUserId(),newContact);
                updateContacts();  // Refresh the contact list
            });
        });

        createNewGroupButton.setOnAction(e -> {
            Stage newGroupStage = new Stage();
            NewGroupView newGroupView = null;
            try {
                newGroupView = new NewGroupView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            newGroupView.show(newGroupStage, (groupName, selectedMembers) -> {
                System.out.println("New group created: " + groupName);
                System.out.println("Selected members: " + selectedMembers);

                //后端创建，并跳转到新群聊界面
                String newGroupId = contactController.createNewGroup(contactController.currentUserId(), groupName, selectedMembers);
                try {
                    openChatWindowView(newGroupId);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                updateContacts();  // Refresh the contact list
            });
        });

        return newChatSection;
    }

    // Create contacts section (Personal or Group)
    private VBox createContactsSection(String sectionName) {
        VBox contactsSection = new VBox();
        contactsSection.setStyle("-fx-padding: 10; -fx-background-color: #95D2B3;");

        Text sectionTitle = new Text(sectionName);
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox contactsList = new VBox(10);
        contactsSection.getChildren().addAll(sectionTitle, contactsList);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contactsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefHeight(200);

        contactsSection.setAlignment(Pos.CENTER);
        contactsSection.setSpacing(20);

        // Populate contacts list (initially empty)
        updateContactsList(contactsList);

        return contactsSection;
    }

    // Update the contacts list section dynamically
    private void updateContactsList(VBox contactsList) {
        contactsList.getChildren().clear();  // Clear the current list

        // Fetch updated contacts from the controller
        List<MembersInContactList> contacts = contactController.getContacts();
        for (MembersInContactList contact : contacts) {
            if (contact.isGroupChatRoom()) {
                contactsList.getChildren().add(createGroupContactItem(contact));
            } else {
                contactsList.getChildren().add(createPersonalContactItem(contact));
            }
        }
    }

    // Update method to refresh the contacts container
    private void updateContacts() {
        // Update each section (personal and group contacts)
        for (int i = 0; i < contactsContainer.getChildren().size(); i++) {
            VBox section = (VBox) contactsContainer.getChildren().get(i);
            updateContactsList((VBox) section.getChildren().get(1)); // Update the contacts list inside the section
        }
    }

    // Create personal contact item
    private HBox createPersonalContactItem(MembersInContactList contact) {
        HBox contactItem = new HBox();
        contactItem.setAlignment(Pos.CENTER_LEFT);
        contactItem.setSpacing(10);
        contactItem.setPadding(new Insets(5, 10, 5, 10));

        Text contactName = new Text(contact.getUserId() + ": " + contact.getName());
        contactName.setStyle("-fx-font-size: 16px;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        deleteButton.setMinWidth(60);
        deleteButton.setVisible(false);

        contactItem.setOnMouseEntered(e -> deleteButton.setVisible(true));
        contactItem.setOnMouseExited(e -> deleteButton.setVisible(false));

        deleteButton.setOnAction(e -> {
            System.out.println("Delete button clicked for contact: " + contact.getName());
            contactController.removeContact(contactController.currentUserId(),contact.getUserId());
            updateContacts();  // Refresh the contact list
        });

        //open the chat window with this friend
        contactItem.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    openChatWindowView(contact.getChatRoomId());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contactItem.getChildren().addAll(contactName, deleteButton);
        return contactItem;
    }

    // Create group contact item
    private HBox createGroupContactItem(MembersInContactList contact) {
        HBox contactItem = new HBox();
        contactItem.setAlignment(Pos.CENTER_LEFT);
        contactItem.setSpacing(10);
        contactItem.setPadding(new Insets(5, 10, 5, 10));

        Text groupName = new Text(contact.getUserId() + ": " + contact.getName());
        groupName.setStyle("-fx-font-size: 16px;");

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        exitButton.setMinWidth(60);
        exitButton.setVisible(false);

        contactItem.setOnMouseEntered(e -> exitButton.setVisible(true));
        contactItem.setOnMouseExited(e -> exitButton.setVisible(false));

        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked for group: " + contact.getName());
            contactController.removeGroupContact(contactController.currentUserId(), contact.getChatRoomId());
            updateContacts();  // Refresh the contact list
        });

        contactItem.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    openChatWindowView(contact.getChatRoomId());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contactItem.getChildren().addAll(groupName, exitButton);
        return contactItem;
    }

    // Open ChatWindowView for contact or group
    private void openChatWindowView(String chatRoomId) throws Exception {
        System.out.println("Opening chat window for: " + chatRoomId);
        ChatWindowView chatWindowView = new ChatWindowView(chatRoomId);
        Stage chatWindowStage = new Stage();
        chatWindowView.start(chatWindowStage);
    }

    // Create bottom bar navigation
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);

        // Navigate to chats view
        chatsButton.setOnAction(e -> {
            System.out.println("Navigate to Chats view");
            ChatListView chatListView = null;
            try {
                chatListView = new ChatListView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                chatListView.start(stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Navigate to contacts view
        contactsButton.setOnAction(e -> {
            System.out.println("Navigate to Contacts view");
            updateContacts();
        });

        // Navigate to settings view
        settingsButton.setOnAction(e -> {
            System.out.println("Navigate to Settings view");
            ProfileSettingsView settingsView = new ProfileSettingsView();
            settingsView.start(stage);
        });

        return bottomBar;
    }
}
