package com.library.pos.views.manager;

import com.library.pos.models.User;
import com.library.pos.views.manager.borrowings.IndexBorrowingView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TransaksiView - View untuk transaksi peminjaman dan pengembalian
 */
public class TransaksiView extends JPanel {
    private final User currentUser;
    private Runnable onBackCallback;

    public TransaksiView(User currentUser) {
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
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("Transaksi");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));

        JLabel descLabel = new JLabel("Peminjaman dan pengembalian buku");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        titlePanel.add(descLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Back Button
        JButton backButton = createBackButton();
        headerPanel.add(backButton, BorderLayout.EAST);

        // Content Panel - embed IndexBorrowingView
        IndexBorrowingView borrowingView = new IndexBorrowingView();

        add(headerPanel, BorderLayout.NORTH);
        add(borrowingView, BorderLayout.CENTER);
    }

    private JButton createBackButton() {
    Color greenSuccess = new Color(16, 185, 129); 
    
    JButton button = new JButton("← Kembali") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // Round lebih melengkung
            
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
    button.setForeground(Color.WHITE); 
    button.setBackground(greenSuccess);
    
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setPreferredSize(new Dimension(120, 38));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(new Color(5, 150, 105)); 
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(greenSuccess);
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