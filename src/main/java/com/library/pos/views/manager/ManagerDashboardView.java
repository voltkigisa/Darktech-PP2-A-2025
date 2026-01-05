package com.library.pos.views.manager;

import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ManagerDashboardView - Main dashboard for Manager
 */
public class ManagerDashboardView extends JFrame {
    private User currentUser;
    private JPanel contentPanel;

    public ManagerDashboardView(User user) {
        this.currentUser = user;

        setTitle("Library POS System - Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        showDashboardContent();

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(23, 120, 217)); // Blue background
        header.setBorder(new EmptyBorder(20, 40, 20, 40));
        header.setPreferredSize(new Dimension(0, 75));

        JLabel titleLabel = new JLabel("Manager Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Selamat datang, Library Manager");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel userBadge = new JLabel("[MANAGER]");
        userBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userBadge.setForeground(new Color(23, 120, 217));
        userBadge.setOpaque(true);
        userBadge.setBackground(Color.WHITE);
        userBadge.setBorder(new EmptyBorder(5, 12, 5, 12));

        JButton logoutButton = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
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
        logoutButton.setPreferredSize(new Dimension(90, 35));
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
                dispose();
                SwingUtilities.invokeLater(() -> {
                    com.library.pos.views.auth.LoginView loginView = new com.library.pos.views.auth.LoginView();
                    loginView.setVisible(true);
                });
            }
        });

        rightPanel.add(welcomeLabel);
        rightPanel.add(userBadge);
        rightPanel.add(logoutButton);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

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
        welcomePanel.setBorder(new EmptyBorder(35, 40, 35, 40));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel welcomeTitle = new JLabel("Selamat Datang di Library POS System!");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeTitle.setForeground(new Color(31, 41, 55));

        JLabel welcomeDesc = new JLabel("Anda login sebagai Manager. Anda dapat mengelola transaksi peminjaman dan pengembalian buku.");
        welcomeDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeDesc.setForeground(new Color(107, 114, 128));

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createRigidArea(new Dimension(3, 12)));
        welcomePanel.add(welcomeDesc);

        JPanel cardsContainer = new JPanel(new GridBagLayout());
        cardsContainer.setBackground(new Color(245, 247, 250));
        cardsContainer.setBorder(new EmptyBorder(30, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        cardsContainer.add(createFeatureCard("Lihat Buku", "Lihat daftar buku yang tersedia", new Color(23, 120, 217), ""), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        cardsContainer.add(createFeatureCard("Lihat Anggota", "Lihat data anggota perpustakaan", new Color(16, 185, 129), ""), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cardsContainer.add(createFeatureCard("Transaksi", "Peminjaman dan pengembalian buku", new Color(245, 158, 11), ""), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        cardsContainer.add(createFeatureCard("Laporan", "Lihat laporan transaksi", new Color(23, 162, 184), ""), gbc);

        dashboardPanel.add(welcomePanel);
        dashboardPanel.add(cardsContainer);

        contentPanel.add(dashboardPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createFeatureCard(String title, String description, Color accentColor, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 25, 30, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(550, 200));

        JPanel contentPnl = new JPanel();
        contentPnl.setLayout(new BoxLayout(contentPnl, BoxLayout.Y_AXIS));
        contentPnl.setBackground(Color.WHITE);

        JPanel shortBar = new JPanel();
        shortBar.setBackground(accentColor);
        shortBar.setPreferredSize(new Dimension(50, 4));
        shortBar.setMaximumSize(new Dimension(50, 4));
        shortBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPnl.add(shortBar);
        contentPnl.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPnl.add(titleLabel);
        contentPnl.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPnl.add(descLabel);

        card.add(contentPnl, BorderLayout.CENTER);

        final Color originalBg = Color.WHITE;
        final Color hoverBg = new Color(249, 250, 251);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBg);
                contentPnl.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalBg);
                contentPnl.setBackground(originalBg);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navigateToPage(title);
            }
        });

        return card;
    }

    private void navigateToPage(String pageName) {
        switch (pageName) {
            case "Lihat Buku":
                showLihatBukuContent();
                break;
            case "Lihat Anggota":
                showLihatAnggotaContent();
                break;
            case "Transaksi":
                showTransaksiContent();
                break;
            case "Laporan":
                showLaporanContent();
                break;
            default:
                showDashboardContent();
                break;
        }
    }

    private void showLihatBukuContent() {
        contentPanel.removeAll();

        JPanel bukuPanel = new JPanel(new BorderLayout());
        bukuPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Lihat Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton backButton = createBackButton();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel descLabel = new JLabel("Halaman untuk melihat daftar buku yang tersedia di perpustakaan.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(descLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel infoLabel = new JLabel("Fitur ini sedang dalam pengembangan...");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(156, 163, 175));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(infoLabel);

        bukuPanel.add(headerPanel, BorderLayout.NORTH);
        bukuPanel.add(content, BorderLayout.CENTER);

        contentPanel.add(bukuPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLihatAnggotaContent() {
        contentPanel.removeAll();

        JPanel anggotaPanel = new JPanel(new BorderLayout());
        anggotaPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Lihat Anggota");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton backButton = createBackButton();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel descLabel = new JLabel("Halaman untuk melihat data anggota perpustakaan.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(descLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel infoLabel = new JLabel("Fitur ini sedang dalam pengembangan...");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(156, 163, 175));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(infoLabel);

        anggotaPanel.add(headerPanel, BorderLayout.NORTH);
        anggotaPanel.add(content, BorderLayout.CENTER);

        contentPanel.add(anggotaPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showTransaksiContent() {
        contentPanel.removeAll();

        JPanel transaksiPanel = new JPanel(new BorderLayout());
        transaksiPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Transaksi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton backButton = createBackButton();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel descLabel = new JLabel("Halaman untuk mengelola peminjaman dan pengembalian buku.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(descLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel infoLabel = new JLabel("Fitur ini sedang dalam pengembangan...");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(156, 163, 175));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(infoLabel);

        transaksiPanel.add(headerPanel, BorderLayout.NORTH);
        transaksiPanel.add(content, BorderLayout.CENTER);

        contentPanel.add(transaksiPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLaporanContent() {
        contentPanel.removeAll();

        JPanel laporanPanel = new JPanel(new BorderLayout());
        laporanPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Laporan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(31, 41, 55));

        JButton backButton = createBackButton();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel descLabel = new JLabel("Halaman untuk melihat laporan transaksi.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(descLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel infoLabel = new JLabel("Fitur ini sedang dalam pengembangan...");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(156, 163, 175));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(infoLabel);

        laporanPanel.add(headerPanel, BorderLayout.NORTH);
        laporanPanel.add(content, BorderLayout.CENTER);

        contentPanel.add(laporanPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("← Kembali ke Dashboard") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(107, 114, 128));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setPreferredSize(new Dimension(200, 35));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(75, 85, 99));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(107, 114, 128));
            }
        });

        backButton.addActionListener(e -> showDashboardContent());

        return backButton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User dummyUser = new User(2, "manager", "", "Library Manager", "MANAGER");
            ManagerDashboardView dashboard = new ManagerDashboardView(dummyUser);
            dashboard.setVisible(true);
        });
    }
}

