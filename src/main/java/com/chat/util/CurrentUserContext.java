package com.chat.util;

import com.chat.model.User;

public class CurrentUserContext {
    private static CurrentUserContext instance;
    private User currentUser;

    private CurrentUserContext() {}

    public static synchronized CurrentUserContext getInstance() {
        if (instance == null) {
            instance = new CurrentUserContext();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
