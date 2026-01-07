package com.library.pos.views.manager.books;

import com.library.pos.controllers.BookController;
import com.library.pos.models.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * IndexBookView - Manager book list view
 */
public class IndexBookView extends JPanel {
    private BookController controller;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private Runnable onBackCallback;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color GREEN_EMERALD = new Color(16, 185, 129); // Warna untuk tombol kembali

    public IndexBookView() {
        this.controller = new BookController();
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

        JLabel lblTitle = new JLabel("MANAJEMEN BOOKS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola Data Buku Perpustakaan");
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

    private JPanel createTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(LIGHT_BG);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR BOOKS");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        btnAdd = createButton("TAMBAH BOOK", SUCCESS_COLOR);
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

        String[] genres = { "Semua Genre", "Novel", "Fiksi", "Non-Fiksi", "Sejarah", "Sains", "Teknologi", "Biografi" };
        filterGenre = new JComboBox<>(genres);
        filterGenre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterGenre.setPreferredSize(new Dimension(130, 35));
        filterGenre.addActionListener(e -> filterData());

        searchFilterPanel.add(new JLabel("Filter:"));
        searchFilterPanel.add(filterGenre);
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(btnAdd);

        topPanel.add(searchFilterPanel, BorderLayout.EAST);

        String[] columns = {"ID", "ISBN", "Title", "Author", "Publisher", "Stock", "Aksi"};
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

        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookTable.setForeground(Color.BLACK);
        bookTable.setRowHeight(55);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setShowVerticalLines(true);
        bookTable.setGridColor(new Color(220, 220, 220));

        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(DARK_COLOR);
        bookTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        bookTable.getTableHeader().setReorderingAllowed(false);

        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(280);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        };

        for (int i = 0; i < 6; i++) {
            bookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        bookTable.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        bookTable.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(bookTable);
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
        button.setPreferredSize(new Dimension(180, 42));

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
        allBooks = controller.getAllBooks();
        filterData();
    }

    private void filterData() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().toLowerCase();
        if (searchText.equals("search..."))
            searchText = "";
        String selectedGenre = (String) filterGenre.getSelectedItem();

        for (Book book : allBooks) {
            boolean matchesSearch = searchText.isEmpty() ||
                    book.getTitle().toLowerCase().contains(searchText) ||
                    (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(searchText)) ||
                    (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(searchText));

            boolean matchesGenre = selectedGenre.equals("Semua Genre") ||
                    (book.getGenre() != null && book.getGenre().equalsIgnoreCase(selectedGenre));

            if (matchesSearch && matchesGenre) {
                Object[] row = {
                        book.getId(),
                        book.getIsbn() != null ? book.getIsbn() : "-",
                        book.getTitle(),
                        book.getAuthor() != null ? book.getAuthor() : "-",
                        book.getPublisher() != null ? book.getPublisher() : "-",
                        book.getStock(),
                        book.getId()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openCreateDialog() {
        CreateBookView createView = new CreateBookView(this);
        createView.setVisible(true);
    }

    private void openShowDialog(int bookId) {
        Book book = controller.getBookById(bookId);
        if (book != null) {
            ShowBookView showView = new ShowBookView(this, book);
            showView.setVisible(true);
        }
    }

    private void openEditDialog(int bookId) {
        Book book = controller.getBookById(bookId);
        if (book != null) {
            EditBookView editView = new EditBookView(this, book);
            editView.setVisible(true);
        }
    }

    private void deleteBook(int bookId) {
        if (controller.deleteBook(bookId)) {
            loadData();
        }
    }

    class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setBackground(Color.WHITE);
            add(createSmallButton("Detail", new Color(52, 152, 219)));
            add(createSmallButton("Edit", PRIMARY_COLOR));
            add(createSmallButton("Hapus", DANGER_COLOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private int bookId;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);

            JButton btnShow = createSmallButton("Detail", new Color(52, 152, 219));
            JButton btnEdit = createSmallButton("Edit", PRIMARY_COLOR);
            JButton btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            btnShow.addActionListener(e -> {
                fireEditingStopped();
                openShowDialog(bookId);
            });
            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                openEditDialog(bookId);
            });
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                deleteBook(bookId);
            });

            panel.add(btnShow);
            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.bookId = (int) value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return bookId;
        }
    }
}