package com.library.pos.views.auth;

import com.library.pos.dao.UserDAO;
import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginView - A stylized login screen similar to the provided mockup.
 */
public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox cbShowPassword;
    private UserDAO userDAO;

    public LoginView() {
        this.userDAO = new UserDAO();
        setTitle("Library POS System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                Color c1 = new Color(98, 88, 208); // bluish
                Color c2 = new Color(150, 80, 170); // purple
                GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                // subtle diagonal lines to mimic background pattern (light overlay)
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.06f));
                g2.setColor(Color.WHITE);
                for (int i = -h; i < w; i += 40) {
                    g2.drawLine(i, 0, i + h, h);
                }
                g2.dispose();
            }
        };
        bg.setLayout(new GridBagLayout());

        RoundedPanel card = new RoundedPanel(20, new Color(255, 255, 255));
        card.setPreferredSize(new Dimension(520, 520));
        card.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(36, 40, 40, 40));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel topDecor = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                g2.setColor(new Color(121, 97, 201));
                g2.fillArc((w / 2) - 40, -20, 80, 80, 0, 180);
                g2.dispose();
            }
        };
        topDecor.setOpaque(false);
        topDecor.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitle = new JLabel("Library POS System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(new Color(41, 46, 64));

        JLabel lblSubtitle = new JLabel("Masuk ke akun Anda untuk melanjutkan");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(125, 133, 153));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(topDecor);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(lblTitle);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(lblSubtitle);
        content.add(Box.createRigidArea(new Dimension(0, 26)));


        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(51, 52, 60));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtUsername.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        txtUsername.setForeground(Color.GRAY);
        txtUsername.setText("Masukkan username");
        txtUsername.setHorizontalAlignment(JTextField.LEFT);
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            private boolean isPlaceholder = true;

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (isPlaceholder) {
                    txtUsername.setText("");
                    txtUsername.setForeground(Color.BLACK);
                    txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    isPlaceholder = false;
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtUsername.getText().trim().isEmpty()) {
                    txtUsername.setForeground(Color.GRAY);
                    txtUsername.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    txtUsername.setText("Masukkan username");
                    isPlaceholder = true;
                }
            }
        });

        form.add(lblUser);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(txtUsername);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(51, 52, 60));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (getPassword().length == 0 && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(128, 128, 128, 180));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 14));

                    Insets insets = getInsets();
                    g2.drawString("Masukkan password", insets.left,
                        getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };

        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setHorizontalAlignment(JTextField.LEFT);
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        txtPassword.setEchoChar('\u2022');

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPassword.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPassword.repaint();
            }
        });

        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { txtPassword.repaint(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { txtPassword.repaint(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { txtPassword.repaint(); }
        });

        form.add(lblPass);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(txtPassword);
        form.add(Box.createRigidArea(new Dimension(0, 8)));

        cbShowPassword = new JCheckBox("Tampilkan password");
        cbShowPassword.setOpaque(false);
        cbShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbShowPassword.addActionListener(e -> toggleShowPassword());

        form.add(cbShowPassword);
        form.add(Box.createRigidArea(new Dimension(0, 30)));

        form.add(Box.createVerticalGlue());

        GradientButton btnLogin = new GradientButton("Masuk");
        btnLogin.setPreferredSize(new Dimension(0, 44));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> onLogin());

        content.add(form);
        content.add(btnLogin);

        card.add(content, BorderLayout.CENTER);

        bg.add(card, new GridBagConstraints());

        add(bg, BorderLayout.CENTER);
    }

    private void toggleShowPassword() {
        if (cbShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('\u2022');
        }
        txtPassword.repaint();
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Raw username: '" + txtUsername.getText() + "'");
        System.out.println("Trimmed username: '" + username + "'");
        System.out.println("Password length: " + password.length());
        System.out.println("Password value: '" + password + "'");

        if (username.equals("Masukkan username")) {
            username = "";
            System.out.println("Username was placeholder, cleared");
        }


        System.out.println("Final username: '" + username + "'");
        System.out.println("Final password: '" + password + "'");

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password harus diisi.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            User user = userDAO.authenticate(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this,
                    "Selamat datang, " + user.getName() + "!\nRole: " + user.getRole(),
                    "Login Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);

                dispose();

                SwingUtilities.invokeLater(() -> {
                    if ("ADMIN".equalsIgnoreCase(user.getRole()) || "Administrator".equalsIgnoreCase(user.getRole())) {
                        com.library.pos.views.admin.AdminDashboardView dashboard =
                            new com.library.pos.views.admin.AdminDashboardView(user);
                        dashboard.setVisible(true);
                    } else if ("MANAGER".equalsIgnoreCase(user.getRole())) {
                        com.library.pos.views.manager.ManagerDashboardView dashboard =
                            new com.library.pos.views.manager.ManagerDashboardView(user);
                        dashboard.setVisible(true);
                    }
                });


            } else {
                JOptionPane.showMessageDialog(this,
                    "Username atau password salah!\nSilakan coba lagi.",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);

                txtPassword.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan saat login:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bg) {
            super();
            this.radius = radius;
            this.backgroundColor = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(38, 34, 66, 80));
            for (int i = 0; i < 6; i++) {
                int alpha = 80 - i * 12;
                g2.setColor(new Color(38, 34, 66, Math.max(alpha, 0)));
                g2.fillRoundRect(6 - i, 6 - i, w - (12 - i * 2), h - (12 - i * 2), radius + 6, radius + 6);
            }

            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, w - 12, h - 12, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public Insets getInsets() {
            return new Insets(12, 12, 12, 12);
        }
    }

    static class GradientButton extends JButton {
        public GradientButton(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1 = new Color(93, 138, 241);
            Color c2 = new Color(153, 87, 201);
            GradientPaint gp = new GradientPaint(0, 0, c1, w, 0, c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, 12, 12);

            FontMetrics fm = g2.getFontMetrics();
            Rectangle r = new Rectangle(0, 0, w, h);
            int textY = (r.height - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            int textWidth = fm.stringWidth(getText());
            g2.drawString(getText(), (r.width - textWidth) / 2, textY);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            LoginView lv = new LoginView();
            lv.setVisible(true);
        });
    }
}

