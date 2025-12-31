package com.library.pos.controllers;

import com.library.pos.models.User;
import com.library.pos.views.LoginView;
import com.library.pos.views.MainFrame;

/**
 * Main application controller managing view navigation.
 */
public class MainController {
    
    private final AuthController authController;
    private LoginView loginView;
    private MainFrame mainFrame;
    
    public MainController(AuthController authController) {
        this.authController = authController;
    }
    
    /**
     * Set the login view reference.
     * @param loginView the LoginView instance
     */
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    /**
     * Set the main frame reference.
     * @param mainFrame the MainFrame instance
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    /**
     * Navigate to the main dashboard after successful login.
     */
    public void showDashboard() {
        User user = authController.getCurrentUser();
        if (user == null) {
            return;
        }
        
        if (loginView != null) {
            loginView.dispose();
        }
        
        mainFrame = new MainFrame(this, authController);
        mainFrame.setVisible(true);
    }
    
    /**
     * Handle logout and return to login screen.
     */
    public void handleLogout() {
        authController.logout();
        
        if (mainFrame != null) {
            mainFrame.dispose();
        }
        
        loginView = new LoginView(this, authController);
        loginView.setVisible(true);
    }
    
    /**
     * Get the auth controller.
     * @return the AuthController instance
     */
    public AuthController getAuthController() {
        return authController;
    }
}
