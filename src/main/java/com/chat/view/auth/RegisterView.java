package com.chat.view.auth;

import com.chat.controller.AuthController; // 导入AuthController类，用于处理注册逻辑
import com.chat.util.ProfilePictureHandler; // 导入ProfilePictureHandler类，用于处理头像逻辑
import com.chat.model.User; // 导入User类，用于创建新用户对象

import javafx.geometry.Pos; // 导入Pos类，用于设置布局对齐方式
import javafx.scene.Scene; // 导入Scene类，用于创建场景
import javafx.scene.control.*; // 导入JavaFX控件类，例如Button、TextField、PasswordField、Label等
import javafx.scene.layout.GridPane; // 导入GridPane类，用于布局管理
import javafx.scene.layout.HBox; // 导入HBox类，用于水平布局
import javafx.scene.layout.VBox; // 导入VBox类，用于垂直布局
import javafx.scene.text.Font; // 导入Font类，用于设置字体
import javafx.stage.FileChooser; // 导入FileChooser类，用于选择文件
import javafx.stage.Stage; // 导入Stage类，表示窗口
import javafx.scene.image.Image; // 导入Image类，用于设置ImageView的图像
import javafx.scene.image.ImageView; // 导入ImageView类，用于显示头像

import java.io.File; // 导入File类，用于处理文件选择
import java.util.UUID; // 导入UUID，用于生成唯一ID

public class RegisterView {
    private Stage stage;
    private AuthController authController;
    private ImageView profileImageView; // 用于显示头像的ImageView

    // 构造函数
    public RegisterView(Stage stage, AuthController authController) {
        this.stage = stage;
        this.authController = authController;
        this.profileImageView = new ImageView();
    }

    // 显示注册界面
    public void show() {
        GridPane grid = new GridPane(); // 创建GridPane布局
        grid.setAlignment(Pos.CENTER); // 内容居中对齐
        grid.setVgap(15); // 设置垂直间距
        grid.setHgap(10); // 设置水平间距
        grid.setStyle("-fx-padding: 20;"); // 为整个GridPane增加内边距，四周留白

        // User ID 输入框
        Label userIdLabel = new Label("User ID: ");
        userIdLabel.setFont(Font.font("Arial", 11));
        TextField userIdField = new TextField();
        userIdField.setPrefWidth(200);
        VBox userIdBox = new VBox(5);
        userIdBox.getChildren().addAll(userIdLabel, userIdField);
        grid.add(userIdBox, 0, 0);

        // Name 输入框
        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Arial", 11));
        TextField nameField = new TextField();
        nameField.setPrefWidth(200);
        VBox nameBox = new VBox(5);
        nameBox.getChildren().addAll(nameLabel, nameField);
        grid.add(nameBox, 0, 1);


        // Password 输入框
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 11));
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        grid.add(passwordBox, 0, 2);

        // 上传头像部分
        Label profilePicLabel = new Label("Profile Picture:");
        profilePicLabel.setFont(Font.font("Arial", 11));
        Button uploadButton = new Button("Upload Picture"); // 上传头像按钮
        uploadButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;"); // 按钮样式
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser(); // 创建文件选择器
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg")); // 设置只能选择图片文件
            fileChooser.setTitle("Choose Profile Picture");
            File file = fileChooser.showOpenDialog(stage); // 显示文件选择对话框
            if (file != null) {
                Image circularImage = ProfilePictureHandler.createCircularProfilePicture(file); // 处理图片为圆形
                if (circularImage != null) {
                    profileImageView.setImage(circularImage); // 显示头像
                } else {
                    System.out.println("Failed to load profile picture.");
                }
            }
        });

        profileImageView.setFitWidth(100); // 设置头像宽度
        profileImageView.setFitHeight(100); // 设置头像高度
        VBox profilePicBox = new VBox(5); // 创建垂直布局
        profilePicBox.getChildren().addAll(profilePicLabel, uploadButton, profileImageView); // 添加标签、按钮和头像到布局
        grid.add(profilePicBox, 0, 3); // 添加到GridPane的第五行

        // 注册按钮
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;"); // 设置按钮样式
        registerButton.setOnAction(e -> { // 注册按钮点击事件
            String userId = userIdField.getText();
            String name = nameField.getText();
            String password = passwordField.getText();

            // 自动生成唯一的ID
            String uniqueId = UUID.randomUUID().toString();

            // 获取头像图片路径
            String profilePicture = profileImageView.getImage() != null ? profileImageView.getImage().getUrl() : "";

            // 创建用户对象
            User newUser = new User(userId, name, password, profilePicture);
            if (authController.register(newUser)) { // 调用AuthController的register方法
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText("You have successfully registered!");
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Failed");
                alert.setHeaderText("User ID already exists!");
                ButtonType okButtonFail = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButtonFail);
                alert.showAndWait();
            }
        });

        // 注册按钮居中
        HBox registerButtonBox = new HBox(10);
        registerButtonBox.setAlignment(Pos.CENTER);
        registerButtonBox.getChildren().add(registerButton);
        grid.add(registerButtonBox, 0, 4); // 将注册按钮添加到GridPane的第六行

        // 创建场景并显示
        Scene scene = new Scene(grid, 300, 500); // 创建新场景，设置宽高
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show(); // 显示舞台
    }
}
