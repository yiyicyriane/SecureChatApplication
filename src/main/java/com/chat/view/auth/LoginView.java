/*
用户输入自己的User ID 和Password,然后点击Sign In就可以登录(登录后会跳转到ChatListView界面），
然后登录按钮的左下方有Sign Up 按钮（点击后会跳转到Sign up的界面RegisterView.java 。
然后登录按钮的右下方有Reset Password按钮（点击后会跳转到ForgotPasswordView.java
用JavaFX完成
 */

package com.chat.view.auth;

import com.chat.controller.AuthController;
import com.chat.view.chat.ChatListView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;
    private AuthController authController;

    public LoginView(Stage stage, AuthController authController) {
        this.stage = stage;
        this.authController = authController;
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        Label userIdLabel = new Label("User ID:");
        userIdLabel.setFont(Font.font("Arial", 11));
        TextField userIdField = new TextField();
        userIdField.setPrefWidth(200);
        userIdField.setMaxWidth(200);
        userIdField.setMinWidth(200);
        VBox userIdBox = new VBox(5);
        userIdBox.getChildren().addAll(userIdLabel, userIdField);
        grid.add(userIdBox, 0, 0);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 11));
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setMinWidth(200);
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        grid.add(passwordBox, 0, 1);

        Button signInButton = new Button("Sign In");
        signInButton.setPrefWidth(200);
        signInButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");
        signInButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String password = passwordField.getText();
            boolean signInSuccess = authController.signIn(userId, password);
            if (signInSuccess) {
                Stage currentStage = (Stage) signInButton.getScene().getWindow();
                currentStage.close();

                ChatListView chatListView = new ChatListView();
                Stage chatListStage = new Stage();
                try {
                    chatListView.start(chatListStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign In Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid User ID or Password.");
                alert.showAndWait();
            }
        });
        grid.add(signInButton, 0, 2);

        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setMaxWidth(200);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(95);
        signUpButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        signUpButton.setOnAction(e -> {
            Stage registerStage = new Stage();
            RegisterView registerView = new RegisterView(registerStage, authController);
            registerView.show();
        });

        bottomButtons.getChildren().add(signUpButton);
        grid.add(bottomButtons, 0, 3);

        Scene scene = new Scene(grid, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Sign in");
        stage.show();
    }
}

