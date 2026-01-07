package com.library.pos.controllers;

import com.library.pos.dao.UserDAO;
import com.library.pos.models.User;

import javax.swing.*;
import java.util.List;

/**
 * Controller for User management (Admin only)
 */
public class AdminUserController {
    private UserDAO userDAO;

    public AdminUserController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    /**
     * Get user by ID
     */
    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    /**
     * Create new user with validation
     */
    public boolean createUser(String username, String password, String name, String role) {
        // Validate input
        String validationError = validateUserInput(username, password, name, role, 0);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if username already exists
        if (userDAO.getByUsername(username) != null) {
            JOptionPane.showMessageDialog(null, "Username sudah terdaftar!", "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Create user
        User user = new User(username, password, name, role);
        boolean success = userDAO.create(user);

        if (success) {
            JOptionPane.showMessageDialog(null, "User berhasil ditambahkan!", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan user!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return success;
    }

    /**
     * Update existing user with validation
     */
    public boolean updateUser(int id, String username, String password, String name, String role) {
        // Validate input
        String validationError = validateUserInput(username, password, name, role, id);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if username exists (excluding current user)
        if (userDAO.usernameExists(username, id)) {
            JOptionPane.showMessageDialog(null, "Username sudah digunakan oleh user lain!", "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Update user
        User user = new User(id, username, password, name, role);
        boolean success = userDAO.update(user);

        if (success) {
            JOptionPane.showMessageDialog(null, "Data user berhasil diupdate!", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengupdate data user!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return success;
    }

    /**
     * Delete user with confirmation
     */
    public boolean deleteUser(int id) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus user ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userDAO.delete(id);

            if (success) {
                JOptionPane.showMessageDialog(null, "User berhasil dihapus!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus user!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            return success;
        }

        return false;
    }

    /**
     * Validate user input fields
     */
    private String validateUserInput(String username, String password, String name, String role, int excludeId) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            return "Username tidak boleh kosong!";
        }
        if (username.trim().length() < 3) {
            return "Username minimal 3 karakter!";
        }
        if (username.trim().length() > 50) {
            return "Username maksimal 50 karakter!";
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username hanya boleh huruf, angka, dan underscore!";
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            return "Password tidak boleh kosong!";
        }
        if (password.length() < 6) {
            return "Password minimal 6 karakter!";
        }
        if (password.length() > 255) {
            return "Password maksimal 255 karakter!";
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            return "Nama tidak boleh kosong!";
        }
        if (name.trim().length() < 3) {
            return "Nama minimal 3 karakter!";
        }
        if (name.trim().length() > 100) {
            return "Nama maksimal 100 karakter!";
        }

        // Validate role
        if (role == null || role.trim().isEmpty()) {
            return "Role harus dipilih!";
        }
        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            return "Role harus 'ADMIN' atau 'MANAGER'!";
        }

        return null; // No validation errors
    }
}
