package com.library.pos.main;

import com.library.pos.controllers.AuthController;
import com.library.pos.controllers.MainController;
import com.library.pos.views.LoginView;

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
            // Initialize controllers
            AuthController authController = new AuthController();
            MainController mainController = new MainController(authController);
            
            // Create and show login view
            LoginView loginView = new LoginView(mainController, authController);
            loginView.setVisible(true);
        });
    }
}
