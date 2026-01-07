package com.library.pos.controllers;

import com.library.pos.dao.BookDAO;
import com.library.pos.models.Book;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * BookController - Controller for Book operations
 */
public class BookController {
    private BookDAO bookDAO;

    public BookController() {
        this.bookDAO = new BookDAO();
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAll();
    }

    public Book getBookById(int id) {
        return bookDAO.getById(id);
    }

    public boolean createBook(Book book) {
        String validationError = validateBookInput(book, 0);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (bookDAO.create(book)) {
            JOptionPane.showMessageDialog(null, "Book created successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Failed to create book!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String validationError = validateBookInput(book, book.getId());
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (bookDAO.update(book)) {
            JOptionPane.showMessageDialog(null, "Book updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update book!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteBook(int id) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this book?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (bookDAO.delete(id)) {
                JOptionPane.showMessageDialog(null, "Book deleted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete book!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private String validateBookInput(Book book, int excludeId) {
        // Validate title (required)
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return "Title is required!";
        }

        if (book.getTitle().length() > 150) {
            return "Title must not exceed 150 characters!";
        }

        // Validate ISBN if provided
        if (book.getIsbn() != null && !book.getIsbn().trim().isEmpty()) {
            if (book.getIsbn().length() > 20) {
                return "ISBN must not exceed 20 characters!";
            }
            if (bookDAO.isbnExists(book.getIsbn(), excludeId)) {
                return "ISBN already exists!";
            }
        }

        // Validate ISSN if provided
        if (book.getIssn() != null && !book.getIssn().trim().isEmpty()) {
            if (book.getIssn().length() > 20) {
                return "ISSN must not exceed 20 characters!";
            }
            if (bookDAO.issnExists(book.getIssn(), excludeId)) {
                return "ISSN already exists!";
            }
        }

        // Validate author if provided
        if (book.getAuthor() != null && book.getAuthor().length() > 100) {
            return "Author must not exceed 100 characters!";
        }

        // Validate publisher if provided
        if (book.getPublisher() != null && book.getPublisher().length() > 100) {
            return "Publisher must not exceed 100 characters!";
        }

        // Validate number of pages
        if (book.getNumberOfPages() != null && book.getNumberOfPages() < 1) {
            return "Number of pages must be greater than 0!";
        }

        // Validate genre if provided
        if (book.getGenre() != null && book.getGenre().length() > 50) {
            return "Genre must not exceed 50 characters!";
        }

        // Validate language if provided
        if (book.getLanguage() != null && book.getLanguage().length() > 50) {
            return "Language must not exceed 50 characters!";
        }

        // Validate publication place if provided
        if (book.getPublicationPlace() != null && book.getPublicationPlace().length() > 100) {
            return "Publication place must not exceed 100 characters!";
        }

        // Validate year
        if (book.getYear() != null) {
            if (book.getYear() < 1000 || book.getYear() > 9999) {
                return "Year must be a valid 4-digit year!";
            }
        }

        // Validate stock (required)
        if (book.getStock() < 0) {
            return "Stock cannot be negative!";
        }

        return null;
    }
}
