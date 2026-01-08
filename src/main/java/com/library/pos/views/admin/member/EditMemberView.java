package com.library.pos.views.admin.member;

import javax.swing.*;

import com.library.pos.controllers.MemberController;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * EditMemberView - Meniru persis style CreateMemberView
 * Tombol Save diganti UPDATE MEMBER
 */
public class EditMemberView extends JDialog {
    private MemberController controller;
    private KelolaMemberView parentView;
    private int memberId; // Menyimpan ID untuk query update
    private JTextField txtCode, txtName, txtAge, txtPhone, txtAddress;
    private JButton btnUpdate, btnCancel;

    // Palette Warna - Standardized
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Blue
    private final Color SUCCESS_COLOR = new Color(16, 185, 129); // Green
    private final Color SECONDARY_COLOR = new Color(149, 165, 166); // Gray
    private final Color DARK_COLOR = new Color(31, 41, 55);
    private final Color LIGHT_BG = new Color(249, 250, 251);
    private final Color INFO_BG = new Color(59, 130, 246, 20); // Blue Transparent

    public EditMemberView(KelolaMemberView parent, int id, String code, String name, String age, String phone,
            String address) {
        super(JOptionPane.getFrameForComponent(parent), "Edit Member", true);
        this.parentView = parent;
        this.memberId = id;
        this.controller = new MemberController();

        initComponents();

        // Mengisi data lama ke dalam field
        txtCode.setText(code);
        txtName.setText(name);
        txtAge.setText(age);
        txtPhone.setText(phone);
        txtAddress.setText(address);

        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(600, 750);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        // 1. Header (Judul diganti EDIT MEMBER)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Form (Persis seperti CreateMemberView)
        JPanel formContent = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer (Tombol UPDATE MEMBER)
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("EDIT MEMBER");
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

        // Info Panel (Judul di dalam kotak hijau diganti)
        formContainer.add(createInfoPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

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
        panel.setBackground(INFO_BG); // Blue Transparent
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Edit Member");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(PRIMARY_COLOR);

        JLabel lblDetail = new JLabel(
                "<html>Modify the record for this library member. All fields are required.</html>");
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

        // Teks diganti UPDATE MEMBER
        btnUpdate = createButton("UPDATE MEMBER", SUCCESS_COLOR);
        btnUpdate.addActionListener(e -> updateMember());

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
        button.setPreferredSize(new Dimension(200, 45)); // Lebih lebar dikit buat teks Update

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

    private void updateMember() {
        String code = txtCode.getText().trim();
        String name = txtName.getText().trim();
        String age = txtAge.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Code dan Nama wajib diisi!");
            return;
        }

        // Panggil controller.update (Pastikan MemberController punya method update)
        if (controller.update(memberId, code, name, age, phone, address)) {
            JOptionPane.showMessageDialog(this, "Member berhasil diperbarui!");
            parentView.loadData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data!");
        }
    }
}