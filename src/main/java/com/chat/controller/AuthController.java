package com.chat.controller;

import com.chat.model.User;

import java.util.HashMap; // 用于存储注册用户的 HashMap
import java.util.Map;

public class AuthController {
    private Map<String, User> registeredUsers; // 存储用户ID和用户对象的映射

    // 构造方法
    public AuthController() {
        registeredUsers = new HashMap<>();
    }

    /**
     * 注册新用户
     *
     * @param user 要注册的新用户
     * @return 注册是否成功
     */
    public boolean register(User user) {
        if (registeredUsers.containsKey(user.getUserId())) { // 检查用户ID是否已存在
            return false; // 用户ID已被注册
        }
        registeredUsers.put(user.getUserId(), user); // 将新用户加入注册用户列表
        return true; // 注册成功
    }

    /**
     * 登录
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return 登录是否成功
     */
    public boolean signIn(String userId, String password) {
        User user = registeredUsers.get(userId); // 根据用户ID查找用户
        return user != null && user.getPassword().equals(password); // 验证密码是否匹配
    }
}
