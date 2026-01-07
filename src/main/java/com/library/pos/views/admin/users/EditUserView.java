package com.library.pos.views.admin.users;

import com.library.pos.controllers.AdminUserController;
import com.library.pos.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * EditUserView - Form for editing existing user (Admin only)
 */
public class EditUserView extends JDialog {
    private AdminUserController controller;
    private IndexUserView parentView;
    private User user;
    private JTextField txtUsername, txtName;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnUpdate, btnCancel;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(149, 165, 166);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    public EditUserView(IndexUserView parent, User user) {
        super(SwingUtilities.getWindowAncestor(parent), "Edit Data User", ModalityType.APPLICATION_MODAL);
        this.parentView = parent;
        this.user = user;
        this.controller = new AdminUserController();
        initComponents();
        loadUserData();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(600, 800);
        setResizable(true);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("EDIT DATA USER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)));

        // Info panel
        JPanel infoPanel = createInfoPanel();
        formContainer.add(infoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Username field
        txtUsername = createTextField();
        formContainer
                .add(createFormField("Username *", txtUsername, "Minimal 3 karakter, hanya huruf, angka, underscore"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Name field
        txtName = createTextField();
        formContainer.add(createFormField("Nama Lengkap *", txtName, "Minimal 3 karakter"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        formContainer.add(createFormField("Password *", txtPassword, "Minimal 6 karakter"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Role combobox
        cbRole = new JComboBox<>(new String[] { "Pilih Role", "ADMIN", "MANAGER" });
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbRole.setPreferredSize(new Dimension(0, 45));
        cbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        formContainer.add(createFormField("Role *", cbRole, "Pilih role untuk user (ADMIN atau MANAGER)"));

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 152, 219, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Edit Data User ID: " + user.getId());
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(new Color(41, 128, 185));

        JLabel lblDetail = new JLabel("<html>Update data user. Semua field wajib diisi dan akan divalidasi.</html>");
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetail.setForeground(new Color(52, 73, 94));

        panel.add(lblInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblDetail);

        return panel;
    }

    private JPanel createFormField(String labelText, JComponent field, String hint) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(DARK_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(127, 140, 141));
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(hintLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        styleTextField(textField);
        return textField;
    }

    private void styleTextField(JComponent textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(0, 45));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnUpdate = createButton("UPDATE", PRIMARY_COLOR);
        btnUpdate.addActionListener(e -> updateUser());

        btnCancel = createButton("BATAL", SECONDARY_COLOR);
        btnCancel.addActionListener(e -> dispose());

        panel.add(btnUpdate);
        panel.add(btnCancel);

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

    private void loadUserData() {
        txtUsername.setText(user.getUsername());
        txtName.setText(user.getName());
        txtPassword.setText(user.getPassword());
        cbRole.setSelectedItem(user.getRole());
    }

    private void updateUser() {
        String username = txtUsername.getText().trim();
        String name = txtName.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cbRole.getSelectedItem();

        if (role != null && role.equals("Pilih Role")) {
            role = "";
        }

        if (controller.updateUser(user.getId(), username, password, name, role)) {
            parentView.loadData();
            // Refresh dashboard statistics if available
            if (parentView.getDashboard() != null) {
                parentView.getDashboard().refreshDashboardStats();
            }
            dispose();
        }
    }
}
