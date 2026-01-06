package com.library.pos.dao;

import com.library.pos.config.DatabaseConfig;
import com.library.pos.models.Fine;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * FineDAO - Data Access Object for fines table
 */
public class FineDAO {

    /**
     * Get all fines with member info
     */
    public List<Fine> getAll() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT f.*, m.name as member_name, m.member_code, u.name as paid_by_name " +
                "FROM fines f " +
                "JOIN borrowings b ON f.borrowing_id = b.id " +
                "JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON f.paid_by = u.id " +
                "ORDER BY f.id DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                fines.add(mapResultSetToFine(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fines;
    }

    /**
     * Get unpaid fines only
     */
    public List<Fine> getUnpaidFines() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT f.*, m.name as member_name, m.member_code, u.name as paid_by_name " +
                "FROM fines f " +
                "JOIN borrowings b ON f.borrowing_id = b.id " +
                "JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON f.paid_by = u.id " +
                "WHERE f.payment_status = 'UNPAID' " +
                "ORDER BY f.id DESC";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                fines.add(mapResultSetToFine(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fines;
    }

    /**
     * Get fine by ID
     */
    public Fine getById(int id) {
        String sql = "SELECT f.*, m.name as member_name, m.member_code, u.name as paid_by_name " +
                "FROM fines f " +
                "JOIN borrowings b ON f.borrowing_id = b.id " +
                "JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON f.paid_by = u.id " +
                "WHERE f.id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFine(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get fine by borrowing ID
     */
    public Fine getByBorrowingId(int borrowingId) {
        String sql = "SELECT f.*, m.name as member_name, m.member_code, u.name as paid_by_name " +
                "FROM fines f " +
                "JOIN borrowings b ON f.borrowing_id = b.id " +
                "JOIN members m ON b.member_id = m.id " +
                "LEFT JOIN users u ON f.paid_by = u.id " +
                "WHERE f.borrowing_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, borrowingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFine(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Calculate fine for a borrowing (preview - doesn't save)
     */
    public Fine calculateFine(int borrowingId) {
        String sql = "SELECT b.due_date, SUM(bd.quantity) as total_books " +
                "FROM borrowings b " +
                "JOIN borrowing_details bd ON b.id = bd.borrowing_id " +
                "WHERE b.id = ? AND b.status = 'BORROWED' " +
                "GROUP BY b.id";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, borrowingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Date dueDate = rs.getDate("due_date");
                int totalBooks = rs.getInt("total_books");

                if (dueDate != null) {
                    LocalDate due = dueDate.toLocalDate();
                    LocalDate today = LocalDate.now();

                    if (today.isAfter(due)) {
                        int daysOverdue = (int) ChronoUnit.DAYS.between(due, today);
                        return new Fine(borrowingId, daysOverdue, totalBooks);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create fine record
     */
    public boolean create(Fine fine) {
        String sql = "INSERT INTO fines (borrowing_id, amount, days_overdue, total_books, fine_per_day, payment_status) "
                +
                "VALUES (?, ?, ?, ?, ?, 'UNPAID')";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, new BigDecimal(fine.getBorrowingId()));
            pstmt.setBigDecimal(2, fine.getAmount());
            pstmt.setInt(3, fine.getDaysOverdue());
            pstmt.setInt(4, fine.getTotalBooks());
            pstmt.setBigDecimal(5, fine.getFinePerDay());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mark fine as paid
     */
    public boolean markAsPaid(int id, String paymentMethod, int paidBy) {
        String sql = "UPDATE fines SET payment_status = 'PAID', payment_method = ?, " +
                "paid_at = CURRENT_TIMESTAMP, paid_by = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paymentMethod);
            pstmt.setInt(2, paidBy);
            pstmt.setInt(3, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update fine (admin only)
     */
    public boolean update(Fine fine) {
        String sql = "UPDATE fines SET amount = ?, days_overdue = ?, total_books = ?, fine_per_day = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, fine.getAmount());
            pstmt.setInt(2, fine.getDaysOverdue());
            pstmt.setInt(3, fine.getTotalBooks());
            pstmt.setBigDecimal(4, fine.getFinePerDay());
            pstmt.setInt(5, fine.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete fine (admin only)
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM fines WHERE id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if fine exists for borrowing
     */
    public boolean existsForBorrowing(int borrowingId) {
        String sql = "SELECT COUNT(*) FROM fines WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, borrowingId);
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
     * Auto-generate fines for all overdue borrowings
     */
    public int generateFinesForOverdue() {
        int count = 0;
        String sql = "SELECT b.id, b.due_date, SUM(bd.quantity) as total_books " +
                "FROM borrowings b " +
                "JOIN borrowing_details bd ON b.id = bd.borrowing_id " +
                "LEFT JOIN fines f ON b.id = f.borrowing_id " +
                "WHERE b.status = 'BORROWED' AND b.due_date < CURDATE() AND f.id IS NULL " +
                "GROUP BY b.id";

        // First collect all overdue borrowings data
        List<int[]> overdueData = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int borrowingId = rs.getInt("id");
                Date dueDate = rs.getDate("due_date");
                int totalBooks = rs.getInt("total_books");

                if (dueDate != null) {
                    LocalDate due = dueDate.toLocalDate();
                    LocalDate today = LocalDate.now();
                    int daysOverdue = (int) ChronoUnit.DAYS.between(due, today);

                    if (daysOverdue > 0) {
                        overdueData.add(new int[] { borrowingId, daysOverdue, totalBooks });
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Then create fines after ResultSet is closed
        for (int[] data : overdueData) {
            Fine fine = new Fine(data[0], data[1], data[2]);
            if (create(fine)) {
                count++;
            }
        }

        return count;
    }

    private Fine mapResultSetToFine(ResultSet rs) throws SQLException {
        Fine fine = new Fine();
        fine.setId(rs.getInt("id"));
        fine.setBorrowingId(rs.getInt("borrowing_id"));
        fine.setAmount(rs.getBigDecimal("amount"));
        fine.setDaysOverdue(rs.getInt("days_overdue"));
        fine.setTotalBooks(rs.getInt("total_books"));
        fine.setFinePerDay(rs.getBigDecimal("fine_per_day"));
        fine.setPaymentStatus(rs.getString("payment_status"));
        fine.setPaymentMethod(rs.getString("payment_method"));

        Timestamp paidAt = rs.getTimestamp("paid_at");
        if (paidAt != null) {
            fine.setPaidAt(paidAt.toLocalDateTime());
        }

        int paidBy = rs.getInt("paid_by");
        if (!rs.wasNull()) {
            fine.setPaidBy(paidBy);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            fine.setCreatedAt(createdAt.toLocalDateTime());
        }

        // Joined fields
        fine.setMemberName(rs.getString("member_name"));
        fine.setMemberCode(rs.getString("member_code"));
        fine.setPaidByName(rs.getString("paid_by_name"));

        return fine;
    }
}
