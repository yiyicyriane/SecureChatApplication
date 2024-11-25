package com.chat.controller;

import com.chat.model.User;

import java.util.HashMap; // Used to store registered users
import java.util.Map;

public class AuthController {
    private Map<String, User> registeredUsers; // A map to store user ID and user objects

    // Constructor
    public AuthController() {
        registeredUsers = new HashMap<>();
    }

    /**
     * Register a new user
     *
     * @param user The new user to register
     * @return Whether the registration was successful
     */
    public boolean register(User user) {
        if (registeredUsers.containsKey(user.getUserId())) { // Check if the user ID already exists
            return false; // User ID is already registered
        }
        registeredUsers.put(user.getUserId(), user); // Add new user to the registered users map
        return true; // Registration successful
    }

    /**
     * Sign in a user
     *
     * @param userId   User ID
     * @param password User password
     * @return Whether the sign-in was successful
     */
    public boolean signIn(String userId, String password) {
        User user = registeredUsers.get(userId); // Get the user by ID
        return user != null && user.getPassword().equals(password); // Validate the password
    }

    /**
     * Get a user by their user ID
     *
     * @param userId The ID of the user to retrieve
     * @return The User object associated with the provided user ID, or null if no user is found
     */
    public User getUserById(String userId) {
        return registeredUsers.get(userId); // Return the user associated with the given user ID
    }
}
