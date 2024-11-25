package com.chat.view.settings;

import com.chat.util.CurrentUserContext;
import com.chat.model.User;
import com.chat.util.ProfilePictureHandler;
import com.chat.view.chat.ChatListView;
import com.chat.view.contacts.ContactListView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfileSettingsView {

    private final User currentUser; // Current user fetched from the global context
    private boolean receiveNotifications = false; // Notification setting, default is off

    public ProfileSettingsView() {
        // Fetch the current user from CurrentUserContext
        this.currentUser = CurrentUserContext.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }
    }

    public void start(Stage stage) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Create top area
        VBox topArea = createTopArea();
        // Create Profile Setting area
        VBox profileSettingArea = createProfileSettingArea(stage);
        // Create Notification Setting area
        VBox notificationSettingArea = createNotificationSettingArea();
        // Create bottom navigation bar
        HBox bottomBar = createBottomBar(stage);

        // Add all parts to the root layout
        root.getChildren().addAll(topArea, spacer(), profileSettingArea, spacer(), notificationSettingArea, spacer(), bottomBar);

        Scene scene = new Scene(root, 400, 600);
        Stage settingsStage = new Stage();
        settingsStage.setScene(scene);
        settingsStage.setTitle("Profile Settings");
        settingsStage.show();
    }

    // Create the top area with profile picture and username
    private VBox createTopArea() {
        VBox topArea = new VBox();
        topArea.setStyle("-fx-background-color: #55AD9B;");
        topArea.setPadding(new Insets(10));
        topArea.setAlignment(Pos.CENTER_LEFT);

        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        // Display user's profile picture
        ImageView profileImageView = new ImageView();
        Image profileImage = new Image(currentUser.getProfilePicture()); // Assume the image path is valid
        profileImageView.setImage(profileImage);
        profileImageView.setFitWidth(80);
        profileImageView.setFitHeight(80);
        profileImageView.setPreserveRatio(true);
        Circle clip = new Circle(40, 40, 40); // Circular clip
        profileImageView.setClip(clip);

        // Display user's name
        Label userNameLabel = new Label(currentUser.getName());
        userNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        content.getChildren().addAll(profileImageView, userNameLabel);
        topArea.getChildren().add(content);

        return topArea;
    }

    // Create the Profile Setting area
    private VBox createProfileSettingArea(Stage stage) {
        VBox profileSettingArea = new VBox(10);
        profileSettingArea.setStyle("-fx-background-color: #95D2B3;");
        profileSettingArea.setPadding(new Insets(10));
        profileSettingArea.setAlignment(Pos.CENTER);

        Label profileSettingLabel = new Label("Profile Setting");
        profileSettingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button updateProfilePictureButton = new Button("Update Profile Picture");
        updateProfilePictureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                Image newProfileImage = ProfilePictureHandler.createCircularProfilePicture(file);
                if (newProfileImage != null) {
                    currentUser.setProfilePicture(file.toURI().toString()); // Update user's profile picture path
                    showAlert("Success", "Profile picture updated successfully!");
                } else {
                    showAlert("Error", "Failed to update profile picture.");
                }
            }
        });

        profileSettingArea.getChildren().addAll(profileSettingLabel, updateProfilePictureButton);
        return profileSettingArea;
    }

    // Create the Notification Setting area
    private VBox createNotificationSettingArea() {
        VBox notificationSettingArea = new VBox(10);
        notificationSettingArea.setStyle("-fx-background-color: #95D2B3;");
        notificationSettingArea.setPadding(new Insets(10));
        notificationSettingArea.setAlignment(Pos.CENTER);

        Label notificationSettingLabel = new Label("Notification Setting");
        notificationSettingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        CheckBox notificationCheckBox = new CheckBox("Receive Notifications");
        notificationCheckBox.setSelected(receiveNotifications);
        notificationCheckBox.setOnAction(e -> receiveNotifications = notificationCheckBox.isSelected());

        notificationSettingArea.getChildren().addAll(notificationSettingLabel, notificationCheckBox);
        return notificationSettingArea;
    }

    // Create the bottom navigation bar
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #333; -fx-padding: 10;");
        bottomBar.setPrefHeight(50);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        chatsButton.setOnAction(e -> openView(new ChatListView(), stage));
        contactsButton.setOnAction(e -> openView(new ContactListView(), stage));
        settingsButton.setOnAction(e -> System.out.println("Navigating to Settings..."));

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }

    // Spacer between sections
    private Region spacer() {
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        return spacer;
    }

    // Open a new view
    private void openView(Object view, Stage stage) {
        if (view instanceof ProfileSettingsView) {
            ((ProfileSettingsView) view).start(stage);
        } else if (view instanceof ContactListView) {
            ((ContactListView) view).start(stage);
        } else if (view instanceof ChatListView) {
            ((ChatListView) view).start(stage);
        }
    }

    // Display an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
