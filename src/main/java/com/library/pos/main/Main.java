package com.library.pos.main;

import com.library.pos.views.auth.LoginView;

import javax.swing.*;

/**
 * Main application entry point.
 */
public class Main {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Show Login screen first
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
