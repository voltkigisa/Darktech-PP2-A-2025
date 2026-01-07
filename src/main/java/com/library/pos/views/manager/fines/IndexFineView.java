package com.library.pos.views.manager.fines;

import com.library.pos.controllers.FineController;
import com.library.pos.models.Fine;
import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * IndexFineView - Manager fine list view (view + pay only, no edit/delete)
 */
public class IndexFineView extends JPanel {
    private FineController controller;
    private JTable fineTable;
    private DefaultTableModel tableModel;
    private User currentUser;
    private Runnable onBackCallback;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    public IndexFineView() {
        this(null);
    }

    public IndexFineView(User currentUser) {
        this.controller = new FineController();
        this.currentUser = currentUser != null ? currentUser : new User(1, "admin", "", "Admin", "ADMIN");
        initComponents();
        loadData();
    }
    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
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

        JLabel lblTitle = new JLabel("MANAJEMEN DENDA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola Denda Keterlambatan Pengembalian");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(107, 114, 128));

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(createBackButton(), BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createBackButton() {
        Color greenSuccess = new Color(16, 185, 129);
        JButton button = new JButton("← Kembali") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
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
        button.setForeground(Color.WHITE);
        button.setBackground(greenSuccess);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(5, 150, 105)); }
            public void mouseExited(MouseEvent e) { button.setBackground(greenSuccess); }
        });

        button.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });
        return button;
    }

    private JPanel createTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(LIGHT_BG);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        // Top panel with title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR DENDA");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

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

        String[] statuses = { "Semua Status", "UNPAID", "PAID" };
        filterStatus = new JComboBox<>(statuses);
        filterStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterStatus.setPreferredSize(new Dimension(120, 35));
        filterStatus.addActionListener(e -> filterData());

        searchFilterPanel.add(new JLabel("Status:"));
        searchFilterPanel.add(filterStatus);
        searchFilterPanel.add(searchField);

        topPanel.add(searchFilterPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Anggota", "Jumlah Buku", "Hari Terlambat", "Total Denda", "Status", "Aksi" };
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

        fineTable = new JTable(tableModel);
        fineTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fineTable.setForeground(Color.BLACK);
        fineTable.setRowHeight(55);
        fineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fineTable.setShowVerticalLines(true);
        fineTable.setGridColor(new Color(220, 220, 220));

        fineTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        fineTable.getTableHeader().setBackground(DARK_COLOR);
        fineTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        fineTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        fineTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        fineTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        fineTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        fineTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        fineTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        fineTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        fineTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        // Custom renderer for status highlighting
        fineTable.setDefaultRenderer(Object.class, new StatusRowRenderer());

        // Action column
        fineTable.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        fineTable.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(fineTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(topPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        return mainPanel;
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
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        
        return button;
    }

    public void loadData() {
        allFines = controller.getAllFines();
        filterData();
    }

    private void filterData() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().toLowerCase();
        if (searchText.equals("search member..."))
            searchText = "";
        String selectedStatus = (String) filterStatus.getSelectedItem();

        for (Fine fine : allFines) {
            boolean matchesSearch = searchText.isEmpty() ||
                    (fine.getMemberName() != null && fine.getMemberName().toLowerCase().contains(searchText)) ||
                    (fine.getMemberCode() != null && fine.getMemberCode().toLowerCase().contains(searchText));

            boolean matchesStatus = selectedStatus.equals("Semua Status") ||
                    fine.getPaymentStatus().equals(selectedStatus);

            if (matchesSearch && matchesStatus) {
                String statusDisplay = fine.isPaid() ? "✓ LUNAS" : "BELUM BAYAR";
                Object[] row = {
                        fine.getId(),
                        fine.getMemberName() + " (" + fine.getMemberCode() + ")",
                        fine.getTotalBooks(),
                        fine.getDaysOverdue() + " hari",
                        fine.getFormattedAmount(),
                        statusDisplay,
                        fine.getId() + "|" + fine.getPaymentStatus()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openPayDialog(int fineId) {
        Window window = SwingUtilities.getWindowAncestor(this);
    }

    class StatusRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);

            Object actionValue = table.getValueAt(row, 6);
            boolean isUnpaid = actionValue != null && actionValue.toString().contains("|UNPAID");

            if (!isSelected) {
                if (isUnpaid) {
                    c.setBackground(new Color(255, 250, 230));
                    if (column == 5) {
                        c.setForeground(WARNING_COLOR);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    }
                } else {
                    c.setBackground(new Color(235, 255, 240));
                    if (column == 5) {
                        c.setForeground(SUCCESS_COLOR);
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    }
                }
            }
            return c;
        }
    }

    class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnPay;
        private JLabel lblPaid;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setBackground(Color.WHITE);
            btnPay = createSmallButton("Bayar", SUCCESS_COLOR);
            lblPaid = new JLabel("Lunas");
            lblPaid.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblPaid.setForeground(new Color(127, 140, 141));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            boolean isUnpaid = value != null && value.toString().contains("|UNPAID");
            if (isUnpaid) {
                add(btnPay);
                setBackground(new Color(255, 250, 230));
            } else {
                add(lblPaid);
                setBackground(new Color(235, 255, 240));
            }
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnPay;
        private JLabel lblPaid;
        private int fineId;
        private boolean isUnpaid;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);
            btnPay = createSmallButton("Bayar", SUCCESS_COLOR);
            btnPay.addActionListener(e -> {
                fireEditingStopped();
                openPayDialog(fineId);
            });
            lblPaid = new JLabel("Lunas");
            lblPaid.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblPaid.setForeground(new Color(127, 140, 141));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.removeAll();
            if (value != null) {
                String[] parts = value.toString().split("\\|");
                this.fineId = Integer.parseInt(parts[0]);
                this.isUnpaid = parts.length > 1 && parts[1].equals("UNPAID");
            }
            if (isUnpaid) {
                panel.add(btnPay);
                panel.setBackground(new Color(255, 250, 230));
            } else {
                panel.add(lblPaid);
                panel.setBackground(new Color(235, 255, 240));
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return fineId; }
    }
}