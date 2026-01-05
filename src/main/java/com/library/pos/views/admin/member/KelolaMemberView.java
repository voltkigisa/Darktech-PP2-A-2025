package com.library.pos.views.admin.member;

// Import Controller Anda
import com.library.pos.controllers.admin.MemberController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KelolaMemberView extends JPanel {
    // Pastikan inisialisasi controller benar
    private final MemberController controller = new MemberController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtTelepon, txtAlamat;
    
    // selectedId harus INT agar pas dengan parameter update(int id, ...) di controller
    private int selectedId = -1; 

    public KelolaMemberView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshTable(); // Memanggil data saat pertama kali dibuka
    }

    private void initComponents() {
        // --- PANEL FORM (KIRI) ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(300, 0));

        txtNama = new JTextField();
        txtTelepon = new JTextField();
        txtAlamat = new JTextField();

        // Layouting form sederhana
        formPanel.add(new JLabel("Nama Lengkap:"));
        formPanel.add(txtNama);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(new JLabel("Nomor Telepon:"));
        formPanel.add(txtTelepon);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(txtAlamat);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Tombol-tombol
        JButton btnSave = new JButton("Simpan");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
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
        String[] columns = {"ID", "Nama", "Telepon", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- LOGIKA EVENT (Agar tidak error) ---

        // 1. Simpan (Create)
        btnSave.addActionListener(e -> {
            if (controller.save(txtNama.getText(), txtTelepon.getText(), txtAlamat.getText())) {
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
                refreshTable();
                clearForm();
            }
        });

        // 2. Klik Tabel (Ambil data untuk Update/Delete)
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // Mengambil ID dari kolom 0
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtNama.setText(table.getValueAt(row, 1).toString());
                txtTelepon.setText(table.getValueAt(row, 2).toString());
                txtAlamat.setText(table.getValueAt(row, 3).toString());
            }
        });

        // 3. Update
        btnUpdate.addActionListener(e -> {
            if (selectedId != -1) {
                if (controller.update(selectedId, txtNama.getText(), txtTelepon.getText(), txtAlamat.getText())) {
                    JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!");
                    refreshTable();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!");
            }
        });

        // 4. Delete
        btnDelete.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus member ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
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
        // Memanggil method loadDataToTable di controller Anda
        controller.loadDataToTable(tableModel);
    }

    private void clearForm() {
        txtNama.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        selectedId = -1;
        table.clearSelection();
    }
}