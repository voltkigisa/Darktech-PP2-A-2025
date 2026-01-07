package com.library.pos.views.manager.books;

import com.library.pos.models.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ShowBookView - Detail view for a single Book
 */
public class ShowBookView extends JDialog {
    private IndexBookView parentView;
    private Book book;
    private JButton btnClose, btnEdit;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color INFO_BG = new Color(52, 152, 219, 20);

    public ShowBookView(IndexBookView parent, Book book) {
        super(JOptionPane.getFrameForComponent(parent), "Book Details", true);
        this.parentView = parent;
        this.book = book;
        initComponents();
        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(700, 850);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(createDetailPanel());
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

        JLabel lblTitle = new JLabel("BOOK DETAILS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle);

        return headerPanel;
    }

    private JPanel createDetailPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JPanel detailContainer = new JPanel();
        detailContainer.setLayout(new BoxLayout(detailContainer, BoxLayout.Y_AXIS));
        detailContainer.setBackground(Color.WHITE);
        detailContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));

        // Icon
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JLabel iconLabel = new JLabel("BOOK");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        iconLabel.setForeground(new Color(52, 152, 219));
        iconPanel.add(iconLabel);

        detailContainer.add(iconPanel);

        // Detail fields
        detailContainer.add(createDetailField("ID", String.valueOf(book.getId())));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Title", book.getTitle()));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("ISBN", book.getIsbn() != null ? book.getIsbn() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("ISSN", book.getIssn() != null ? book.getIssn() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Author", book.getAuthor() != null ? book.getAuthor() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Publisher", book.getPublisher() != null ? book.getPublisher() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Number of Pages", book.getNumberOfPages() != null ? book.getNumberOfPages().toString() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Genre", book.getGenre() != null ? book.getGenre() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Language", book.getLanguage() != null ? book.getLanguage() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Publication Place", book.getPublicationPlace() != null ? book.getPublicationPlace() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Year", book.getYear() != null ? book.getYear().toString() : "-"));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        detailContainer.add(createDetailField("Stock", String.valueOf(book.getStock())));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        String createdAt = book.getCreatedAt() != null ?
                book.getCreatedAt().toString().substring(0, 19).replace("T", " ") : "-";
        detailContainer.add(createDetailField("Created Date", createdAt));
        detailContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        String updatedAt = book.getUpdatedAt() != null ?
                book.getUpdatedAt().toString().substring(0, 19).replace("T", " ") : "-";
        detailContainer.add(createDetailField("Last Updated", updatedAt));

        mainPanel.add(detailContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createDetailField(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(INFO_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLabel.setForeground(new Color(52, 73, 94));
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblValue.setForeground(DARK_COLOR);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(lblValue);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnEdit = createButton("EDIT DATA", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> {
            dispose();
            EditBookView editView = new EditBookView(parentView, book);
            editView.setVisible(true);
        });

        btnClose = createButton("CLOSE", new Color(149, 165, 166));
        btnClose.addActionListener(e -> dispose());

        panel.add(btnEdit);
        panel.add(btnClose);

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
}
