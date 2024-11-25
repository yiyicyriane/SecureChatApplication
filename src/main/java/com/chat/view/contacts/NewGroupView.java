package com.chat.view.contacts;

import com.chat.model.Group;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class NewGroupView extends Application {
    private final Set<String> existingGroupIds = new HashSet<>(); // 存储已存在的群组ID，用于唯一性校验
    private Consumer<String> onGroupCreated; // 创建群组成功后的回调函数

    @Override
    public void start(Stage primaryStage) throws Exception {
        start(primaryStage, null);
    }

    public void start(Stage primaryStage, Consumer<String> onGroupCreated) {
        this.onGroupCreated = onGroupCreated;

        // 创建根布局
        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        // 群组ID输入框
        Label groupIdLabel = new Label("Enter Group ID (must start with 'G'):");
        TextField groupIdField = new TextField();
        groupIdField.setPromptText("e.g., G12345");

        // 群组名称输入框
        Label groupNameLabel = new Label("Enter Group Name:");
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Enter your group name");

        // 创建按钮
        Button createButton = new Button("Create Group");
        createButton.setOnAction(e -> {
            String groupId = groupIdField.getText().trim();
            String groupName = groupNameField.getText().trim();

            // 验证群组ID
            if (!validateGroupId(groupId)) {
                showAlert("Invalid Group ID", "Group ID must start with 'G' and be unique.");
                return;
            }

            // 验证群组名称
            if (groupName.isEmpty()) {
                showAlert("Invalid Group Name", "Group name cannot be empty.");
                return;
            }

            // 模拟群组创建
            String adminUserId = "admin123"; // 当前用户的userId，可替换为实际登录用户
            Group newGroup = new Group(groupId, groupName, adminUserId, List.of(adminUserId));
            existingGroupIds.add(groupId);

            // 调用回调函数传递新群组ID
            if (onGroupCreated != null) {
                onGroupCreated.accept(groupId);
            }

            // 显示成功提示框
            showAlert("Group Created",
                    "You have successfully created a new group: " +
                            groupName + "\nGroup ID: " + groupId +
                            "\nPlease copy the Group ID and share it with your friends to join.");

            // 关闭窗口
            primaryStage.close();
        });

        root.getChildren().addAll(groupIdLabel, groupIdField, groupNameLabel, groupNameField, createButton);

        // 设置场景
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Group");
        primaryStage.show();
    }

    // 验证群组ID
    private boolean validateGroupId(String groupId) {
        return groupId.startsWith("G") && !existingGroupIds.contains(groupId) && groupId.length() > 1;
    }

    // 显示提示框
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
