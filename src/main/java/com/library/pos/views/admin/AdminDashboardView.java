package com.library.pos.views.admin;

import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * AdminDashboardView - Main dashboard for Administrator
 */
public class AdminDashboardView extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private JButton selectedMenuButton = null;

    public AdminDashboardView(User user) {
        this.currentUser = user;

        setTitle("Library POS System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 247, 250));

        JPanel header = createHeader();
        rightPanel.add(header, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        showDashboardContent();

        rightPanel.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 750));
        sidebar.setBackground(new Color(31, 41, 55)); // Dark gray
        sidebar.setLayout(new BorderLayout());

        JPanel topSection = new JPanel();
        topSection.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));
        topSection.setBackground(new Color(31, 41, 55));

        JLabel titleLabel = new JLabel("Library POS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        topSection.add(titleLabel);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(31, 41, 55));
        menuPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnDashboard = createMenuButton("", "Dashboard", true);
        JButton btnKelola = createMenuButton("", "Kelola Manager", false);
        JButton btnBuku = createMenuButton("", "Kelola Buku", false);
        JButton btnAnggota = createMenuButton("", "Kelola Anggota", false);
        JButton btnTransaksi = createMenuButton("", "Transaksi", false);
        JButton btnLaporan = createMenuButton("", "Laporan", false);

        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnKelola);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnBuku);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnAnggota);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnTransaksi);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnLaporan);

        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        bottomSection.setBackground(new Color(31, 41, 55));
        bottomSection.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(55, 65, 81)));

        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(99, 102, 241));
                g2.fillOval(0, 0, 45, 45);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String text = "A";
                int x = (45 - fm.stringWidth(text)) / 2;
                int y = ((45 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(45, 45);
            }
        };
        avatarPanel.setOpaque(false);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(31, 41, 55));

        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel(currentUser.getRole());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(156, 163, 175));

        userInfoPanel.add(nameLabel);
        userInfoPanel.add(roleLabel);

        bottomSection.add(avatarPanel);
        bottomSection.add(userInfoPanel);

        sidebar.add(topSection, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomSection, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createMenuButton(String icon, String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(selected ? new Color(99, 102, 241) : new Color(31, 41, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setBorder(new EmptyBorder(10, 30, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (selected) {
            selectedMenuButton = button;
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedMenuButton) {
                    button.setBackground(new Color(55, 65, 81));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != selectedMenuButton) {
                    button.setBackground(new Color(31, 41, 55));
                }
            }
        });

        button.addActionListener(e -> {
            if (selectedMenuButton != null) {
                selectedMenuButton.setBackground(new Color(31, 41, 55));
            }
            button.setBackground(new Color(99, 102, 241));
            selectedMenuButton = button;

            if (text.equals("Dashboard")) {
                showDashboardContent();
            } else {
                showComingSoonContent(text);
            }
        });

        return button;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        header.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton logoutButton = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(239, 68, 68)); // Red
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(220, 38, 38));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(239, 68, 68));
            }
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Close this window and show login again
                dispose();
                SwingUtilities.invokeLater(() -> {
                    com.library.pos.views.auth.LoginView loginView = new com.library.pos.views.auth.LoginView();
                    loginView.setVisible(true);
                });
            }
        });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);

        return header;
    }

    private void showDashboardContent() {
        contentPanel.removeAll();

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(new Color(245, 247, 250));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel welcomeLabel = new JLabel("Selamat Datang, " + currentUser.getRole() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(31, 41, 55));

        JLabel descLabel = new JLabel("Anda login sebagai " + currentUser.getRole() + ". Kelola sistem perpustakaan dengan mudah.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(107, 114, 128));

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(25, 10)));
        welcomePanel.add(descLabel);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        statsPanel.add(createStatCard("", "Total Buku", "0", new Color(59, 130, 246)));

        statsPanel.add(createStatCard("", "Total Anggota", "0", new Color(16, 185, 129)));

        statsPanel.add(createStatCard("", "Peminjaman Aktif", "0", new Color(245, 158, 11)));

        statsPanel.add(createStatCard("", "Total Manager", "1", new Color(139, 92, 246)));

        dashboardPanel.add(welcomePanel);
        dashboardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        dashboardPanel.add(statsPanel);
        dashboardPanel.add(Box.createVerticalGlue());

        contentPanel.add(dashboardPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 20, 25, 20)
        ));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(107, 114, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(valueLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(titleLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void showComingSoonContent(String menuName) {
        contentPanel.removeAll();

        JPanel comingSoonPanel = new JPanel();
        comingSoonPanel.setLayout(new BoxLayout(comingSoonPanel, BoxLayout.Y_AXIS));
        comingSoonPanel.setBackground(Color.WHITE);
        comingSoonPanel.setBorder(new EmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel(menuName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Fitur ini sedang dalam pengembangan");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(107, 114, 128));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        comingSoonPanel.add(titleLabel);
        comingSoonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        comingSoonPanel.add(messageLabel);

        contentPanel.add(comingSoonPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User dummyUser = new User(1, "admin", "", "Administrator", "Administrator");
            AdminDashboardView dashboard = new AdminDashboardView(dummyUser);
            dashboard.setVisible(true);
        });
    }
}

