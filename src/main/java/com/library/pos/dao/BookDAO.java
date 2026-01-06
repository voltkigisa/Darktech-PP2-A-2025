package com.library.pos.dao;

import com.library.pos.config.DatabaseConfig;
import com.library.pos.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookDAO - Data Access Object for books table
 */
public class BookDAO {

    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public Book getById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Book getByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Book getByIssn(String issn) {
        String sql = "SELECT * FROM books WHERE issn = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, issn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBook(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean create(Book book) {
        String sql = "INSERT INTO books (isbn, issn, title, author, publisher, number_of_pages, " +
                "genre, language, publication_place, year, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getIssn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            if (book.getNumberOfPages() != null) {
                pstmt.setInt(6, book.getNumberOfPages());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setString(7, book.getGenre());
            pstmt.setString(8, book.getLanguage());
            pstmt.setString(9, book.getPublicationPlace());
            if (book.getYear() != null) {
                pstmt.setInt(10, book.getYear());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }
            pstmt.setInt(11, book.getStock());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Book book) {
        String sql = "UPDATE books SET isbn = ?, issn = ?, title = ?, author = ?, publisher = ?, " +
                "number_of_pages = ?, genre = ?, language = ?, publication_place = ?, year = ?, stock = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getIssn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            if (book.getNumberOfPages() != null) {
                pstmt.setInt(6, book.getNumberOfPages());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setString(7, book.getGenre());
            pstmt.setString(8, book.getLanguage());
            pstmt.setString(9, book.getPublicationPlace());
            if (book.getYear() != null) {
                pstmt.setInt(10, book.getYear());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }
            pstmt.setInt(11, book.getStock());
            pstmt.setInt(12, book.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isbnExists(String isbn, int excludeId) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ? AND id != ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean issnExists(String issn, int excludeId) {
        if (issn == null || issn.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM books WHERE issn = ? AND id != ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, issn);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setIssn(rs.getString("issn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));

        int numberOfPages = rs.getInt("number_of_pages");
        book.setNumberOfPages(rs.wasNull() ? null : numberOfPages);

        book.setGenre(rs.getString("genre"));
        book.setLanguage(rs.getString("language"));
        book.setPublicationPlace(rs.getString("publication_place"));

        int year = rs.getInt("year");
        book.setYear(rs.wasNull() ? null : year);

        book.setStock(rs.getInt("stock"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            book.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            book.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return book;
    }
}
