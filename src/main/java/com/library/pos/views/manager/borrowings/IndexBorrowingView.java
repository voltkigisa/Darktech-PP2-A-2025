package com.library.pos.views.manager.borrowings;

import com.library.pos.controllers.BorrowingController;
import com.library.pos.models.Borrowing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * IndexBorrowingView - Manager borrowing list view with overdue highlighting
 */
public class IndexBorrowingView extends JPanel {
    private BorrowingController controller;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JTextField searchField;
    private JComboBox<String> filterStatus;
    private List<Borrowing> allBorrowings;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public IndexBorrowingView() {
        this.controller = new BorrowingController();
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

        JLabel lblTitle = new JLabel("MANAJEMEN PEMINJAMAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola Data Peminjaman Buku");
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

        JLabel tableTitle = new JLabel("DAFTAR PEMINJAMAN");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        btnAdd = createButton("TAMBAH PEMINJAMAN", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> openCreateDialog());

        topPanel.add(tableTitle, BorderLayout.WEST);

        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchFilterPanel.setBackground(Color.WHITE);

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        searchField.setText("Search member...");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search member...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search member...");
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

        String[] statuses = { "Semua Status", "BORROWED", "RETURNED", "TERLAMBAT" };
        filterStatus = new JComboBox<>(statuses);
        filterStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterStatus.setPreferredSize(new Dimension(130, 35));
        filterStatus.addActionListener(e -> filterData());

        searchFilterPanel.add(new JLabel("Status:"));
        searchFilterPanel.add(filterStatus);
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(btnAdd);

        topPanel.add(searchFilterPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Anggota", "Petugas", "Tgl Pinjam", "Tgl Kembali", "Status", "Aksi" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? JPanel.class : Object.class;
            }
        };

        borrowingTable = new JTable(tableModel);
        borrowingTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        borrowingTable.setForeground(Color.BLACK);
        borrowingTable.setRowHeight(55);
        borrowingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowingTable.setShowVerticalLines(true);
        borrowingTable.setGridColor(new Color(220, 220, 220));

        borrowingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        borrowingTable.getTableHeader().setBackground(DARK_COLOR);
        borrowingTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        borrowingTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        borrowingTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        borrowingTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        borrowingTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        borrowingTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        borrowingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        borrowingTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        borrowingTable.getColumnModel().getColumn(6).setPreferredWidth(180);

        // Custom renderer for overdue highlighting
        borrowingTable.setDefaultRenderer(Object.class, new OverdueRowRenderer());

        // Action column
        borrowingTable.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        borrowingTable.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(borrowingTable);
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
        button.setPreferredSize(new Dimension(200, 42));

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

    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(75, 32));
        return button;
    }

    public void loadData() {
        allBorrowings = controller.getAllBorrowings();
        filterData();
    }

    private void filterData() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().toLowerCase();
        if (searchText.equals("search member..."))
            searchText = "";
        String selectedStatus = (String) filterStatus.getSelectedItem();

        for (Borrowing borrowing : allBorrowings) {
            boolean matchesSearch = searchText.isEmpty() ||
                    (borrowing.getMemberName() != null && borrowing.getMemberName().toLowerCase().contains(searchText))
                    ||
                    (borrowing.getMemberCode() != null && borrowing.getMemberCode().toLowerCase().contains(searchText));

            boolean matchesStatus = selectedStatus.equals("Semua Status") ||
                    (selectedStatus.equals("TERLAMBAT") && borrowing.isOverdue()) ||
                    (!selectedStatus.equals("TERLAMBAT") && borrowing.getStatus().equals(selectedStatus));

            if (matchesSearch && matchesStatus) {
                String statusDisplay = borrowing.getStatus();
                if (borrowing.isOverdue()) {
                    statusDisplay = "⚠ TERLAMBAT";
                }

                Object[] row = {
                        borrowing.getId(),
                        borrowing.getMemberName() + " (" + borrowing.getMemberCode() + ")",
                        borrowing.getUserName(),
                        borrowing.getBorrowDate() != null ? borrowing.getBorrowDate().format(dateFormatter) : "-",
                        borrowing.getDueDate() != null ? borrowing.getDueDate().format(dateFormatter) : "-",
                        statusDisplay,
                        borrowing.getId() + "|" + borrowing.isOverdue()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openCreateDialog() {
        Window window = SwingUtilities.getWindowAncestor(this);
        CreateBorrowingView createView = new CreateBorrowingView(window, this);
        createView.setVisible(true);
    }

    private void openShowDialog(int borrowingId) {
        Window window = SwingUtilities.getWindowAncestor(this);
        ShowBorrowingView showView = new ShowBorrowingView(window, borrowingId);
        showView.setVisible(true);
    }

    private void deleteBorrowing(int borrowingId) {
        if (controller.deleteBorrowing(borrowingId)) {
            loadData();
        }
    }

    /**
     * Custom renderer to highlight overdue rows
     */
    class OverdueRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);

            // Check if this row is overdue
            Object actionValue = table.getValueAt(row, 6);
            boolean isOverdue = false;
            if (actionValue != null && actionValue.toString().contains("|true")) {
                isOverdue = true;
            }

            if (!isSelected) {
                if (isOverdue) {
                    c.setBackground(new Color(255, 235, 235));
                    c.setForeground(DANGER_COLOR);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }

            // Special styling for status column
            if (column == 5 && isOverdue) {
                setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }

            return c;
        }
    }

    class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnShow, btnDelete;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setBackground(Color.WHITE);

            btnShow = createSmallButton("Detail", new Color(52, 152, 219));
            btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            add(btnShow);
            add(btnDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Check if overdue for background
            if (value != null && value.toString().contains("|true")) {
                setBackground(new Color(255, 235, 235));
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnShow, btnDelete;
        private int borrowingId;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);

            btnShow = createSmallButton("Detail", new Color(52, 152, 219));
            btnShow.addActionListener(e -> {
                fireEditingStopped();
                openShowDialog(borrowingId);
            });

            btnDelete = createSmallButton("Hapus", DANGER_COLOR);
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                deleteBorrowing(borrowingId);
            });

            panel.add(btnShow);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value != null) {
                String[] parts = value.toString().split("\\|");
                this.borrowingId = Integer.parseInt(parts[0]);
                if (parts.length > 1 && parts[1].equals("true")) {
                    panel.setBackground(new Color(255, 235, 235));
                } else {
                    panel.setBackground(Color.WHITE);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return borrowingId;
        }
    }
}
