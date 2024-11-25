package com.chat.view.chat;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChatWindowView extends Application {
    private String chatRoomId; // 存储聊天对象的ID

    public ChatWindowView(String chatRoomId) {
        this.chatRoomId = chatRoomId; // 构造函数接收并存储聊天ID
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 根据 chatId 加载聊天内容
        System.out.println("Opening chat window for chat ID: " + chatRoomId);

        // 你的代码：根据 chatId 获取聊天数据并展示
        // 这里你可以调用方法加载个人聊天或者群组聊天
    }
}
