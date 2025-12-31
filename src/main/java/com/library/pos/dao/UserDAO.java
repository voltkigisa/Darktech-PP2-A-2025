package com.library.pos.dao;

import com.library.pos.models.User;

/**
 * Data Access Object interface for User operations.
 */
public interface UserDAO {
    
    /**
     * Authenticate a user with username and password.
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     */
    User authenticate(String username, String password);
    
    /**
     * Find a user by their username.
     * @param username the username to search for
     * @return User object if found, null otherwise
     */
    User findByUsername(String username);
    
    /**
     * Find a user by their ID.
     * @param id the user ID
     * @return User object if found, null otherwise
     */
    User findById(int id);
}
