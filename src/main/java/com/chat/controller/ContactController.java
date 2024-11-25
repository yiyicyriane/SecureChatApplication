package com.chat.controller;

import com.chat.util.ApiClient;

public class ContactController {

    private final ApiClient apiClient;

    public ContactController() {
        this.apiClient = new ApiClient(); // Utility class for HTTP requests
    }

    // Method to add a contact
    public String addContact(String userId, String contactId) {
        try {
            // Send HTTP POST request to the backend API
            String url = "/api/auth/" + userId;
            String response = apiClient.sendPostRequest(url, contactId);

            // Backend response determines the result
            if (response.equals("Friend already exists.")) {
                return "Friend already exists.";
            } else if (response.equals("Invalid Friend ID.")) {
                return "Invalid Friend ID.";
            } else if (response.equals("Friend application accepted.")) {
                return "success";
            } else {
                return "Server error.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error.";
        }
    }
}
