package com.chat.view.settings;

import com.chat.util.CurrentUserContext;
import com.chat.model.User;
import com.chat.util.ProfilePictureHandler;
import com.chat.view.chat.ChatListView;
import com.chat.view.contacts.ContactListView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfileSettingsView extends Application {

    private User currentUser; // Current user fetched from the global context
    private boolean receiveNotifications = false; // Notification setting, default is off

    @Override
    public void start(Stage stage) {
        // Initialize current user for demo purpose
        initializeCurrentUser();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Create top area
        VBox topArea = createTopArea();
        // Create Profile Setting area and Notification Setting area
        VBox centerArea = new VBox(2, createProfileSettingArea(stage), createNotificationSettingArea());
        centerArea.setVgrow(centerArea, Priority.ALWAYS);
        centerArea.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 20 0;");

        // Create bottom navigation bar
        HBox bottomBar = createBottomBar(stage);

        // Add all parts to the root layout
        root.setTop(topArea);
        root.setCenter(centerArea);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Profile Settings");
        stage.show();
    }

    // Initialize a mock user for demonstration
    private void initializeCurrentUser() {
        currentUser = new User("1", "Demo User", "123456", "file:profile_picture.png");
        CurrentUserContext.getInstance().setCurrentUser(currentUser);
    }

    // Create the top area with profile picture and username
    private VBox createTopArea() {
        VBox topArea = new VBox();
        topArea.setStyle("-fx-background-color: #55AD9B; -fx-padding: 20 10 30 10;");
        topArea.setAlignment(Pos.CENTER_LEFT);

        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        // Display user's profile picture
        ImageView profileImageView = new ImageView();
        Image profileImage = new Image("https://via.placeholder.com/40?text=G"); // Set the path to your profile picture
        profileImageView.setImage(profileImage);
        profileImageView.setFitWidth(80);
        profileImageView.setFitHeight(80);
        profileImageView.setPreserveRatio(true);
        Circle clip = new Circle(40, 40, 40); // Circular clip
        profileImageView.setClip(clip);

        // Display user's name
        Label userNameLabel = new Label("Demo User");
        userNameLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold;");

        content.getChildren().addAll(profileImageView, userNameLabel);
        topArea.getChildren().add(content);

        return topArea;
    }

    // Create the Profile Setting area
    private VBox createProfileSettingArea(Stage stage) {
        VBox profileSettingArea = new VBox(10);
        profileSettingArea.setStyle("-fx-background-color: #95D2B3; -fx-padding: 30 10 30 10;"); // Increased padding
        profileSettingArea.setAlignment(Pos.CENTER_LEFT);

        Label profileSettingLabel = new Label("Profile Setting");
        profileSettingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button updateProfilePictureButton = new Button("Update Profile Picture");
        updateProfilePictureButton.setStyle("-fx-alignment: CENTER_LEFT;");
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
        notificationSettingArea.setStyle("-fx-background-color: #95D2B3; -fx-padding: 30 10 30 10;"); // Increased padding
        notificationSettingArea.setAlignment(Pos.CENTER_LEFT);

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
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        bottomBar.setPrefHeight(50);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        chatsButton.setOnAction(e -> openView(new ChatListView(), stage));
        contactsButton.setOnAction(e -> openView(new ContactListView(), stage));
        settingsButton.setOnAction(e -> System.out.println("Already in Settings"));

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


    // Display an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}