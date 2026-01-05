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
    private final User currentUser;
    private JPanel contentPanel;
    private JButton selectedMenuButton = null;

    public AdminDashboardView(User user) {
        this.currentUser = user;

        setTitle("Library POS System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
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
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        showDashboardContent();

        rightPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setBackground(new Color(31, 41, 55));
        sidebar.setLayout(new BorderLayout());

        // Top Section - Logo
        JPanel topSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        topSection.setBackground(new Color(31, 41, 55));

        JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Library POS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        topSection.add(iconLabel);
        topSection.add(titleLabel);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(31, 41, 55));
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnDashboard = createMenuButton("", "Dashboard", true);
        JButton btnKelolaMgr = createMenuButton("", "Manage Managers", false);
        JButton btnCategories = createMenuButton("", "Categories", false);
        JButton btnBuku = createMenuButton("", "Manage Books", false);
        JButton btnAnggota = createMenuButton("", "Manage Members", false);
        JButton btnTransaksi = createMenuButton("", "Transactions", false);
        JButton btnLaporan = createMenuButton("", "Reports", false);

        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnKelolaMgr);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnCategories);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnBuku);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnAnggota);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnTransaksi);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(btnLaporan);

        // Bottom Section - User Profile
        JPanel bottomSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
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
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
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
        JButton button = new JButton(icon + "  " + text);
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

            switch (text) {
                case "Dashboard":
                    showDashboardContent();
                    break;
                case "Manage Managers":
                    showKelolaManagerContent();
                    break;
                case "Categories":
                    showCategoriesContent();
                    break;
                case "Manage Books":
                    showKelolaBukuContent();
                    break;
                case "Manage Members":
                    showKelolaAnggotaContent();
                    break;
                case "Transactions":
                    showTransaksiContent();
                    break;
                case "Reports":
                    showLaporanContent();
                    break;
                default:
                    showComingSoonContent(text);
                    break;
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

        JButton logoutButton = createStyledButton("Logout", new Color(239, 68, 68));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
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

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String txt = getText();
                int x = (getWidth() - fm.stringWidth(txt)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(txt, x, y);
                g2.dispose();
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Dynamic width based on text, minimum 100px
        int width = Math.max(180, text.length() * 10);
        button.setPreferredSize(new Dimension(width, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void showDashboardContent() {
        contentPanel.removeAll();

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setBackground(new Color(245, 247, 250));

        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Convert role to friendly display name
        String displayRole = currentUser.getRole().equals("ADMIN") ? "Administrator" : currentUser.getRole();

        JLabel welcomeLabel = new JLabel("Welcome, " + displayRole + "! ");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(31, 41, 55));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("You are logged in as " + displayRole + ". Manage the library system with ease.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(20, 10)));
        welcomePanel.add(descLabel);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        statsPanel.add(createStatCard("", "Total Books", "0", new Color(59, 130, 246)));
        statsPanel.add(createStatCard("", "Total Members", "0", new Color(16, 185, 129)));
        statsPanel.add(createStatCard("", "Active Loans", "0", new Color(245, 158, 11)));
        statsPanel.add(createStatCard("", "Total Managers", "1", new Color(139, 92, 246)));

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
            new EmptyBorder(30, 25, 30, 25)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));

        topPanel.add(iconLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(107, 114, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(valueLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(titleLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(Box.createRigidArea(new Dimension(0, 15)), BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showKelolaManagerContent() {
        contentPanel.removeAll();

        // Directly embed the user management panel
        com.library.pos.views.admin.users.IndexUserView userPanel = 
            new com.library.pos.views.admin.users.IndexUserView();

        contentPanel.add(userPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showKelolaBukuContent() {
        contentPanel.removeAll();

        JPanel bukuPanel = new JPanel();
        bukuPanel.setLayout(new BoxLayout(bukuPanel, BoxLayout.Y_AXIS));
        bukuPanel.setBackground(Color.WHITE);
        bukuPanel.setBorder(new EmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("Manage Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Book management feature is under development");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(107, 114, 128));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bukuPanel.add(titleLabel);
        bukuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bukuPanel.add(messageLabel);

        contentPanel.add(bukuPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showKelolaAnggotaContent() {
    contentPanel.removeAll(); // Hapus tulisan "sedang dalam pengembangan"

    // Panggil tampilan KelolaMemberView yang sudah kita perbaiki tadi
    com.library.pos.views.admin.member.KelolaMemberView memberView = 
        new com.library.pos.views.admin.member.KelolaMemberView();

    contentPanel.add(memberView, BorderLayout.CENTER); // Masukkan form & tabel ke dashboard
    
    contentPanel.revalidate();
    contentPanel.repaint();
}

    private void showTransaksiContent() {
        contentPanel.removeAll();

        JPanel transaksiPanel = new JPanel();
        transaksiPanel.setLayout(new BoxLayout(transaksiPanel, BoxLayout.Y_AXIS));
        transaksiPanel.setBackground(Color.WHITE);
        transaksiPanel.setBorder(new EmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Loan transaction feature is under development");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(107, 114, 128));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        transaksiPanel.add(titleLabel);
        transaksiPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        transaksiPanel.add(messageLabel);

        contentPanel.add(transaksiPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCategoriesContent() {
        contentPanel.removeAll();

        JPanel categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));
        categoriesPanel.setBackground(Color.WHITE);
        categoriesPanel.setBorder(new EmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("Categories");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Category management feature is under development");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(107, 114, 128));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        categoriesPanel.add(titleLabel);
        categoriesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        categoriesPanel.add(messageLabel);

        contentPanel.add(categoriesPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLaporanContent() {
        contentPanel.removeAll();

        JPanel laporanPanel = new JPanel();
        laporanPanel.setLayout(new BoxLayout(laporanPanel, BoxLayout.Y_AXIS));
        laporanPanel.setBackground(Color.WHITE);
        laporanPanel.setBorder(new EmptyBorder(100, 50, 100, 50));

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Report feature is under development");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(107, 114, 128));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        laporanPanel.add(titleLabel);
        laporanPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        laporanPanel.add(messageLabel);

        contentPanel.add(laporanPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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

        JLabel messageLabel = new JLabel("This feature is under development");
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

