package com.library.pos.views.admin.member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ShowMemberView - Detail view for a single Member
 * Mengikuti style ShowBookView
 */
public class ShowMemberView extends JDialog {
    private KelolaMemberView parentView;
    private int id;
    private String code, name, age, phone, address;
    private JButton btnClose, btnEdit;

    // Palette Warna seragam dengan ShowBookView
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color INFO_BG = new Color(52, 152, 219, 20); // Biru Transparan

    public ShowMemberView(KelolaMemberView parent, int id, String code, String name, String age, String phone, String address) {
        super(JOptionPane.getFrameForComponent(parent), "Member Details", true);
        this.parentView = parent;
        this.id = id;
        this.code = code;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;

        initComponents();
        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(600, 800);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Detail Panel dengan Scroll
        JPanel detailPanel = createDetailPanel();
        JScrollPane scrollPane = new JScrollPane(detailPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("MEMBER DETAILS");
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

        // Icon Teks Besar
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel iconLabel = new JLabel("MEMBER");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setForeground(new Color(52, 152, 219));
        iconPanel.add(iconLabel);

        detailContainer.add(iconPanel);

        // Fields (Menggunakan method createDetailField seperti kodinganmu)
        detailContainer.add(createDetailField("ID", String.valueOf(id)));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Member Code", code));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Full Name", name));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Age", age));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Phone Number", phone));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Address", address));

        mainPanel.add(detailContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createDetailField(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(INFO_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLabel.setForeground(new Color(52, 73, 94));
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value != null && !value.isEmpty() ? value : "-");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblValue.setForeground(DARK_COLOR);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(lblValue);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnEdit = createButton("EDIT DATA", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> {
            dispose();
            // Membuka EditMemberView
            EditMemberView editView = new EditMemberView(parentView, id, code, name, age, phone, address);
            editView.setVisible(true);
        });

        btnClose = createButton("CLOSE", new Color(149, 165, 166));
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
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });

        return button;
    }
}