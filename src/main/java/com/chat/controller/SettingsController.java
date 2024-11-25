package com.chat.controller;

import com.chat.model.User;

public class SettingsController {

    private boolean notificationsEnabled = true; // 默认开启通知

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
        // 保存到数据库或配置文件（根据需求实现）
    }

    public void updateUserProfilePicture(User user) {
        // 更新用户头像信息到数据库或后端（根据需求实现）
        System.out.println("Updated profile picture for user: " + user.getUserId());
    }
}
