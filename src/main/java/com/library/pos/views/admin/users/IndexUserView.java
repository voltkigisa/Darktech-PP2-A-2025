package com.library.pos.views.admin.users;

import com.library.pos.controllers.admin.AdminUserController;
import com.library.pos.models.User;
import com.library.pos.views.admin.AdminDashboardView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * IndexUserView - Main list view for User management (Admin only)
 */
public class IndexUserView extends JPanel {
    private AdminUserController controller;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JTextField searchField;
    private JComboBox<String> filterRole;
    private List<User> allUsers;
    private AdminDashboardView dashboard; // Reference to dashboard for refresh

    // Color scheme - matching dashboard
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color INFO_COLOR = new Color(139, 92, 246);
    private final Color DARK_COLOR = new Color(31, 41, 55);
    private final Color LIGHT_BG = new Color(249, 250, 251);

    // Constructor without dashboard (for backward compatibility)
    public IndexUserView() {
        this(null);
    }

    // Constructor with dashboard reference
    public IndexUserView(AdminDashboardView dashboard) {
        this.dashboard = dashboard;
        this.controller = new AdminUserController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);

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

        JLabel lblTitle = new JLabel("MANAJEMEN USERS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola Data Admin dan Manager Sistem");
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
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        // Top panel with title and add button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR USERS");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        btnAdd = createButton("TAMBAH USER", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> openCreateView());

        topPanel.add(tableTitle, BorderLayout.WEST);

        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchFilterPanel.setBackground(Color.WHITE);

        // Search field
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        searchField.setText("Search...");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterData();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterData();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterData();
            }
        });

        // Role filter
        String[] roles = { "Semua Role", "ADMIN", "MANAGER" };
        filterRole = new JComboBox<>(roles);
        filterRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterRole.setPreferredSize(new Dimension(120, 35));
        filterRole.addActionListener(e -> filterData());

        searchFilterPanel.add(new JLabel("Filter:"));
        searchFilterPanel.add(filterRole);
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(btnAdd);

        topPanel.add(searchFilterPanel, BorderLayout.EAST);

        // Table with action buttons in column
        String[] columns = { "ID", "Username", "Nama Lengkap", "Role", "Tanggal Dibuat", "Aksi" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only action column is "editable" for button clicks
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? JPanel.class : Object.class;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userTable.setForeground(Color.BLACK);
        userTable.setRowHeight(55);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setShowVerticalLines(true);
        userTable.setGridColor(new Color(220, 220, 220));

        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(DARK_COLOR);
        userTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        userTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(180);
        userTable.getColumnModel().getColumn(5).setPreferredWidth(280);

        // Center align for all columns except action
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(Color.BLACK);
                setBackground(isSelected ? new Color(52, 152, 219, 50) : Color.WHITE);
                setOpaque(true);
                return this;
            }
        };

        for (int i = 0; i < 5; i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Action column renderer with buttons
        userTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        userTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(topPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 38));

        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(bgColor.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(70, 30));
        button.setMargin(new Insets(5, 8, 5, 8));

        return button;
    }

    public void loadData() {
        allUsers = controller.getAllUsers();
        filterData();
    }

    /**
     * Get dashboard reference for refresh callbacks
     */
    public AdminDashboardView getDashboard() {
        return dashboard;
    }

    private void filterData() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().toLowerCase();
        if (searchText.equals("search..."))
            searchText = "";
        String selectedRole = (String) filterRole.getSelectedItem();

        for (User user : allUsers) {
            boolean matchesSearch = searchText.isEmpty() ||
                    user.getUsername().toLowerCase().contains(searchText) ||
                    user.getName().toLowerCase().contains(searchText);

            boolean matchesRole = selectedRole.equals("Semua Role") ||
                    user.getRole().equals(selectedRole);

            if (matchesSearch && matchesRole) {
                Object[] row = {
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getRole(),
                        user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 19).replace("T", " ")
                                : "-",
                        user.getId()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openCreateView() {
        CreateUserView createView = new CreateUserView(this);
        createView.setVisible(true);
    }

    private void openShowView(int userId) {
        User user = controller.getUserById(userId);
        if (user != null) {
            ShowUserView showView = new ShowUserView(this, user);
            showView.setVisible(true);
        }
    }

    private void openEditView(int userId) {
        User user = controller.getUserById(userId);
        if (user != null) {
            EditUserView editView = new EditUserView(this, user);
            editView.setVisible(true);
        }
    }

    private void deleteUser(int userId) {
        if (controller.deleteUser(userId)) {
            loadData();
            // Refresh dashboard statistics if available
            if (dashboard != null) {
                dashboard.refreshDashboardStats();
            }
        }
    }

    // Inner class for rendering action buttons
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnShow, btnEdit, btnDelete;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setBackground(Color.WHITE);

            btnShow = createSmallButton("Detail", INFO_COLOR);
            btnEdit = createSmallButton("Edit", PRIMARY_COLOR);
            btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            add(btnShow);
            add(btnEdit);
            add(btnDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Inner class for handling button clicks in table
    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnShow, btnEdit, btnDelete;
        private int userId;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);

            btnShow = createSmallButton("Detail", INFO_COLOR);
            btnEdit = createSmallButton("Edit", PRIMARY_COLOR);
            btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            btnShow.addActionListener(e -> {
                fireEditingStopped();
                openShowView(userId);
            });

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                openEditView(userId);
            });

            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                deleteUser(userId);
            });

            panel.add(btnShow);
            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            userId = (int) table.getValueAt(row, 0); // Get ID from first column
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return userId;
        }
    }
}
