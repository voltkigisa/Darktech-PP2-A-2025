package com.library.pos.views.admin.member;

import com.library.pos.controllers.admin.MemberController;
import com.library.pos.views.admin.AdminDashboardView;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * KelolaMemberView - Full Version
 * Fitur: Teks Hitam Pekat, Tombol Lengkap (Detail, Edit, Hapus)
 */
public class KelolaMemberView extends JPanel {
    private final MemberController controller = new MemberController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMemberCode, txtName, txtAge, txtPhone, txtAddress;
    private int selectedId = -1;
    private AdminDashboardView dashboard; // Reference to dashboard for refresh

    // Palette Warna
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);   // Blue
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);   // Green
    private final Color DANGER_COLOR = new Color(239, 68, 68);    // Red
    private final Color DARK_COLOR = new Color(31, 41, 55);
    private final Color LIGHT_BG = new Color(249, 250, 251);

    // Constructor without dashboard (for backward compatibility)
    public KelolaMemberView() {
        this(null);
    }

    // Constructor with dashboard reference
    public KelolaMemberView(AdminDashboardView dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(30, 0));
        setBackground(new Color(245, 247, 250)); // Light Gray Background
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);

        // 1. Header Section
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Main Content
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("MANAJEMEN MEMBERS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola data anggota perpustakaan");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(107, 114, 128));

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(LIGHT_BG);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Top Area (Judul Tabel & Tombol Tambah)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR MEMBERS");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        btnAdd = createButton("TAMBAH MEMBER", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> openCreateDialog());

        topPanel.add(tableTitle, BorderLayout.WEST);
        topPanel.add(btnAdd, BorderLayout.EAST);

        // Setup Table
        String[] columns = {"ID", "Code", "Name", "Age", "Phone", "Address", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom Aksi yang bisa diklik
            }
        };

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(topPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableContainer, BorderLayout.CENTER);
        return mainPanel;
    }

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        setupTableStyle();

        // --- LOGIKA EVENT ---
        btnSave.addActionListener(e -> {
            if (controller.save(txtMemberCode.getText(), txtName.getText(), txtAge.getText(), txtPhone.getText(),
                    txtAddress.getText())) {
                JOptionPane.showMessageDialog(this, "Success: Member saved!");
                refreshTable();
                clearForm();
                // Refresh dashboard statistics if available
                if (dashboard != null) {
                    dashboard.refreshDashboardStats();
                }
            }
        };

        // Terapkan Renderer ke Kolom 0-5
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(blackTextRenderer);
        }

        btnUpdate.addActionListener(e -> {
            if (selectedId != -1) {
                if (controller.update(selectedId, txtMemberCode.getText(), txtName.getText(), txtAge.getText(),
                        txtPhone.getText(), txtAddress.getText())) {
                    JOptionPane.showMessageDialog(this, "Success: Data updated!");
                    refreshTable();
                    clearForm();
                    // Refresh dashboard statistics if available
                    if (dashboard != null) {
                        dashboard.refreshDashboardStats();
                    }
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
                        // Refresh dashboard statistics if available
                        if (dashboard != null) {
                            dashboard.refreshDashboardStats();
                        }
                    }
                }
            }
        });
        return button;
    }

    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(75, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void loadData() { 
        tableModel.setRowCount(0);
        controller.loadDataToTable(tableModel);
    }

    private void openCreateDialog() {
        CreateMemberView dialog = new CreateMemberView(this);
        dialog.setVisible(true);
    }

    // --- INNER CLASSES (RENDERER & EDITOR) ---

    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
            setBackground(Color.WHITE);
            add(createSmallButton("Detail", PRIMARY_COLOR));
            add(createSmallButton("Edit", new Color(59, 130, 246)));
            add(createSmallButton("Hapus", DANGER_COLOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private int memberId;
        private JButton btnDetail, btnEdit, btnDelete;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
            panel.setBackground(Color.WHITE);

            btnDetail = createSmallButton("Detail", PRIMARY_COLOR);
            btnEdit = createSmallButton("Edit", new Color(59, 130, 246));
            btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            btnDetail.addActionListener(e -> {
    fireEditingStopped();
    int row = table.getSelectedRow();
    if (row != -1) {
        int id = (int) table.getValueAt(row, 0);
        String code = (String) table.getValueAt(row, 1);
        String name = (String) table.getValueAt(row, 2);
        String age = String.valueOf(table.getValueAt(row, 3));
        String phone = (String) table.getValueAt(row, 4);
        String address = (String) table.getValueAt(row, 5);

        // Gunakan 'KelolaMemberView.this' sebagai parent
        ShowMemberView showView = new ShowMemberView(
            KelolaMemberView.this, 
            id, code, name, age, phone, address
        );
        showView.setVisible(true);
    }
});



            btnEdit.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            if (row != -1) {
            // Mengambil data dari baris yang diklik
            int id = (int) table.getValueAt(row, 0);
            String code = (String) table.getValueAt(row, 1);
            String name = (String) table.getValueAt(row, 2);
            String age = String.valueOf(table.getValueAt(row, 3));
            String phone = (String) table.getValueAt(row, 4);
            String address = (String) table.getValueAt(row, 5);

            // Membuka Form Edit dengan data yang sudah terisi
            EditMemberView editView = new EditMemberView(KelolaMemberView.this, id, code, name, age, phone, address);
            editView.setVisible(true);
            }
        });

            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(panel, "Hapus member ini?", "Hapus", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controller.delete(memberId)) {
                        loadData();
                    }
                }
            });

            panel.add(btnDetail);
            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            memberId = (int) t.getValueAt(r, 0);
            return panel;
        }
    }

    private void refreshTable() {
        controller.loadDataToTable(tableModel);
    }

    private void setupTableStyle() {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(99, 102, 241, 30));
        table.setSelectionForeground(TEXT_COLOR);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}