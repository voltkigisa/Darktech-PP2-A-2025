package com.library.pos.views;

import com.library.pos.models.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagerDashboardPanel extends JPanel {
    private final User currentUser;
    private static final Color BACKGROUND = new Color(243, 244, 246);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color PRIMARY = new Color(79, 70, 229);
    private static final Color SUCCESS = new Color(16, 185, 129);
    private static final Color WARNING = new Color(245, 158, 11);
    private static final Color INFO = new Color(59, 130, 246);

    public ManagerDashboardPanel(User currentUser) {
        this.currentUser = currentUser;
        initializeUI();
    }

    private void initializeUI() {
        setBackground(BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));
        add(createHeaderPanel(), BorderLayout.NORTH);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.add(Box.createVerticalStrut(25));
        content.add(createStatsRow());
        content.add(Box.createVerticalStrut(25));
        content.add(createActionsRow());
        add(content, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        JPanel welcome = new JPanel();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.setBackground(BACKGROUND);
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "! 👋");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        JLabel dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_SECONDARY);
        welcome.add(welcomeLabel);
        welcome.add(Box.createVerticalStrut(5));
        welcome.add(dateLabel);
        panel.add(welcome, BorderLayout.WEST);
        JLabel badge = new JLabel("MANAGER");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(SUCCESS);
        badge.setBorder(new EmptyBorder(8, 16, 8, 16));
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        badgePanel.setBackground(BACKGROUND);
        badgePanel.add(badge);
        panel.add(badgePanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setBackground(BACKGROUND);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        row.add(createStatCard("📚", "Available Books", "1,145", PRIMARY));
        row.add(createStatCard("🔄", "Books on Loan", "89", WARNING));
        row.add(createStatCard("📅", "Due Today", "12", INFO));
        row.add(createStatCard("⚠️", "Overdue", "5", new Color(239, 68, 68)));
        return row;
    }

    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLbl.setForeground(TEXT_SECONDARY);
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLbl.setForeground(TEXT_PRIMARY);
        info.add(titleLbl);
        info.add(valueLbl);
        card.add(iconLbl, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel createActionsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setBackground(BACKGROUND);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        row.add(createQuickActionsCard());
        row.add(createTodayTasksCard());
        return row;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Quick Actions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        JPanel actions = new JPanel(new GridLayout(2, 2, 10, 10));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(15, 0, 0, 0));
        actions.add(createBtn("📖 Lend Book", PRIMARY));
        actions.add(createBtn("↩️ Return Book", SUCCESS));
        actions.add(createBtn("🔍 Search", INFO));
        actions.add(createBtn("👤 Members", WARNING));
        card.add(title, BorderLayout.NORTH);
        card.add(actions, BorderLayout.CENTER);
        return card;
    }

    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(color);
        btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createTodayTasksCard() {
        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Today's Tasks");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(TEXT_PRIMARY);
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setBorder(new EmptyBorder(15, 0, 0, 0));
        list.add(createTaskItem("Process 12 pending returns", false));
        list.add(Box.createVerticalStrut(8));
        list.add(createTaskItem("Follow up on 5 overdue books", false));
        list.add(Box.createVerticalStrut(8));
        list.add(createTaskItem("Update member records", true));
        card.add(title, BorderLayout.NORTH);
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTaskItem(String task, boolean done) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        JLabel taskLbl = new JLabel((done ? "✓ " : "○ ") + task);
        taskLbl.setFont(new Font("Segoe UI", done ? Font.ITALIC : Font.PLAIN, 12));
        taskLbl.setForeground(done ? TEXT_SECONDARY : TEXT_PRIMARY);
        item.add(taskLbl, BorderLayout.WEST);
        return item;
    }
}
