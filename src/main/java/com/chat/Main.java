package com.chat;

import com.chat.controller.AuthController;
import com.chat.view.auth.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 创建 AuthController 对象
        AuthController authController = new AuthController();

        // 1. 创建并显示登录界面
        Stage loginStage = new Stage();
        LoginView loginView = new LoginView(loginStage, authController); // 传递 AuthController
        loginView.show();

        // 登录按钮点击后，模拟登录操作
        // 这里仅做示范，实际操作会在 LoginView 中完成
        // String loginUserId = "user1"; // 假设用户名
        // String loginPassword = "password"; // 假设密码
        // boolean loginSuccess = authController.login(loginUserId, loginPassword);

        // if (loginSuccess) {
        //     System.out.println("Login successful!");
        // } else {
        //     System.out.println("Login failed: Invalid credentials.");
        // }
    }

    public static void main(String[] args) {
        launch(args); // 启动 JavaFX 应用程序
    }
}
