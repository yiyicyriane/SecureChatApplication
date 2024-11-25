/*Automatically recognizes and manages user status updates,
including setting online and offline status.*/

package com.chat.service;

import java.util.HashMap;
import java.util.Map;

public class UserStatusService {

    // 模拟存储用户的在线状态，使用用户ID作为键，状态作为值
    private Map<String, String> userStatusMap;

    public UserStatusService() {
        // 初始化用户状态存储
        this.userStatusMap = new HashMap<>();
    }

    // 获取用户的当前状态
    public String getUserStatus(String userId) {
        // 如果用户ID存在，则返回该用户的状态；否则返回"离线"
        return userStatusMap.getOrDefault(userId, "Offline");
    }

    // 设置用户的状态
    public void setUserStatus(String userId, String status) {
        // 更新用户状态
        userStatusMap.put(userId, status);
    }

    // 模拟一个用户上线的方法
    public void userOnline(String userId) {
        setUserStatus(userId, "Online");
    }

    // 模拟一个用户下线的方法
    public void userOffline(String userId) {
        setUserStatus(userId, "Offline");
    }

    // 模拟用户忙碌状态的方法
    public void setUserBusy(String userId) {
        setUserStatus(userId, "Busy");
    }
}
