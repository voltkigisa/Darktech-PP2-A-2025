package com.library.pos.views.manager.member;

import javax.swing.*;

import com.library.pos.controllers.MemberController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CreateMemberView - Form untuk menambah member baru
 * Mengikuti style Dashboard dan CreateCategoryView
 */
public class CreateMemberView extends JDialog {
    private MemberController controller;
    private KelolaMemberView parentView;
    private JTextField txtCode, txtName, txtAge, txtPhone, txtAddress;
    private JButton btnSave, btnCancel;

    // Palette Warna - Standardized
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Blue
    private final Color SUCCESS_COLOR = new Color(16, 185, 129); // Green
    private final Color SECONDARY_COLOR = new Color(149, 165, 166); // Gray
    private final Color DARK_COLOR = new Color(31, 41, 55);
    private final Color LIGHT_BG = new Color(249, 250, 251);
    private final Color INFO_BG = new Color(16, 185, 129, 20); // Green Transparent

    public CreateMemberView(KelolaMemberView parent) {
        // Mengambil frame utama agar dialog menjadi modal (mengunci layar belakang)
        super(JOptionPane.getFrameForComponent(parent), "Add New Member", true);
        this.parentView = parent;
        this.controller = new MemberController();

        initComponents();
        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(600, 750); // Tinggi disesuaikan dengan jumlah field
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        // Bagian Atas (Title)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Bagian Tengah (Form) - Menggunakan ScrollPane jika field bertambah banyak
        JPanel formContent = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Bagian Bawah (Tombol)
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("ADD NEW MEMBER");
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

        // 1. Info Panel (Kotak Hijau)
        formContainer.add(createInfoPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. Input Fields
        txtCode = createTextField();
        formContainer.add(createFormField("Member Code *", txtCode, "ID unik anggota (Contoh: MBR-001)"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        txtName = createTextField();
        formContainer.add(createFormField("Full Name *", txtName, "Nama lengkap sesuai identitas"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        txtAge = createTextField();
        formContainer.add(createFormField("Age *", txtAge, "Masukkan angka (Contoh: 20)"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        txtPhone = createTextField();
        formContainer.add(createFormField("Phone Number *", txtPhone, "Nomor aktif yang bisa dihubungi"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        txtAddress = createTextField();
        formContainer.add(createFormField("Address *", txtAddress, "Alamat lengkap saat ini"));

        mainPanel.add(formContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(INFO_BG); // Green Transparent
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SUCCESS_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Add New Member");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(SUCCESS_COLOR);

        JLabel lblDetail = new JLabel("<html>Create a new record for library member. All fields are required.</html>");
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetail.setForeground(DARK_COLOR);

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

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(127, 140, 141));

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
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnSave = createButton("SAVE MEMBER", SUCCESS_COLOR);
        btnSave.addActionListener(e -> saveMember());

        btnCancel = createButton("CANCEL", SECONDARY_COLOR);
        btnCancel.addActionListener(e -> dispose());

        panel.add(btnSave);
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
        button.setPreferredSize(new Dimension(180, 45));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void saveMember() {
        String code = txtCode.getText().trim();
        String name = txtName.getText().trim();
        String age = txtAge.getText().trim(); // Controller Anda menerima String untuk age
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();

        // Validasi sederhana
        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Code dan Nama wajib diisi!");
            return;
        }

        // GANTI 'create' MENJADI 'save' sesuai dengan isi MemberController Anda
        if (controller.save(code, name, age, phone, address)) {
            JOptionPane.showMessageDialog(this, "Member berhasil ditambahkan!");
            parentView.loadData(); // Pastikan loadData di KelolaMemberView sudah 'public'
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke database!");
        }
    }
}