package com.library.pos.views;

import com.library.pos.controllers.AuthController;
import com.library.pos.controllers.MainController;
import com.library.pos.models.Role;
import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Main application frame with sidebar navigation and content panels.
 */
public class MainFrame extends JFrame {
    
    private final MainController mainController;
    private final AuthController authController;
    private final User currentUser;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    
    // Colors
    private static final Color SIDEBAR_COLOR = new Color(17, 24, 39);
    private static final Color SIDEBAR_HOVER = new Color(31, 41, 55);
    private static final Color CONTENT_BACKGROUND = new Color(243, 244, 246);
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private static final Color TEXT_DARK = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);
    
    public MainFrame(MainController mainController, AuthController authController) {
        this.mainController = mainController;
        this.authController = authController;
        this.currentUser = authController.getCurrentUser();
        
        mainController.setMainFrame(this);
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library POS - " + currentUser.getRole().getDisplayName() + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CONTENT_BACKGROUND);
        
        // Create sidebar
        sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Create content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BACKGROUND);
        
        // Add dashboard panels based on role
        if (currentUser.getRole() == Role.ADMIN) {
            contentPanel.add(new AdminDashboardPanel(currentUser), "dashboard");
        } else {
            contentPanel.add(new ManagerDashboardPanel(currentUser), "dashboard");
        }
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Logo section
        JPanel logoPanel = createLogoPanel();
        sidebar.add(logoPanel);
        
        // Divider
        sidebar.add(createDivider());
        
        // Navigation items
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(SIDEBAR_COLOR);
        navPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        navPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Dashboard menu item (active by default)
        JPanel dashboardItem = createNavItem("📊", "Dashboard", true);
        dashboardItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentPanel, "dashboard");
            }
        });
        navPanel.add(dashboardItem);
        navPanel.add(Box.createVerticalStrut(5));
        
        // Role-specific menu items (placeholders for future features)
        if (currentUser.getRole() == Role.ADMIN) {
            navPanel.add(createNavItem("👥", "User Management", false));
            navPanel.add(Box.createVerticalStrut(5));
            navPanel.add(createNavItem("📚", "Books", false));
            navPanel.add(Box.createVerticalStrut(5));
            navPanel.add(createNavItem("📋", "Reports", false));
        } else {
            navPanel.add(createNavItem("📚", "Books", false));
            navPanel.add(Box.createVerticalStrut(5));
            navPanel.add(createNavItem("🔄", "Lending", false));
            navPanel.add(Box.createVerticalStrut(5));
            navPanel.add(createNavItem("↩️", "Returns", false));
        }
        
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(createNavItem("⚙️", "Settings", false));
        
        sidebar.add(navPanel);
        
        // Spacer to push user info to bottom
        sidebar.add(Box.createVerticalGlue());
        
        // Divider before user section
        sidebar.add(createDivider());
        
        // User info section
        JPanel userPanel = createUserPanel();
        sidebar.add(userPanel);
        
        return sidebar;
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(SIDEBAR_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setMaximumSize(new Dimension(260, 80));
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("Library POS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(titleLabel);
        panel.add(Box.createHorizontalGlue());
        
        return panel;
    }
    
    private JSeparator createDivider() {
        JSeparator divider = new JSeparator();
        divider.setForeground(new Color(55, 65, 81));
        divider.setBackground(SIDEBAR_COLOR);
        divider.setMaximumSize(new Dimension(260, 1));
        return divider;
    }
    
    private JPanel createNavItem(String icon, String text, boolean isActive) {
        JPanel item = new JPanel() {
            private boolean hovered = false;
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isActive) {
                    g2d.setColor(PRIMARY_COLOR);
                } else if (hovered) {
                    g2d.setColor(SIDEBAR_HOVER);
                } else {
                    g2d.setColor(SIDEBAR_COLOR);
                }
                
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        
        item.setLayout(new BoxLayout(item, BoxLayout.X_AXIS));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(12, 15, 12, 15));
        item.setMaximumSize(new Dimension(230, 45));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", isActive ? Font.BOLD : Font.PLAIN, 14));
        textLabel.setForeground(isActive ? Color.WHITE : TEXT_SECONDARY);
        
        item.add(iconLabel);
        item.add(Box.createHorizontalStrut(12));
        item.add(textLabel);
        item.add(Box.createHorizontalGlue());
        
        return item;
    }
    
    private JPanel createUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(SIDEBAR_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 20, 20));
        panel.setMaximumSize(new Dimension(260, 80));
        
        // Avatar
        JLabel avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillOval(0, 0, 40, 40);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initial = currentUser.getName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (40 - fm.stringWidth(initial)) / 2;
                int y = (40 + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(initial, x, y);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(40, 40));
        avatarLabel.setMaximumSize(new Dimension(40, 40));
        
        // User info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(SIDEBAR_COLOR);
        
        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_PRIMARY);
        
        JLabel roleLabel = new JLabel(currentUser.getRole().getDisplayName());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(TEXT_SECONDARY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);
        
        // Logout button
        JButton logoutBtn = new JButton("↪") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2d.setColor(SIDEBAR_HOVER);
                } else {
                    g2d.setColor(SIDEBAR_COLOR);
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                super.paintComponent(g);
            }
        };
        logoutBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        logoutBtn.setForeground(TEXT_SECONDARY);
        logoutBtn.setPreferredSize(new Dimension(35, 35));
        logoutBtn.setMaximumSize(new Dimension(35, 35));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("Logout");
        logoutBtn.addActionListener(e -> handleLogout());
        
        panel.add(avatarLabel);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(infoPanel);
        panel.add(Box.createHorizontalGlue());
        panel.add(logoutBtn);
        
        return panel;
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            mainController.handleLogout();
        }
    }
}
