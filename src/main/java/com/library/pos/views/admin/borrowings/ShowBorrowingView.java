package com.library.pos.views.admin.borrowings;

import com.library.pos.controllers.BorrowingController;
import com.library.pos.models.Borrowing;
import com.library.pos.models.BorrowingDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * ShowBorrowingView - Dialog to display borrowing details with overdue warning
 */
public class ShowBorrowingView extends JDialog {
    private BorrowingController controller;
    private Borrowing borrowing;
    private List<BorrowingDetail> details;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public ShowBorrowingView(Window parent, int borrowingId) {
        super(parent, "Detail Peminjaman", ModalityType.APPLICATION_MODAL);
        this.controller = new BorrowingController();
        this.borrowing = controller.getBorrowingById(borrowingId);
        this.details = controller.getBorrowingDetails(borrowingId);

        if (borrowing == null) {
            JOptionPane.showMessageDialog(parent, "Data peminjaman tidak ditemukan!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(700, 600);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("DETAIL PEMINJAMAN #" + borrowing.getId());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(Color.WHITE);
        contentContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)));

        // Overdue warning
        if (borrowing.isOverdue()) {
            contentContainer.add(createOverdueWarningPanel());
            contentContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        // Info cards
        JPanel infoGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        infoGrid.setBackground(Color.WHITE);
        infoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Card 1: Borrowing ID & Petugas
        infoGrid.add(createInfoCard("ID Peminjaman", String.valueOf(borrowing.getId()),
                "Petugas: " + borrowing.getUserName(), PRIMARY_COLOR));

        // Card 2: Member info
        infoGrid.add(createInfoCard("Anggota", borrowing.getMemberName(),
                "Kode: " + borrowing.getMemberCode(), SUCCESS_COLOR));

        // Card 3: Borrow date
        String borrowDateStr = borrowing.getBorrowDate() != null ? borrowing.getBorrowDate().format(dateFormatter)
                : "-";
        infoGrid.add(createInfoCard("Tanggal Pinjam", borrowDateStr,
                "Tanggal peminjaman", PRIMARY_COLOR));

        // Card 4: Due date with overdue info
        String dueDateStr = borrowing.getDueDate() != null ? borrowing.getDueDate().format(dateFormatter) : "-";
        String dueDateHint = "Batas pengembalian";
        Color dueDateColor = SUCCESS_COLOR;

        if (borrowing.isOverdue()) {
            long daysOverdue = ChronoUnit.DAYS.between(borrowing.getDueDate(), java.time.LocalDate.now());
            dueDateHint = "⚠ Terlambat " + daysOverdue + " hari!";
            dueDateColor = DANGER_COLOR;
        }
        infoGrid.add(createInfoCard("Tanggal Kembali", dueDateStr, dueDateHint, dueDateColor));

        contentContainer.add(infoGrid);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("Status: ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(DARK_COLOR);

        JLabel statusValue = new JLabel(borrowing.isOverdue() ? "TERLAMBAT" : borrowing.getStatus());
        statusValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusValue.setForeground(borrowing.isOverdue() ? DANGER_COLOR
                : (borrowing.getStatus().equals("RETURNED") ? SUCCESS_COLOR : PRIMARY_COLOR));
        statusValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusValue.setOpaque(true);
        statusValue.setBackground(borrowing.isOverdue() ? new Color(255, 235, 235)
                : (borrowing.getStatus().equals("RETURNED") ? new Color(235, 255, 240) : new Color(235, 245, 255)));

        statusPanel.add(statusLabel);
        statusPanel.add(statusValue);

        contentContainer.add(statusPanel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Books section
        JLabel lblBooks = new JLabel("Daftar Buku yang Dipinjam");
        lblBooks.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBooks.setForeground(DARK_COLOR);
        lblBooks.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentContainer.add(lblBooks);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Books table
        String[] columns = { "No", "Judul Buku", "ISBN", "Jumlah" };
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int no = 1;
        for (BorrowingDetail detail : details) {
            Object[] row = {
                    no++,
                    detail.getBookTitle(),
                    detail.getBookIsbn() != null ? detail.getBookIsbn() : "-",
                    detail.getQuantity()
            };
            tableModel.addRow(row);
        }

        JTable bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setRowHeight(40);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(new Color(240, 240, 240));

        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(70);

        JScrollPane tableScroll = new JScrollPane(bookTable);
        tableScroll.setPreferredSize(new Dimension(0, 150));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        contentContainer.add(tableScroll);

        mainPanel.add(contentContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createOverdueWarningPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(231, 76, 60, 30));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        long daysOverdue = ChronoUnit.DAYS.between(borrowing.getDueDate(), java.time.LocalDate.now());

        JLabel lblWarning = new JLabel("⚠ PEMINJAMAN TERLAMBAT!");
        lblWarning.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWarning.setForeground(DANGER_COLOR);

        JLabel lblDetail = new JLabel("<html>Peminjaman ini telah melewati batas waktu pengembalian selama <b>" +
                daysOverdue + " hari</b>. Segera hubungi anggota untuk pengembalian buku.</html>");
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDetail.setForeground(new Color(52, 73, 94));

        panel.add(lblWarning);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblDetail);

        return panel;
    }

    private JPanel createInfoCard(String title, String value, String hint, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15))));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(127, 140, 141));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(DARK_COLOR);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHint = new JLabel(hint);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(hint.contains("⚠") ? DANGER_COLOR : new Color(127, 140, 141));
        lblHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblValue);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblHint);

        return card;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        JButton btnClose = new JButton("TUTUP");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(PRIMARY_COLOR);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setOpaque(true);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setPreferredSize(new Dimension(160, 42));

        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnClose.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnClose.setBackground(PRIMARY_COLOR);
            }
        });

        btnClose.addActionListener(e -> dispose());

        panel.add(btnClose);

        return panel;
    }
}
