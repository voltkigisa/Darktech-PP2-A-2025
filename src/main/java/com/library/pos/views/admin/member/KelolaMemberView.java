package com.library.pos.views.admin.member;

import com.library.pos.controllers.admin.MemberController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KelolaMemberView extends JPanel {
    private final MemberController controller = new MemberController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMemberCode, txtName, txtAge, txtPhone, txtAddress;
    private int selectedId = -1;

    // Warna Tema (Senada dengan Dashboard)
    private final Color PRIMARY_COLOR = new Color(99, 102, 241); // Indigo
    private final Color DANGER_COLOR = new Color(239, 68, 68);  // Red
    private final Color BG_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(31, 41, 55);

    public KelolaMemberView() {
        setLayout(new BorderLayout(30, 0));
        setBackground(new Color(245, 247, 250)); // Light Gray Background
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // --- PANEL FORM (KIRI) ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(BG_COLOR);
        cardPanel.setPreferredSize(new Dimension(350, 0));
        cardPanel.setBorder(new LineBorder(new Color(229, 231, 235), 1));

        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(BG_COLOR);
        formContent.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Judul Form
        JLabel formTitle = new JLabel("Member Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TEXT_COLOR);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContent.add(formTitle);
        formContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // Inisialisasi Input dengan Style Modern
        txtMemberCode = createStyledTextField();
        txtName = createStyledTextField();
        txtAge = createStyledTextField();
        txtPhone = createStyledTextField();
        txtAddress = createStyledTextField();

        addLabeledInput(formContent, "Member Code", txtMemberCode);
        addLabeledInput(formContent, "Full Name", txtName);
        addLabeledInput(formContent, "Age", txtAge);
        addLabeledInput(formContent, "Phone Number", txtPhone);
        addLabeledInput(formContent, "Address", txtAddress);

        formContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tombol-tombol Action
        JButton btnSave = createModernButton("Save Member", PRIMARY_COLOR);
        JButton btnUpdate = createModernButton("Update", new Color(16, 185, 129)); // Green
        JButton btnDelete = createModernButton("Delete", DANGER_COLOR);
        JButton btnClear = createModernButton("Clear Form", new Color(107, 114, 128)); // Gray

        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionPanel.setBackground(BG_COLOR);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        actionPanel.add(btnSave);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnClear);

        formContent.add(actionPanel);
        cardPanel.add(formContent, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.WEST);

        // --- PANEL TABEL (TENGAH) ---
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(BG_COLOR);
        tableContainer.setBorder(new LineBorder(new Color(229, 231, 235), 1));

        String[] columns = {"ID", "Code", "Name", "Age", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        // --- LOGIKA EVENT ---
        btnSave.addActionListener(e -> {
            if (controller.save(txtMemberCode.getText(), txtName.getText(), txtAge.getText(), txtPhone.getText(), txtAddress.getText())) {
                JOptionPane.showMessageDialog(this, "Success: Member registered!");
                refreshTable();
                clearForm();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtMemberCode.setText(table.getValueAt(row, 1).toString());
                txtName.setText(table.getValueAt(row, 2).toString());
                txtAge.setText(table.getValueAt(row, 3).toString());
                txtPhone.setText(table.getValueAt(row, 4).toString());
                txtAddress.setText(table.getValueAt(row, 5).toString());
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedId != -1) {
                if (controller.update(selectedId, txtMemberCode.getText(), txtName.getText(), txtAge.getText(), txtPhone.getText(), txtAddress.getText())) {
                    JOptionPane.showMessageDialog(this, "Success: Member updated!");
                    refreshTable();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member from the table.");
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this member permanently?", "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controller.delete(selectedId)) {
                        refreshTable();
                        clearForm();
                    }
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());
    }

    // Helper: Membuat Label + Input Field
    private void addLabeledInput(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(75, 85, 99));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 35));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupTableStyle() {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(PRIMARY_COLOR);
        table.setGridColor(new Color(243, 244, 246));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowVerticalLines(false);

        // Header Style
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(249, 250, 251));
        table.getTableHeader().setForeground(new Color(107, 114, 128));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        // Center Align Kolom ID & Age
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    private void refreshTable() { controller.loadDataToTable(tableModel); }

    private void clearForm() {
        txtMemberCode.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}