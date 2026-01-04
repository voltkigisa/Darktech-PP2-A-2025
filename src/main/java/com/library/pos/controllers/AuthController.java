package com.library.pos.controllers;

import com.library.pos.dao.UserDAO;
import com.library.pos.models.User;
import com.library.pos.models.UserRole;

import java.util.Optional;

/**
 * Controller for handling authentication operations.
 */
public class AuthController {

    private final UserDAO userDAO;
    private User currentUser;

    public AuthController() {
        this.userDAO = new UserDAO();
        this.currentUser = null;
    }

    /**
     * Attempt to login with the provided credentials.
     * 
     * @param username the username
     * @param password the password
     * @return true if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }

        Optional<User> user = userDAO.authenticate(username.trim(), password);

        if (user.isPresent()) {
            this.currentUser = user.get();
            System.out.println("Login successful for user: " + currentUser.getUsername() +
                    " with role: " + currentUser.getRole());
            return true;
        }

        return false;
    }

    /**
     * Logout the current user.
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getUsername() + " logged out.");
            currentUser = null;
        }
    }

    /**
     * Check if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get the currently logged in user.
     * 
     * @return the current User, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if the current user is an admin.
     * 
     * @return true if current user has ADMIN role
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }

    /**
     * Check if the current user is a manager.
     * 
     * @return true if current user has MANAGER role
     */
    public boolean isManager() {
        return currentUser != null && currentUser.getRole() == UserRole.MANAGER;
    }

    /**
     * Get the role of the current user.
     * 
     * @return the UserRole of the current user, or null if not logged in
     */
    public UserRole getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * Get the name of the current user.
     * 
     * @return the name of the current user, or empty string if not logged in
     */
    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getName() : "";
    }
}
