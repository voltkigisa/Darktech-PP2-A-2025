package com.library.pos.controllers;

import com.library.pos.dao.UserDAO;
import com.library.pos.dao.UserDAOImpl;
import com.library.pos.models.User;

/**
 * Controller handling user authentication.
 */
public class AuthController {
    
    private final UserDAO userDAO;
    private User currentUser;
    
    public AuthController() {
        this.userDAO = new UserDAOImpl();
    }
    
    /**
     * Attempt to log in a user with the given credentials.
     * @param username the username
     * @param password the password
     * @return true if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }
        
        User user = userDAO.authenticate(username.trim(), password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    /**
     * Log out the current user.
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Get the currently logged-in user.
     * @return the current User, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if a user is currently logged in.
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
