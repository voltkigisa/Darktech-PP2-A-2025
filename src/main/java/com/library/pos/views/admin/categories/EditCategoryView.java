package com.library.pos.views.admin.categories;

import com.library.pos.controllers.CategoryController;
import com.library.pos.models.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * EditCategoryView - Form for editing existing category
 */
public class EditCategoryView extends JDialog {
    private CategoryController controller;
    private IndexCategoryView parentView;
    private Category category;
    private JTextField txtName;
    private JButton btnUpdate, btnCancel;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(149, 165, 166);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    public EditCategoryView(IndexCategoryView parent, Category category) {
        super(JOptionPane.getFrameForComponent(parent), "Edit Category", true);
        this.parentView = parent;
        this.category = category;
        this.controller = new CategoryController();
        initComponents();
        loadCategoryData();
        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(550, 600);
        setResizable(false);
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

        JLabel lblTitle = new JLabel("EDIT CATEGORY");
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
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));

        // Info panel
        JPanel infoPanel = createInfoPanel();
        formContainer.add(infoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Name field
        txtName = createTextField();
        formContainer.add(createFormField("Category Name *", txtName, "Minimum 3 characters, maximum 100 characters"));

        mainPanel.add(formContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 152, 219, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Edit Category ID: " + category.getId());
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(new Color(41, 128, 185));

        JLabel lblDetail = new JLabel("<html>Update category data. Category name must be unique.</html>");
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
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(0, 45));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnUpdate = createButton("UPDATE", PRIMARY_COLOR);
        btnUpdate.addActionListener(e -> updateCategory());

        btnCancel = createButton("CANCEL", SECONDARY_COLOR);
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

    private void loadCategoryData() {
        txtName.setText(category.getName());
    }

    private void updateCategory() {
        String name = txtName.getText().trim();

        if (controller.updateCategory(category.getId(), name)) {
            parentView.loadData();
            dispose();
        }
    }
}
