package com.library.pos.views;

import com.library.pos.controllers.AuthController;
import com.library.pos.controllers.MainController;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium Login view with modern design and responsive layout.
 */
public class LoginView extends JFrame {

    private final MainController mainController;
    private final AuthController authController;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private JCheckBox showPasswordCheckbox;

    // Premium color palette
    private static final Color PRIMARY_GRADIENT_START = new Color(102, 126, 234);
    private static final Color PRIMARY_GRADIENT_END = new Color(118, 75, 162);
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color INPUT_BORDER = new Color(226, 232, 240);
    private static final Color INPUT_FOCUS_BORDER = new Color(102, 126, 234);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BUTTON_PRIMARY = new Color(102, 126, 234);
    private static final Color BUTTON_HOVER = new Color(79, 99, 206);

    public LoginView(MainController mainController, AuthController authController) {
        this.mainController = mainController;
        this.authController = authController;

        initializeUI();
        mainController.setCurrentView(this);
    }

    private void initializeUI() {
        setTitle("Library POS System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 600));
        setSize(600, 700);
        setLocationRelativeTo(null);

        // Main gradient background panel
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Login card panel
        JPanel cardPanel = createLoginCard();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 40, 40, 40);

        mainPanel.add(cardPanel, gbc);
        add(mainPanel);

        setupKeyboardShortcuts();
        setupResponsiveResize();
    }

    private JPanel createLoginCard() {
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                int shadowSize = 20;
                for (int i = 0; i < shadowSize; i++) {
                    float alpha = (float) (shadowSize - i) / shadowSize * 0.1f;
                    g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                    g2d.fill(new RoundRectangle2D.Float(i, i, getWidth() - i * 2, getHeight() - i * 2, 30, 30));
                }

                // Draw card background
                g2d.setColor(CARD_BACKGROUND);
                g2d.fill(new RoundRectangle2D.Float(10, 10, getWidth() - 20, getHeight() - 20, 24, 24));
                g2d.dispose();
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setPreferredSize(new Dimension(420, 520));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Logo/Icon section
        JPanel logoPanel = createLogoPanel();
        cardPanel.add(logoPanel);
        cardPanel.add(Box.createVerticalStrut(15));

        // Header section
        JPanel headerPanel = createHeaderPanel();
        cardPanel.add(headerPanel);
        cardPanel.add(Box.createVerticalStrut(35));

        // Form section
        JPanel formPanel = createFormPanel();
        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalStrut(30));

        // Button section
        JPanel buttonPanel = createButtonPanel();
        cardPanel.add(buttonPanel);
        cardPanel.add(Box.createVerticalStrut(20));

        // Footer section
        JPanel footerPanel = createFooterPanel();
        cardPanel.add(footerPanel);

        return cardPanel;
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Create a circular logo icon
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient circle
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_GRADIENT_START, getWidth(), getHeight(),
                        PRIMARY_GRADIENT_END);
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                // Book icon (simplified)
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                // Draw book shape
                g2d.drawLine(cx, cy - 12, cx, cy + 12);
                g2d.drawArc(cx - 15, cy - 12, 15, 10, 0, 180);
                g2d.drawArc(cx, cy - 12, 15, 10, 0, 180);
                g2d.drawLine(cx - 15, cy - 7, cx - 15, cy + 12);
                g2d.drawLine(cx + 15, cy - 7, cx + 15, cy + 12);
                g2d.drawArc(cx - 15, cy + 7, 15, 10, 180, 180);
                g2d.drawArc(cx, cy + 7, 15, 10, 180, 180);

                g2d.dispose();
            }
        };
        iconCircle.setPreferredSize(new Dimension(70, 70));
        iconCircle.setOpaque(false);

        logoPanel.add(iconCircle);
        return logoPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Library POS System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Masuk ke akun Anda untuk melanjutkan");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(350, 200));

        // Username field
        JLabel usernameLabel = createStyledLabel("Username");
        usernameField = createStyledTextField("Masukkan username");

        // Password field
        JLabel passwordLabel = createStyledLabel("Password");
        passwordField = createStyledPasswordField("Masukkan password");

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Tampilkan password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckbox.setForeground(TEXT_SECONDARY);
        showPasswordCheckbox.setOpaque(false);
        showPasswordCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPasswordCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordCheckbox.setFocusPainted(false);
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('●');
            }
        });

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(showPasswordCheckbox);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(statusLabel);

        return formPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(160, 174, 192));
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2d.dispose();
                }
            }
        };
        styleInputField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(160, 174, 192));
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2d.dispose();
                }
            }
        };
        field.setEchoChar('●');
        styleInputField(field);
        return field;
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.setCaretColor(PRIMARY_GRADIENT_START);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setPreferredSize(new Dimension(300, 48));

        field.setBorder(new RoundedBorder(INPUT_BORDER, 12, 14));

        // Focus effects
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new RoundedBorder(INPUT_FOCUS_BORDER, 12, 14));
                field.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new RoundedBorder(INPUT_BORDER, 12, 14));
                field.repaint();
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton = new JButton("Masuk") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(BUTTON_HOVER.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(BUTTON_HOVER);
                } else {
                    GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_GRADIENT_START, getWidth(), 0,
                            PRIMARY_GRADIENT_END);
                    g2d.setPaint(gradient);
                }

                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };

        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> performLogin());

        buttonPanel.add(loginButton);
        return buttonPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return footerPanel;
    }

    private void setupKeyboardShortcuts() {
        getRootPane().setDefaultButton(loginButton);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearFields");
        getRootPane().getActionMap().put("clearFields", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    private void setupResponsiveResize() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            showError("Username tidak boleh kosong!");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Password tidak boleh kosong!");
            passwordField.requestFocus();
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Memproses...");
        statusLabel.setText(" ");

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
                        mainController.navigateToDashboard();
                    } else {
                        showError("Username atau password salah!");
                        passwordField.setText("");
                        passwordField.requestFocus();
                    }
                } catch (Exception ex) {
                    showError("Terjadi kesalahan saat login!");
                    ex.printStackTrace();
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Masuk");
                }
            }
        };

        worker.execute();
    }

    private void showError(String message) {
        statusLabel.setText("⚠ " + message);
        statusLabel.setForeground(ERROR_COLOR);
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
        usernameField.requestFocus();
    }

    // Custom gradient background panel
    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_GRADIENT_START,
                    getWidth(), getHeight(), PRIMARY_GRADIENT_END);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Add subtle pattern overlay
            g2d.setColor(new Color(255, 255, 255, 10));
            for (int i = 0; i < getWidth(); i += 50) {
                g2d.drawLine(i, 0, i + getHeight(), getHeight());
            }

            g2d.dispose();
        }
    }

    // Custom rounded border
    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final int padding;

        public RoundedBorder(Color color, int radius, int padding) {
            this.color = color;
            this.radius = radius;
            this.padding = padding;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(padding, padding, padding, padding);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = padding;
            return insets;
        }
    }
}
