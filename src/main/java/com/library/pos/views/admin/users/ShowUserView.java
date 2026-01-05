package com.library.pos.views.admin.users;

import com.library.pos.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ShowUserView - Detail view for a single User (Admin only)
 */
public class ShowUserView extends JDialog {
    private IndexUserView parentView;
    private User user;
    private JButton btnClose, btnEdit;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color INFO_BG = new Color(52, 152, 219, 20);

    public ShowUserView(IndexUserView parent, User user) {
        super(SwingUtilities.getWindowAncestor(parent), "Detail User", ModalityType.APPLICATION_MODAL);
        this.parentView = parent;
        this.user = user;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(600, 750);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createDetailPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("DETAIL USER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle);

        return headerPanel;
    }

    private JPanel createDetailPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel detailContainer = new JPanel();
        detailContainer.setLayout(new BoxLayout(detailContainer, BoxLayout.Y_AXIS));
        detailContainer.setBackground(Color.WHITE);
        detailContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));

        // Profile icon
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel iconLabel = new JLabel("USER");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setForeground(new Color(52, 152, 219));
        iconPanel.add(iconLabel);

        detailContainer.add(iconPanel);

        // Detail fields
        detailContainer.add(createDetailField("ID", String.valueOf(user.getId())));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        detailContainer.add(createDetailField("Username", user.getUsername()));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        detailContainer.add(createDetailField("Nama Lengkap", user.getName()));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        detailContainer.add(createDetailField("Role", user.getRole()));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        String createdAt = user.getCreatedAt() != null ?
                user.getCreatedAt().toString().substring(0, 19).replace("T", " ") : "-";
        detailContainer.add(createDetailField("Tanggal Dibuat", createdAt));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        String updatedAt = user.getUpdatedAt() != null ?
                user.getUpdatedAt().toString().substring(0, 19).replace("T", " ") : "-";
        detailContainer.add(createDetailField("Terakhir Diupdate", updatedAt));

        mainPanel.add(detailContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createDetailField(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(INFO_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLabel.setForeground(new Color(52, 73, 94));
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValue.setForeground(DARK_COLOR);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblValue);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnEdit = createButton("EDIT DATA", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> {
            dispose();
            EditUserView editView = new EditUserView(parentView, user);
            editView.setVisible(true);
        });

        btnClose = createButton("TUTUP", new Color(149, 165, 166));
        btnClose.addActionListener(e -> dispose());

        panel.add(btnEdit);
        panel.add(btnClose);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 42));

        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });

        return button;
    }
}
