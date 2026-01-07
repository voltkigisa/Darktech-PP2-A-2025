package com.library.pos.views.manager.books;

import com.library.pos.controllers.BookController;
import com.library.pos.models.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CreateBookView - Form for creating new book
 */
public class CreateBookView extends JDialog {
    private BookController controller;
    private IndexBookView parentView;
    private JTextField txtIsbn, txtIssn, txtTitle, txtAuthor, txtPublisher;
    private JTextField txtPages, txtGenre, txtLanguage, txtPublicationPlace, txtYear, txtStock;
    private JButton btnSave, btnCancel;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color SECONDARY_COLOR = new Color(149, 165, 166);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);

    public CreateBookView(IndexBookView parent) {
        super(JOptionPane.getFrameForComponent(parent), "Add New Book", true);
        this.parentView = parent;
        this.controller = new BookController();
        initComponents();
        setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
    }

    private void initComponents() {
        setSize(750, 800);
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

        JLabel lblTitle = new JLabel("ADD NEW BOOK");
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
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));

        // Info panel
        JPanel infoPanel = createInfoPanel();
        formContainer.add(infoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Title field (required)
        txtTitle = createTextField();
        formContainer.add(createFormField("Book Title *", txtTitle, "Required field"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // ISBN and ISSN fields
        JPanel isbnIssnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        isbnIssnPanel.setBackground(Color.WHITE);
        isbnIssnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        isbnIssnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        txtIsbn = createTextField();
        isbnIssnPanel.add(createFormField("ISBN", txtIsbn, "Optional, max 20 chars"));

        txtIssn = createTextField();
        isbnIssnPanel.add(createFormField("ISSN", txtIssn, "Optional, max 20 chars"));

        formContainer.add(isbnIssnPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Author and Publisher fields
        JPanel authorPublisherPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        authorPublisherPanel.setBackground(Color.WHITE);
        authorPublisherPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorPublisherPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        txtAuthor = createTextField();
        authorPublisherPanel.add(createFormField("Author", txtAuthor, "Optional, max 100 chars"));

        txtPublisher = createTextField();
        authorPublisherPanel.add(createFormField("Publisher", txtPublisher, "Optional, max 100 chars"));

        formContainer.add(authorPublisherPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Pages, Year, Stock fields
        JPanel numericPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        numericPanel.setBackground(Color.WHITE);
        numericPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        numericPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        txtPages = createTextField();
        numericPanel.add(createFormField("Number of Pages", txtPages, "Optional"));

        txtYear = createTextField();
        numericPanel.add(createFormField("Year", txtYear, "Optional, 4 digits"));

        txtStock = createTextField();
        txtStock.setText("0");
        numericPanel.add(createFormField("Stock *", txtStock, "Required, >= 0"));

        formContainer.add(numericPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Genre and Language fields
        JPanel genreLanguagePanel = new JPanel(new GridLayout(1, 2, 15, 0));
        genreLanguagePanel.setBackground(Color.WHITE);
        genreLanguagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreLanguagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        txtGenre = createTextField();
        genreLanguagePanel.add(createFormField("Genre", txtGenre, "Optional, max 50 chars"));

        txtLanguage = createTextField();
        genreLanguagePanel.add(createFormField("Language", txtLanguage, "Optional, max 50 chars"));

        formContainer.add(genreLanguagePanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Publication Place field
        txtPublicationPlace = createTextField();
        formContainer.add(createFormField("Publication Place", txtPublicationPlace, "Optional, max 100 chars"));

        mainPanel.add(formContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(39, 174, 96, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Add New Book");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfo.setForeground(new Color(39, 174, 96));

        JLabel lblDetail = new JLabel("<html>Create a new book record. Title and Stock are required fields. ISBN and ISSN must be unique.</html>");
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

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(0, 40));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panel.setBackground(LIGHT_BG);

        btnSave = createButton("SAVE", SUCCESS_COLOR);
        btnSave.addActionListener(e -> saveBook());

        btnCancel = createButton("CANCEL", SECONDARY_COLOR);
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

    private void saveBook() {
        Book book = new Book();
        
        // Get and set values
        String isbn = txtIsbn.getText().trim();
        book.setIsbn(isbn.isEmpty() ? null : isbn);
        
        String issn = txtIssn.getText().trim();
        book.setIssn(issn.isEmpty() ? null : issn);
        
        book.setTitle(txtTitle.getText().trim());
        
        String author = txtAuthor.getText().trim();
        book.setAuthor(author.isEmpty() ? null : author);
        
        String publisher = txtPublisher.getText().trim();
        book.setPublisher(publisher.isEmpty() ? null : publisher);
        
        try {
            String pagesStr = txtPages.getText().trim();
            if (!pagesStr.isEmpty()) {
                book.setNumberOfPages(Integer.parseInt(pagesStr));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Number of pages must be a valid number!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String genre = txtGenre.getText().trim();
        book.setGenre(genre.isEmpty() ? null : genre);
        
        String language = txtLanguage.getText().trim();
        book.setLanguage(language.isEmpty() ? null : language);
        
        String publicationPlace = txtPublicationPlace.getText().trim();
        book.setPublicationPlace(publicationPlace.isEmpty() ? null : publicationPlace);
        
        try {
            String yearStr = txtYear.getText().trim();
            if (!yearStr.isEmpty()) {
                book.setYear(Integer.parseInt(yearStr));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            book.setStock(Integer.parseInt(txtStock.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock must be a valid number!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controller.createBook(book)) {
            parentView.loadData();
            dispose();
        }
    }
}
