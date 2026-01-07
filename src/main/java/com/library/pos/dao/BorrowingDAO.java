package com.library.pos.dao;

import com.library.pos.config.DatabaseConfig;
import com.library.pos.models.Borrowing;
import com.library.pos.models.BorrowingDetail;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * BorrowingDAO - Data Access Object for borrowings table
 */
public class BorrowingDAO {

    /**
     * Get all borrowings with member and user info
     */
    public List<Borrowing> getAll() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT b.*, m.name as member_name, m.member_code, u.name as user_name " +
                "FROM borrowings b " +
                "LEFT JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "ORDER BY b.id DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowings;
    }

    /**
     * Get borrowing by ID with full details
     */
    public Borrowing getById(int id) {
        String sql = "SELECT b.*, m.name as member_name, m.member_code, u.name as user_name " +
                "FROM borrowings b " +
                "LEFT JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "WHERE b.id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBorrowing(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get borrowing details (books) for a borrowing
     */
    public List<BorrowingDetail> getDetailsByBorrowingId(int borrowingId) {
        List<BorrowingDetail> details = new ArrayList<>();
        String sql = "SELECT bd.*, b.title as book_title, b.isbn as book_isbn " +
                "FROM borrowing_details bd " +
                "LEFT JOIN books b ON bd.book_id = b.id " +
                "WHERE bd.borrowing_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, borrowingId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return details;
    }

    /**
     * Create new borrowing with details (transaction)
     */
    public boolean create(Borrowing borrowing, List<BorrowingDetail> details) {
        String sqlBorrowing = "INSERT INTO borrowings (member_id, user_id, borrow_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, 'BORROWED')";
        String sqlDetail = "INSERT INTO borrowing_details (borrowing_id, book_id, quantity) VALUES (?, ?, ?)";
        String sqlUpdateStock = "UPDATE books SET stock = stock - ? WHERE id = ? AND stock >= ?";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Insert borrowing
            int borrowingId;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlBorrowing, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, borrowing.getMemberId());
                pstmt.setInt(2, borrowing.getUserId());
                pstmt.setDate(3, Date.valueOf(borrowing.getBorrowDate()));
                pstmt.setDate(4, Date.valueOf(borrowing.getDueDate()));
                pstmt.executeUpdate();

                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    borrowingId = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Insert details and update stock
            for (BorrowingDetail detail : details) {
                // Update book stock
                try (PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock)) {
                    pstmtStock.setInt(1, detail.getQuantity());
                    pstmtStock.setInt(2, detail.getBookId());
                    pstmtStock.setInt(3, detail.getQuantity());
                    int updated = pstmtStock.executeUpdate();
                    if (updated == 0) {
                        conn.rollback();
                        return false; // Not enough stock
                    }
                }

                // Insert detail
                try (PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail)) {
                    pstmtDetail.setInt(1, borrowingId);
                    pstmtDetail.setInt(2, detail.getBookId());
                    pstmtDetail.setInt(3, detail.getQuantity());
                    pstmtDetail.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete borrowing and restore book stock
     */
    public boolean delete(int id) {
        String sqlGetDetails = "SELECT book_id, quantity FROM borrowing_details WHERE borrowing_id = ?";
        String sqlRestoreStock = "UPDATE books SET stock = stock + ? WHERE id = ?";
        String sqlDeleteDetails = "DELETE FROM borrowing_details WHERE borrowing_id = ?";
        String sqlDeleteBorrowing = "DELETE FROM borrowings WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConfig.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Get details to restore stock
            List<int[]> detailsToRestore = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetDetails)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    detailsToRestore.add(new int[] { rs.getInt("book_id"), rs.getInt("quantity") });
                }
            }

            // Restore book stock
            for (int[] detail : detailsToRestore) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlRestoreStock)) {
                    pstmt.setInt(1, detail[1]); // quantity
                    pstmt.setInt(2, detail[0]); // book_id
                    pstmt.executeUpdate();
                }
            }

            // Delete details
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDetails)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }

            // Delete borrowing
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteBorrowing)) {
                pstmt.setInt(1, id);
                int deleted = pstmt.executeUpdate();
                if (deleted == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if member exists
     */
    public boolean memberExists(int memberId) {
        String sql = "SELECT COUNT(*) FROM members WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if book has enough stock
     */
    public boolean hasEnoughStock(int bookId, int quantity) {
        String sql = "SELECT stock FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock") >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Count all borrowings
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM borrowings";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Count active borrowings (status = 'BORROWED' or 'OVERDUE')
     */
    public int countActive() {
        String sql = "SELECT COUNT(*) FROM borrowings WHERE status IN ('BORROWED', 'OVERDUE')";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setId(rs.getInt("id"));
        borrowing.setMemberId(rs.getInt("member_id"));
        borrowing.setUserId(rs.getInt("user_id"));

        Date borrowDate = rs.getDate("borrow_date");
        if (borrowDate != null) {
            borrowing.setBorrowDate(borrowDate.toLocalDate());
        }

        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            borrowing.setDueDate(dueDate.toLocalDate());
        }

        borrowing.setStatus(rs.getString("status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            borrowing.setCreatedAt(createdAt.toLocalDateTime());
        }

        // Joined fields
        borrowing.setMemberName(rs.getString("member_name"));
        borrowing.setMemberCode(rs.getString("member_code"));
        borrowing.setUserName(rs.getString("user_name"));

        return borrowing;
    }

    private BorrowingDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        BorrowingDetail detail = new BorrowingDetail();
        detail.setId(rs.getInt("id"));
        detail.setBorrowingId(rs.getInt("borrowing_id"));
        detail.setBookId(rs.getInt("book_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setReturnedQuantity(rs.getInt("returned_quantity"));
        detail.setBookTitle(rs.getString("book_title"));
        detail.setBookIsbn(rs.getString("book_isbn"));
        return detail;
    }
}
