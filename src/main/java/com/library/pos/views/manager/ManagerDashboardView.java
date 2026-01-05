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
    private final User currentUser;
    private JPanel contentPanel;

    public ManagerDashboardView(User user) {
        this.currentUser = user;

        setTitle("Library POS System - Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Content Panel (will be replaced when navigating)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));

        // Show dashboard by default
        showDashboard();

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 123, 255)); // Blue header
        header.setBorder(new EmptyBorder(20, 40, 20, 40));
        header.setPreferredSize(new Dimension(0, 80));

        // Left side - Title
        JLabel titleLabel = new JLabel("Manager Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        // Center - Greeting
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);

        JLabel greetingLabel = new JLabel("Welcome, Library Manager");
        greetingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        greetingLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel("[MANAGER]");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        roleLabel.setForeground(new Color(255, 255, 255, 220));

        centerPanel.add(greetingLabel);
        centerPanel.add(roleLabel);

        // Right side - Logout button
        JButton logoutButton = createLogoutButton();

        header.add(titleLabel, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
        header.add(logoutButton, BorderLayout.EAST);

        return header;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout") {
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(0, 123, 255));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(100, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        button.addActionListener(e -> {
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

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Container for text - aligned to top-left
        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
        textContainer.setBackground(Color.WHITE);
        textContainer.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to Library POS System!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("You are logged in as Manager. You can manage book loans and returns.");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textContainer.add(titleLabel);
        textContainer.add(Box.createRigidArea(new Dimension(0, 12)));
        textContainer.add(descLabel);

        // Add to WEST (left) and NORTH (top) of BorderLayout
        panel.add(textContainer, BorderLayout.WEST);

        return panel;
    }

    private JPanel createCardsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 30, 30));
        panel.setBackground(new Color(248, 249, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // Card 1 - View Books
        panel.add(createFeatureCard(
            "View Books",
            "View available books list",
            new Color(0, 123, 255),
            ""
        ));

        // Card 2 - View Members
        panel.add(createFeatureCard(
            "View Members",
            "View library members data",
            new Color(40, 167, 69),
            ""
        ));

        // Card 3 - Categories
        panel.add(createFeatureCard(
            "Categories",
            "Manage book categories",
            new Color(108, 99, 255),
            ""
        ));

        // Card 4 - Transactions
        panel.add(createFeatureCard(
            "Transactions",
            "Book loans and returns",
            new Color(255, 193, 7),
            ""
        ));

        // Card 5 - Reports
        panel.add(createFeatureCard(
            "Reports",
            "View transaction reports",
            new Color(23, 162, 184),
            ""
        ));

        return panel;
    }

    private JPanel createFeatureCard(String title, String description, Color accentColor, String icon) {
        JPanel outerCard = new JPanel(new BorderLayout());
        outerCard.setBackground(new Color(248, 249, 250));
        outerCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Accent line at top (outside the card)
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));

        // Main card content
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        // Use 2px border from the start to prevent shifting on hover
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 2),
            new EmptyBorder(34, 29, 34, 29) // Adjust padding to compensate for 2px border
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(108, 117, 125));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(descLabel);

        outerCard.add(accentLine, BorderLayout.NORTH);
        outerCard.add(card, BorderLayout.CENTER);

        // Hover effect - only change border color, not thickness
        outerCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(34, 29, 34, 29)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230), 2),
                    new EmptyBorder(34, 29, 34, 29)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(title);
            }
        });

        return outerCard;
    }

    private void handleCardClick(String cardTitle) {
        switch (cardTitle) {
            case "View Books":
                showLihatBuku();
                break;
            case "View Members":
                showLihatAnggota();
                break;
            case "Categories":
                showCategories();
                break;
            case "Transactions":
                showTransaksi();
                break;
            case "Reports":
                showLaporan();
                break;
            default:
                JOptionPane.showMessageDialog(this,
                    "Feature " + cardTitle + " is under development",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private void showDashboard() {
        contentPanel.removeAll();

        JPanel dashboardContent = new JPanel();
        dashboardContent.setLayout(new BoxLayout(dashboardContent, BoxLayout.Y_AXIS));
        dashboardContent.setBackground(new Color(248, 249, 250));
        dashboardContent.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Welcome Panel
        JPanel welcomePanel = createWelcomePanel();
        dashboardContent.add(welcomePanel);
        dashboardContent.add(Box.createRigidArea(new Dimension(0, 40)));

        // Feature Cards Panel
        JPanel cardsPanel = createCardsPanel();
        dashboardContent.add(cardsPanel);
        dashboardContent.add(Box.createVerticalGlue());

        contentPanel.add(dashboardContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLihatBuku() {
        contentPanel.removeAll();

        com.library.pos.views.manager.books.IndexBookView bookView = 
            new com.library.pos.views.manager.books.IndexBookView();

        contentPanel.add(bookView, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLihatAnggota() {
        contentPanel.removeAll();
        LihatAnggotaView lihatAnggotaView = new LihatAnggotaView(currentUser);
        lihatAnggotaView.setOnBackCallback(() -> showDashboard());
        contentPanel.add(lihatAnggotaView, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCategories() {
        contentPanel.removeAll();

        com.library.pos.views.manager.categories.IndexCategoryView categoryView = 
            new com.library.pos.views.manager.categories.IndexCategoryView();

        contentPanel.add(categoryView, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showTransaksi() {
        contentPanel.removeAll();
        TransaksiView transaksiView = new TransaksiView(currentUser);
        transaksiView.setOnBackCallback(() -> showDashboard());
        contentPanel.add(transaksiView, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLaporan() {
        contentPanel.removeAll();
        LaporanView laporanView = new LaporanView(currentUser);
        laporanView.setOnBackCallback(() -> showDashboard());
        contentPanel.add(laporanView, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User dummyUser = new User(2, "manager", "", "Library Manager", "MANAGER");
            ManagerDashboardView dashboard = new ManagerDashboardView(dummyUser);
            dashboard.setVisible(true);
        });
    }
}

