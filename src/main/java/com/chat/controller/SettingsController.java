package com.chat.controller;

import com.chat.model.User;
import com.chat.util.CurrentUserContext;

public class SettingsController {
/*暂时不展示notification设置的功能。
    private boolean notificationsEnabled = true; // 默认开启通知

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
        // 保存到数据库或配置文件（根据需求实现）
    }
 */

    public User getCurrentUser() {
        return CurrentUserContext.getInstance().getCurrentUser();
    }

    //TODO method to update profile picture, 返回true说明后端更新成功。
    public boolean updateProfilePicture(String string) {
    }
}
