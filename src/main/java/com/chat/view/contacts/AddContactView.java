package com.chat.view.contacts;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AddContactView extends Application {

    private Consumer<String> onContactAdded;

    public void start(Stage primaryStage, Consumer<String> onContactAdded) {
        this.onContactAdded = onContactAdded;

        // Create the root layout
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER_LEFT); // Align to the left
        root.setPadding(new Insets(15)); // Add some padding

        // Add instruction label
        Label instructionLabel = new Label("Please enter the user's/group's ID:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Create the contact ID input field
        TextField contactIdField = new TextField();
        contactIdField.setPromptText("Enter Contact ID");
        contactIdField.setMaxWidth(250); // Optional: Limit the width of the text field

        // Create the add button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String newContact = contactIdField.getText().trim();
            if (!newContact.isEmpty() && onContactAdded != null) {
                onContactAdded.accept(newContact);
                primaryStage.close(); // Close the window after adding the contact
            }
        });

        // Add all elements to the root layout
        root.getChildren().addAll(instructionLabel, contactIdField, addButton);

        // Create and show the scene
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add Contact");
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        start(primaryStage, null);
    }
}
