package com.library.pos.controllers;

import com.library.pos.models.UserRole;
import com.library.pos.views.admin.AdminDashboardView;
import com.library.pos.views.manager.ManagerDashboardView;
import com.library.pos.views.LoginView;

import javax.swing.*;

/**
 * Main controller for managing navigation between views.
 */
public class MainController {

    private final AuthController authController;
    private JFrame currentView;

    public MainController(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Navigate to the appropriate dashboard based on user role.
     */
    public void navigateToDashboard() {
        if (!authController.isLoggedIn()) {
            System.err.println("Cannot navigate to dashboard: User not logged in");
            return;
        }

        UserRole role = authController.getCurrentUserRole();

        // Close current view if exists
        if (currentView != null) {
            currentView.dispose();
        }

        switch (role) {
            case ADMIN:
                navigateToAdminDashboard();
                break;
            case MANAGER:
                navigateToManagerDashboard();
                break;
            default:
                System.err.println("Unknown role: " + role);
        }
    }

    /**
     * Navigate to Admin Dashboard.
     */
    private void navigateToAdminDashboard() {
        AdminDashboardView adminView = new AdminDashboardView(this, authController);
        adminView.setVisible(true);
        currentView = adminView;
        System.out.println("Navigated to Admin Dashboard");
    }

    /**
     * Navigate to Manager Dashboard.
     */
    private void navigateToManagerDashboard() {
        ManagerDashboardView managerView = new ManagerDashboardView(this, authController);
        managerView.setVisible(true);
        currentView = managerView;
        System.out.println("Navigated to Manager Dashboard");
    }

    /**
     * Navigate back to Login view (logout).
     */
    public void navigateToLogin() {
        // Logout the current user
        authController.logout();

        // Close current view if exists
        if (currentView != null) {
            currentView.dispose();
        }

        // Show login view
        LoginView loginView = new LoginView(this, authController);
        loginView.setVisible(true);
        currentView = loginView;
        System.out.println("Navigated to Login");
    }

    /**
     * Get the AuthController instance.
     * 
     * @return the AuthController
     */
    public AuthController getAuthController() {
        return authController;
    }

    /**
     * Set the current view reference.
     * 
     * @param view the current JFrame view
     */
    public void setCurrentView(JFrame view) {
        this.currentView = view;
    }

    /**
     * Get the current view.
     * 
     * @return the current JFrame view
     */
    public JFrame getCurrentView() {
        return currentView;
    }
}
