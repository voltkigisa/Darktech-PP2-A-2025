package com.library.pos.views.manager.reports;

import com.library.pos.dao.BorrowingDAO;
import com.library.pos.dao.FineDAO;
import com.library.pos.models.Borrowing;
import com.library.pos.models.BorrowingDetail;
import com.library.pos.models.Fine;
import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IndexReportView - Manager report view (Read-only)
 * Menampilkan semua transaksi dan denda dalam 1 bulan terakhir
 */
public class IndexReportView extends JPanel {
    private User currentUser;
    private Runnable onBackCallback;
    private BorrowingDAO borrowingDAO;
    private FineDAO fineDAO;

    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private JTable fineTable;
    private DefaultTableModel fineTableModel;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color GREEN_EMERALD = new Color(16, 185, 129);

    public IndexReportView(User currentUser) {
        this.currentUser = currentUser;
        this.borrowingDAO = new BorrowingDAO();
        this.fineDAO = new FineDAO();
        
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
        
        // Create tabbed pane for transactions and fines
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        tabbedPane.addTab("Transaksi Peminjaman", createTransactionPanel());
        tabbedPane.addTab("Denda", createFinePanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

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

        JLabel lblTitle = new JLabel("LAPORAN TRANSAKSI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Data Transaksi dan Denda 1 Bulan Terakhir");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(107, 114, 128));

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(createTopBackButton(), BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createTopBackButton() {
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
        button.setBackground(GREEN_EMERALD);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(120, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(5, 150, 105));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(GREEN_EMERALD);
            }
        });

        button.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });

        return button;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR TRANSAKSI PEMINJAMAN (1 BULAN TERAKHIR)");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        topPanel.add(tableTitle, BorderLayout.WEST);

        String[] columns = {"ID", "Member", "Book", "Borrow Date", "Due Date", "Return Date", "Status"};
        transactionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionTable.setForeground(Color.BLACK);
        transactionTable.setRowHeight(45);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setShowVerticalLines(true);
        transactionTable.setGridColor(new Color(220, 220, 220));

        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(DARK_COLOR);
        transactionTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        transactionTable.getTableHeader().setReorderingAllowed(false);

        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            transactionTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(topPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        panel.add(tableContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFinePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR DENDA (1 BULAN TERAKHIR)");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        topPanel.add(tableTitle, BorderLayout.WEST);

        String[] columns = {"ID", "Member", "Total Books", "Days Overdue", "Amount", "Payment Status", "Created Date"};
        fineTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        fineTable = new JTable(fineTableModel);
        fineTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fineTable.setForeground(Color.BLACK);
        fineTable.setRowHeight(45);
        fineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fineTable.setShowVerticalLines(true);
        fineTable.setGridColor(new Color(220, 220, 220));

        fineTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        fineTable.getTableHeader().setBackground(DARK_COLOR);
        fineTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        fineTable.getTableHeader().setReorderingAllowed(false);

        fineTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        fineTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        fineTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        fineTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        fineTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        fineTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        fineTable.getColumnModel().getColumn(6).setPreferredWidth(150);

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < fineTable.getColumnCount(); i++) {
            fineTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(fineTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContainer.add(topPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        panel.add(tableContainer, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        loadTransactionData();
        loadFineData();
    }

    private void loadTransactionData() {
        transactionTableModel.setRowCount(0);
        
        // Get borrowings from last month
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Borrowing> allBorrowings = borrowingDAO.getAll();
        
        // Filter borrowings from last month
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        for (Borrowing borrowing : allBorrowings) {
            // Check if borrowing is from last month
            if (borrowing.getCreatedAt() != null && borrowing.getCreatedAt().isAfter(oneMonthAgo)) {
                String memberInfo = borrowing.getMemberName() + " (" + borrowing.getMemberCode() + ")";
                String borrowDate = borrowing.getBorrowDate() != null ? borrowing.getBorrowDate().format(formatter) : "-";
                String dueDate = borrowing.getDueDate() != null ? borrowing.getDueDate().format(formatter) : "-";
                String status = borrowing.getStatus();
                
                // Get book details for this borrowing
                List<BorrowingDetail> details = borrowingDAO.getDetailsByBorrowingId(borrowing.getId());
                String bookTitles = details.stream()
                        .map(BorrowingDetail::getBookTitle)
                        .collect(Collectors.joining(", "));
                
                Object[] row = {
                    borrowing.getId(),
                    memberInfo,
                    bookTitles,
                    borrowDate,
                    dueDate,
                    "-", // Return date not tracked in current model
                    status
                };
                transactionTableModel.addRow(row);
            }
        }
    }

    private void loadFineData() {
        fineTableModel.setRowCount(0);
        
        // Get fines from last month
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Fine> allFines = fineDAO.getAll();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Fine fine : allFines) {
            // Check if fine is from last month
            if (fine.getCreatedAt() != null && fine.getCreatedAt().isAfter(oneMonthAgo)) {
                String memberInfo = fine.getMemberName() + " (" + fine.getMemberCode() + ")";
                String createdDate = fine.getCreatedAt() != null ? fine.getCreatedAt().format(formatter) : "-";
                String status = fine.isPaid() ? "PAID" : "UNPAID";
                
                Object[] row = {
                    fine.getId(),
                    memberInfo,
                    fine.getTotalBooks(),
                    fine.getDaysOverdue() + " hari",
                    fine.getFormattedAmount(),
                    status,
                    createdDate
                };
                fineTableModel.addRow(row);
            }
        }
    }
}
