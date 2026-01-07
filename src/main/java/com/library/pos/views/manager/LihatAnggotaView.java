package com.library.pos.views.manager;

import com.library.pos.controllers.admin.MemberController;
import com.library.pos.models.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LihatAnggotaView extends JPanel {
    private final MemberController controller = new MemberController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMemberCode, txtName, txtAge, txtPhone, txtAddress;
    private int selectedId = -1;
    
    private Runnable onBackCallback;
    private final User currentUser;

    private final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color BG_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(31, 41, 55);

    public LihatAnggotaView(User user) {
        this.currentUser = user;
        
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250)); // Mengikuti background TransaksiView

        initComponents();
        refreshTable();
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    private void initComponents() {
        // --- HEADER PANEL (Persis TransaksiView) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("Kelola Anggota");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));

        JLabel descLabel = new JLabel("Manajemen data member perpustakaan");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(108, 117, 125));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        titlePanel.add(descLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Back Button (Panggil method createBackButton yang identik)
        JButton backButton = createBackButton();
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- CONTENT WRAPPER ---
        JPanel contentWrapper = new JPanel(new BorderLayout(30, 0));
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(20, 30, 30, 30));

        // --- PANEL FORM (KIRI) ---
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(BG_COLOR);
        cardPanel.setPreferredSize(new Dimension(350, 0));
        cardPanel.setBorder(new LineBorder(new Color(229, 231, 235), 1));

        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(BG_COLOR);
        formContent.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel formTitle = new JLabel("Member Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TEXT_COLOR);
        formContent.add(formTitle);
        formContent.add(Box.createRigidArea(new Dimension(0, 20)));

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

        JButton btnSave = createModernButton("Save Member", PRIMARY_COLOR);
        JButton btnUpdate = createModernButton("Update", new Color(16, 185, 129));
        JButton btnDelete = createModernButton("Delete", DANGER_COLOR);
        JButton btnClear = createModernButton("Clear", new Color(107, 114, 128));

        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionPanel.setBackground(BG_COLOR);
        actionPanel.add(btnSave);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnClear);

        formContent.add(actionPanel);
        cardPanel.add(formContent, BorderLayout.NORTH);
        contentWrapper.add(cardPanel, BorderLayout.WEST);

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
        contentWrapper.add(tableContainer, BorderLayout.CENTER);

        add(contentWrapper, BorderLayout.CENTER);

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
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete member?", "Warning", JOptionPane.YES_NO_OPTION);
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

    private JButton createBackButton() {
    Color greenSuccess = new Color(16, 185, 129); // Hijau Emerald
    
    JButton button = new JButton("← Kembali") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Menggambar background bulat warna hijau
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // Round lebih melengkung
            
            // Menggambar teks putih
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, x, y);
            g2.dispose();
        }
    };
    
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setForeground(Color.WHITE); // Teks jadi Putih
    button.setBackground(greenSuccess); // Background jadi Hijau
    
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setPreferredSize(new Dimension(120, 38));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(new Color(5, 150, 105)); 
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(greenSuccess);
        }
    });

    button.addActionListener(e -> {
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    });

    return button;
}

    private void addLabeledInput(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(75, 85, 99));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(textField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 35));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(209, 213, 219), 1), new EmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(color.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(color); }
        });
        return btn;
    }

    private void setupTableStyle() {
        table.setRowHeight(40);
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.setSelectionBackground(new Color(238, 242, 255));
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