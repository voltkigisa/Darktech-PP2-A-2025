package com.library.pos.views.admin.borrowings;

import com.library.pos.controllers.BorrowingController;
import com.library.pos.dao.BookDAO;
import com.library.pos.dao.MemberDAO;
import com.library.pos.models.Book;
import com.library.pos.models.Borrowing;
import com.library.pos.models.BorrowingDetail;
import com.library.pos.models.Member;
import com.library.pos.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CreateBorrowingView - Form dialog for creating new borrowing
 */
public class CreateBorrowingView extends JDialog {
    private BorrowingController controller;
    private IndexBorrowingView parentView;
    private MemberDAO memberDAO;
    private BookDAO bookDAO;

    private JComboBox<MemberItem> cmbMember;
    private JSpinner spnBorrowDate;
    private JSpinner spnDueDate;
    private JComboBox<BookItem> cmbBook;
    private JSpinner spnQuantity;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JButton btnAddBook, btnRemoveBook, btnSave, btnCancel;

    private List<BorrowingDetail> borrowingDetails = new ArrayList<>();

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color SECONDARY_COLOR = new Color(149, 165, 166);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    // Current user (petugas)
    private User currentUser;

    public CreateBorrowingView(Window parent, IndexBorrowingView parentView) {
        super(parent, "Tambah Peminjaman Baru", ModalityType.APPLICATION_MODAL);
        this.parentView = parentView;
        this.controller = new BorrowingController();
        this.memberDAO = new MemberDAO();
        this.bookDAO = new BookDAO();

        // Get current user from parent dashboard
        this.currentUser = getCurrentUserFromParent(parent);

        initComponents();
        loadMembers();
        loadBooks();
        setLocationRelativeTo(parent);
    }

    private User getCurrentUserFromParent(Window parent) {
        // Default user if not found
        return new User(1, "admin", "", "Administrator", "ADMIN");
    }

