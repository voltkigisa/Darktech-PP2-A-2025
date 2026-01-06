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
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(300, 0));

        txtmember_code = new JTextField();
        txtName = new JTextField();
        txtAge = new JTextField();
        txtPhone = new JTextField();
        txtAddress = new JTextField();

        // Input Fields
        formPanel.add(new JLabel("Member Code:"));
        formPanel.add(txtmember_code);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(new JLabel("Age:"));
        formPanel.add(txtAge);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(txtPhone);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtAddress);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Tombol CRUD
        JButton btnSave = new JButton("Save Member");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 5, 5));
        btnGrid.setBackground(Color.WHITE);
        btnGrid.add(btnSave);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);
        formPanel.add(btnGrid);

        add(formPanel, BorderLayout.WEST);

        // --- PANEL TABEL (TENGAH) ---
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);

        // Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        searchField.setToolTipText("Cari berdasarkan nama atau kode member");
        JLabel lblSearch = new JLabel("Search: ");
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);

        // Menambah kolom "Age" pada tabel
        String[] columns = { "ID", "member_code", "Name", "Age", "Phone", "Address" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        // Search listener
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable(searchField.getText());
            }
        });

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        // --- LOGIKA EVENT ---
        btnSave.addActionListener(e -> {
            if (controller.save(txtmember_code.getText(), txtName.getText(), txtAge.getText(), txtPhone.getText(),
                    txtAddress.getText())) {
                JOptionPane.showMessageDialog(this, "Success: Member saved!");
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
                if (controller.update(selectedId, txtmember_code.getText(), txtName.getText(), txtAge.getText(),
                        txtPhone.getText(), txtAddress.getText())) {
                    JOptionPane.showMessageDialog(this, "Success: Data updated!");
                    refreshTable();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member from the table.");
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this member?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);
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

    private void filterTable(String searchText) {
        searchText = searchText.toLowerCase().trim();
        // Reload full data first
        controller.loadDataToTable(tableModel);

        if (!searchText.isEmpty()) {
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                String memberCode = tableModel.getValueAt(i, 1).toString().toLowerCase();
                String name = tableModel.getValueAt(i, 2).toString().toLowerCase();
                if (!memberCode.contains(searchText) && !name.contains(searchText)) {
                    tableModel.removeRow(i);
                }
            }
        }
    }

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