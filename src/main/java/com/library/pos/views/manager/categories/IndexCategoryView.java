package com.library.pos.views.manager.categories;

import com.library.pos.controllers.CategoryController;
import com.library.pos.models.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * IndexCategoryView - Admin category list view
 */
public class IndexCategoryView extends JPanel {
    private CategoryController controller;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private Runnable onBackCallback;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color GREEN_EMERALD = new Color(16, 185, 129);

    public IndexCategoryView() {
        this.controller = new CategoryController();
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

        JLabel lblTitle = new JLabel("MANAJEMEN CATEGORIES");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(DARK_COLOR);

        JLabel lblSubtitle = new JLabel("Kelola Kategori Buku Perpustakaan");
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
                    button.setBackground(new Color(5, 150, 105)); // Warna hijau lebih gelap saat hover
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
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel tableTitle = new JLabel("DAFTAR CATEGORIES");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(DARK_COLOR);

        btnAdd = createButton("TAMBAH CATEGORY", SUCCESS_COLOR);
        btnAdd.addActionListener(e -> openCreateDialog());

        topPanel.add(tableTitle, BorderLayout.WEST);
        topPanel.add(btnAdd, BorderLayout.EAST);

        String[] columns = {"ID", "Category Name", "Created Date", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? JPanel.class : Object.class;
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryTable.setForeground(Color.BLACK);
        categoryTable.setRowHeight(55);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setShowVerticalLines(true);
        categoryTable.setGridColor(new Color(220, 220, 220));

        categoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        categoryTable.getTableHeader().setBackground(DARK_COLOR);
        categoryTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        categoryTable.getTableHeader().setReorderingAllowed(false);

        categoryTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        categoryTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        categoryTable.getColumnModel().getColumn(3).setPreferredWidth(280);

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
        
        for (int i = 0; i < 3; i++) {
            categoryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        categoryTable.getColumnModel().getColumn(3).setCellRenderer(new ActionButtonRenderer());
        categoryTable.getColumnModel().getColumn(3).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(categoryTable);
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
        tableModel.setRowCount(0);
        List<Category> categories = controller.getAllCategories();

        for (Category category : categories) {
            Object[] row = {
                    category.getId(),
                    category.getName(),
                    category.getCreatedAt() != null ? category.getCreatedAt().toString().substring(0, 19).replace("T", " ") : "-",
                    category.getId()
            };
            tableModel.addRow(row);
        }
    }

    private void openCreateDialog() {
        CreateCategoryView createView = new CreateCategoryView(this);
        createView.setVisible(true);
    }

    private void openShowDialog(int categoryId) {
        Category category = controller.getCategoryById(categoryId);
        if (category != null) {
        }
    }

    private void openEditDialog(int categoryId) {
        Category category = controller.getCategoryById(categoryId);
        if (category != null) {
        }
    }

    private void deleteCategory(int categoryId) {
        if (controller.deleteCategory(categoryId)) {
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
        private int categoryId;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);

            JButton btnShow = createSmallButton("Detail", new Color(52, 152, 219));
            JButton btnEdit = createSmallButton("Edit", PRIMARY_COLOR);
            JButton btnDelete = createSmallButton("Hapus", DANGER_COLOR);

            btnShow.addActionListener(e -> {
                fireEditingStopped();
                openShowDialog(categoryId);
            });
            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                openEditDialog(categoryId);
            });
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                deleteCategory(categoryId);
            });

            panel.add(btnShow);
            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            categoryId = (int) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return categoryId;
        }
    }
}