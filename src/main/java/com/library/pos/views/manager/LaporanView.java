package com.library.pos.views.manager;

import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * LaporanView - View untuk melihat laporan transaksi
 */
public class LaporanView extends JPanel {
    private final User currentUser;
    private Runnable onBackCallback;

    public LaporanView(User currentUser) {
        this.currentUser = currentUser;

        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        initComponents();
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    private void initComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        JLabel titleLabel = new JLabel("Laporan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));

        JLabel descLabel = new JLabel("Lihat laporan transaksi");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(descLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Back Button
        JButton backButton = createBackButton();
        headerPanel.add(backButton, BorderLayout.EAST);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("Fitur laporan sedang dalam pengembangan");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setForeground(new Color(108, 117, 125));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(infoLabel);
        contentPanel.add(Box.createVerticalGlue());

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createBackButton() {
        JButton button = new JButton("← Kembali") {
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
        button.setForeground(new Color(108, 117, 125));
        button.setBackground(new Color(248, 249, 250));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(233, 236, 239));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(248, 249, 250));
            }
        });

        button.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });

        return button;
    }
}
