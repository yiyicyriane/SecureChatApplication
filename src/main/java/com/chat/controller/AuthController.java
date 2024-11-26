package com.chat.controller;

import com.chat.model.User;
import com.chat.model.UserServer;
import com.chat.service.AuthService;
import com.chat.util.RSAUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.KeyPair;
import java.security.PublicKey;

@Data
@AllArgsConstructor 
public class AuthController {
    private User registeredUser;
    private UserServer registeredUsersServer;
    private final AuthService authService;
    private KeyPair keyPair;

    // Constructor
    public AuthController() throws Exception {
        authService = new AuthService();
    }

    /**
     * Register a new user
     *
     * @param user The new user to register
     * @return Whether the registration was successful
     */
    public boolean register(User user) {
        // TODO: get information from view
        registeredUser = user;
        // generate a new key pair
        try {
            keyPair = RSAUtil.generateKeyPair();
            String publicKey = RSAUtil.publicKeyToString(keyPair.getPublic());
            registeredUsersServer = new UserServer(registeredUser.getUserId(), registeredUser.getName(), registeredUser.getPassword(), publicKey, registeredUser.getProfilePicture());
            String response = authService.register(registeredUsersServer);
            if (response.equals("User registered."))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sign in a user
     *
     * @param userId   User ID
     * @param password User password
     * @return Whether the sign-in was successful
     */
    public boolean login(String userId, String password) {
        try {
            PublicKey publicKey = RSAUtil.decodePublicKey(authService.getPublicKey(userId));
            String encryptPassword = RSAUtil.encrypt(password, publicKey);
            registeredUsersServer = authService.login(userId, encryptPassword);
            if (registeredUsersServer == null) return false;

            // update public key and encrypted password
            keyPair = RSAUtil.generateKeyPair();
            String newPublicKey = RSAUtil.publicKeyToString(keyPair.getPublic());
            String newEncryptPassword = RSAUtil.encrypt(password, keyPair.getPublic());
            authService.putPassword(userId, encryptPassword, newEncryptPassword, newPublicKey);
            
            registeredUsersServer.setPassword(newEncryptPassword);
            registeredUsersServer.setPublicKey(newPublicKey);
            registeredUser = new User(userId, registeredUsersServer.getUsername(), registeredUsersServer.getPassword(), registeredUsersServer.getProfile());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get a user by their user ID
     *
     * @param userId The ID of the user to retrieve
     * @return The User object associated with the provided user ID, or null if no user is found
     */
    // public User getUserById(String userId) {
    //     return registeredUser.get(userId); // Return the user associated with the given user ID
    // }
}
