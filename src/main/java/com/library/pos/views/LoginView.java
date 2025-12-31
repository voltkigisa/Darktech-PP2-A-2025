package com.library.pos.views;

import com.library.pos.controllers.AuthController;
import com.library.pos.controllers.MainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Login view with modern dark theme design.
 */
public class LoginView extends JFrame {
    
    private final MainController mainController;
    private final AuthController authController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;
    
    // Colors
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);     // Indigo
    private static final Color PRIMARY_HOVER = new Color(99, 90, 249);
    private static final Color BACKGROUND_DARK = new Color(17, 24, 39);    // Dark gray
    private static final Color CARD_BACKGROUND = new Color(31, 41, 55);    // Slightly lighter
    private static final Color TEXT_PRIMARY = new Color(243, 244, 246);    // Light gray
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);  // Medium gray
    private static final Color INPUT_BACKGROUND = new Color(55, 65, 81);   // Input background
    private static final Color ERROR_COLOR = new Color(239, 68, 68);       // Red
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);     // Green
    
    public LoginView(MainController mainController, AuthController authController) {
        this.mainController = mainController;
        this.authController = authController;
        
        mainController.setLoginView(this);
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library POS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, BACKGROUND_DARK, 
                        getWidth(), getHeight(), new Color(30, 41, 59));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Login card panel
        JPanel cardPanel = createCardPanel();
        mainPanel.add(cardPanel);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(380, 450));
        
        // Logo/Icon
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel("Library POS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JPanel usernamePanel = createInputPanel("Username", false);
        usernameField = (JTextField) usernamePanel.getClientProperty("field");
        
        // Password field
        JPanel passwordPanel = createInputPanel("Password", true);
        passwordField = (JPasswordField) passwordPanel.getClientProperty("field");
        
        // Message label for errors
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(ERROR_COLOR);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login button
        loginButton = createStyledButton("Sign In");
        loginButton.addActionListener(e -> handleLogin());
        
        // Add key listener for Enter key
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        
        // Add components with spacing
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(30));
        card.add(usernamePanel);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordPanel);
        card.add(Box.createVerticalStrut(10));
        card.add(messageLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        
        return card;
    }
    
    private JPanel createInputPanel(String labelText, boolean isPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 70));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField field;
        if (isPassword) {
            field = new JPasswordField(20);
        } else {
            field = new JTextField(20);
        }
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(INPUT_BACKGROUND);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 85, 99), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setMaximumSize(new Dimension(300, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(75, 85, 99), 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        panel.putClientProperty("field", field);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(PRIMARY_HOVER);
                } else {
                    g2d.setColor(PRIMARY_COLOR);
                }
                
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter username and password", false);
            return;
        }
        
        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return authController.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        showMessage("Login successful!", true);
                        mainController.showDashboard();
                    } else {
                        showMessage("Invalid username or password", false);
                        loginButton.setEnabled(true);
                        loginButton.setText("Sign In");
                        passwordField.setText("");
                    }
                } catch (Exception e) {
                    showMessage("Connection error. Please try again.", false);
                    loginButton.setEnabled(true);
                    loginButton.setText("Sign In");
                }
            }
        };
        worker.execute();
    }
    
    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.setForeground(isSuccess ? SUCCESS_COLOR : ERROR_COLOR);
    }
}