    private void initComponents() {
        setSize(800, 700);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(createFormPanel());
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

        JLabel lblTitle = new JLabel("TAMBAH PEMINJAMAN BARU");
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

        // Info panel
        formContainer.add(createInfoPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Member selection
        cmbMember = new JComboBox<>();
        cmbMember.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMember.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formContainer.add(createFormField("Pilih Anggota *", cmbMember, "Wajib dipilih"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Date fields
        JPanel datePanel = new JPanel(new GridLayout(1, 2, 15, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        datePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // Borrow date spinner
        spnBorrowDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor borrowEditor = new JSpinner.DateEditor(spnBorrowDate, "dd/MM/yyyy");
        spnBorrowDate.setEditor(borrowEditor);
        spnBorrowDate.setValue(new Date());
        spnBorrowDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        datePanel.add(createFormField("Tanggal Peminjaman *", spnBorrowDate, "Hari ini"));

        // Due date spinner (default 7 days from now)
        spnDueDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dueEditor = new JSpinner.DateEditor(spnDueDate, "dd/MM/yyyy");
        spnDueDate.setEditor(dueEditor);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 7);
        spnDueDate.setValue(cal.getTime());
        spnDueDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        datePanel.add(createFormField("Tanggal Pengembalian *", spnDueDate, "Batas waktu peminjaman"));

        formContainer.add(datePanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Book selection section
        JLabel lblBooks = new JLabel("Daftar Buku yang Dipinjam");
        lblBooks.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBooks.setForeground(DARK_COLOR);
        lblBooks.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(lblBooks);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Book add panel
        JPanel bookAddPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bookAddPanel.setBackground(Color.WHITE);
        bookAddPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbBook = new JComboBox<>();
        cmbBook.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbBook.setPreferredSize(new Dimension(350, 40));

        spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnQuantity.setPreferredSize(new Dimension(80, 40));

        btnAddBook = createSmallButton("Tambah", SUCCESS_COLOR);
        btnAddBook.setPreferredSize(new Dimension(100, 40));
        btnAddBook.addActionListener(e -> addBookToList());

        bookAddPanel.add(new JLabel("Buku:"));
        bookAddPanel.add(cmbBook);
        bookAddPanel.add(new JLabel("Jumlah:"));
        bookAddPanel.add(spnQuantity);
        bookAddPanel.add(btnAddBook);

        formContainer.add(bookAddPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Book table
        String[] columns = { "ID", "Judul Buku", "ISBN", "Jumlah", "Hapus" };
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        bookTable = new JTable(bookTableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setRowHeight(40);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(new Color(240, 240, 240));

        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        // Add delete button to table
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new RemoveButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new RemoveButtonEditor(new JCheckBox()));

        JScrollPane tableScroll = new JScrollPane(bookTable);
        tableScroll.setPreferredSize(new Dimension(0, 150));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        formContainer.add(tableScroll);

        mainPanel.add(formContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(39, 174, 96, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Form Peminjaman Baru");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(new Color(39, 174, 96));

        JLabel lblDetail = new JLabel("<html>Pilih anggota, tentukan tanggal peminjaman dan pengembalian, " +
                "lalu tambahkan buku yang akan dipinjam.</html>");
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetail.setForeground(new Color(52, 73, 94));

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
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(127, 140, 141));
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(hintLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnSave = createButton("SIMPAN", SUCCESS_COLOR);
        btnSave.addActionListener(e -> saveBorrowing());

        btnCancel = createButton("BATAL", SECONDARY_COLOR);
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
        button.setPreferredSize(new Dimension(160, 42));

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
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadMembers() {
        try {
            List<Member> members = memberDAO.getAll();
            cmbMember.addItem(new MemberItem(0, "-- Pilih Anggota --"));
            for (Member m : members) {
                cmbMember.addItem(new MemberItem(m.getId(), m.getMember_code() + " - " + m.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data anggota!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAll();
        cmbBook.addItem(new BookItem(0, "-- Pilih Buku --", null, 0));
        for (Book b : books) {
            if (b.getStock() > 0) {
                cmbBook.addItem(new BookItem(b.getId(), b.getTitle() + " (Stok: " + b.getStock() + ")",
                        b.getIsbn(), b.getStock()));
            }
        }
    }

    private void addBookToList() {
        BookItem selectedBook = (BookItem) cmbBook.getSelectedItem();
        if (selectedBook == null || selectedBook.id == 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih buku!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity = (int) spnQuantity.getValue();

        // Check if book already in list
        for (int i = 0; i < bookTableModel.getRowCount(); i++) {
            if ((int) bookTableModel.getValueAt(i, 0) == selectedBook.id) {
                JOptionPane.showMessageDialog(this, "Buku sudah ada dalam daftar!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Check stock
        if (quantity > selectedBook.stock) {
            JOptionPane.showMessageDialog(this, "Jumlah melebihi stok tersedia!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Add to table
        BorrowingDetail detail = new BorrowingDetail(selectedBook.id, quantity);
        detail.setBookTitle(selectedBook.title.split(" \\(Stok:")[0]);
        detail.setBookIsbn(selectedBook.isbn);
        borrowingDetails.add(detail);

        Object[] row = {
                selectedBook.id,
                detail.getBookTitle(),
                selectedBook.isbn != null ? selectedBook.isbn : "-",
                quantity,
                "Hapus"
        };
        bookTableModel.addRow(row);

        // Reset selection
        cmbBook.setSelectedIndex(0);
        spnQuantity.setValue(1);
    }

    private void removeBookFromList(int row) {
        if (row >= 0 && row < borrowingDetails.size()) {
            borrowingDetails.remove(row);
            bookTableModel.removeRow(row);
        }
    }

    private void saveBorrowing() {
        // Get member
        MemberItem selectedMember = (MemberItem) cmbMember.getSelectedItem();
        if (selectedMember == null || selectedMember.id == 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih anggota!", "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get dates
        Date borrowDateUtil = (Date) spnBorrowDate.getValue();
        Date dueDateUtil = (Date) spnDueDate.getValue();

        LocalDate borrowDate = borrowDateUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dueDate = dueDateUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Create borrowing object
        Borrowing borrowing = new Borrowing();
        borrowing.setMemberId(selectedMember.id);
        borrowing.setUserId(currentUser.getId());
        borrowing.setBorrowDate(borrowDate);
        borrowing.setDueDate(dueDate);

        // Create with validation
        if (controller.createBorrowing(borrowing, borrowingDetails)) {
            parentView.loadData();
            // Refresh dashboard statistics if available
            if (parentView.getDashboard() != null) {
                parentView.getDashboard().refreshDashboardStats();
            }
            dispose();
        }
    }

    // Helper classes for ComboBox items
    static class MemberItem {
        int id;
        String display;

        MemberItem(int id, String display) {
            this.id = id;
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }

    static class BookItem {
        int id;
        String title;
        String isbn;
        int stock;

        BookItem(int id, String title, String isbn, int stock) {
            this.id = id;
            this.title = title;
            this.isbn = isbn;
            this.stock = stock;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    // Remove button renderer
    class RemoveButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public RemoveButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setForeground(Color.WHITE);
            setBackground(DANGER_COLOR);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Hapus");
            return this;
        }
    }

    // Remove button editor
    class RemoveButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public RemoveButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Hapus");
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setForeground(Color.WHITE);
            button.setBackground(DANGER_COLOR);
            button.setBorderPainted(false);

            button.addActionListener(e -> {
                fireEditingStopped();
                removeBookFromList(row);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Hapus";
        }
    }
}
