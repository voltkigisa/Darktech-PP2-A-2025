package com.library.pos.views.admin.member;

import com.library.pos.controllers.admin.MemberController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KelolaMemberView extends JPanel {
    private final MemberController controller = new MemberController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtmember_code, txtName, txtAge, txtPhone, txtAddress;
    private int selectedId = -1; 

    public KelolaMemberView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

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
        // Menambah kolom "Age" pada tabel
        String[] columns = {"ID", "member_code", "Name", "Age", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- LOGIKA EVENT ---

        // Simpan Data
        btnSave.addActionListener(e -> {
            if (controller.save(txtmember_code.getText(),txtName.getText(), txtAge.getText(), txtPhone.getText(), txtAddress.getText())) {
                JOptionPane.showMessageDialog(this, "Success: Member saved!");
                refreshTable();
                clearForm();
            }
        });

        // Pilih Data dari Tabel
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtmember_code.setText(table.getValueAt(row, 1).toString());
                txtName.setText(table.getValueAt(row, 1).toString());
                txtAge.setText(table.getValueAt(row, 2).toString());
                txtPhone.setText(table.getValueAt(row, 3).toString());
                txtAddress.setText(table.getValueAt(row, 4).toString());
            }
        });

        // Update Data
        btnUpdate.addActionListener(e -> {
            if (selectedId != -1) {
                if (controller.update(selectedId,txtmember_code.getText(), txtName.getText(), txtAge.getText(), txtPhone.getText(), txtAddress.getText())) {
                    JOptionPane.showMessageDialog(this, "Success: Data updated!");
                    refreshTable();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member from the table.");
            }
        });

        // Delete Data
        btnDelete.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this member?", "Confirmation", JOptionPane.YES_NO_OPTION);
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

    private void refreshTable() {
        controller.loadDataToTable(tableModel);
    }

    private void clearForm() {
        txtmember_code.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}