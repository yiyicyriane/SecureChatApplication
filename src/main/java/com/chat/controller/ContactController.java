package com.chat.controller;

import com.chat.service.AuthService;

public class ContactController {

    private final AuthService authService;

    public ContactController() throws Exception {
        this.authService = new AuthService(); // Utility class for HTTP requests
    }

    // Method to add a contact
    public String addContact(String userId, String contactId) {
        try {
            // Send HTTP POST request to the backend API
            String response = authService.postNewFriendApplicationSenderIdSet(userId, contactId);
            // Backend response determines the result
            if (response.equals("Friend already exists.") || 
                response.equals("Invalid Friend ID.") || 
                response.equals("Friend application sent.")) {
                return response;
            } else if (response.equals("Friend application accepted.")) {
                return "success";
                // TODO: notice and update contact in view
            } else {
                return "Server error.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error.";
        }
    }
}
