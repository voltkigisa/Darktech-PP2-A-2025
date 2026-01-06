package com.library.pos.controllers;

import com.library.pos.dao.BorrowingDAO;
import com.library.pos.models.Borrowing;
import com.library.pos.models.BorrowingDetail;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;

/**
 * BorrowingController - Controller for Borrowing operations with validation and
 * popups
 */
public class BorrowingController {
    private BorrowingDAO borrowingDAO;

    public BorrowingController() {
        this.borrowingDAO = new BorrowingDAO();
    }

    /**
     * Get all borrowings
     */
    public List<Borrowing> getAllBorrowings() {
        return borrowingDAO.getAll();
    }

    /**
     * Get borrowing by ID
     */
    public Borrowing getBorrowingById(int id) {
        return borrowingDAO.getById(id);
    }

    /**
     * Get borrowing details (books)
     */
    public List<BorrowingDetail> getBorrowingDetails(int borrowingId) {
        return borrowingDAO.getDetailsByBorrowingId(borrowingId);
    }

    /**
     * Create new borrowing with validation and popup messages
     */
    public boolean createBorrowing(Borrowing borrowing, List<BorrowingDetail> details) {
        // Validate input
        String validationError = validateBorrowingInput(borrowing, details);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check stock availability
        for (BorrowingDetail detail : details) {
            if (!borrowingDAO.hasEnoughStock(detail.getBookId(), detail.getQuantity())) {
                JOptionPane.showMessageDialog(null,
                        "Stok buku '" + detail.getBookTitle() + "' tidak mencukupi!",
                        "Stok Tidak Cukup",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Create borrowing
        if (borrowingDAO.create(borrowing, details)) {
            JOptionPane.showMessageDialog(null, "Peminjaman berhasil dibuat!", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Gagal membuat peminjaman!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Delete borrowing with confirmation and popup messages
     */
    public boolean deleteBorrowing(int id) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus peminjaman ini?\nStok buku akan dikembalikan.",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (borrowingDAO.delete(id)) {
                JOptionPane.showMessageDialog(null, "Peminjaman berhasil dihapus!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus peminjaman!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Validate borrowing input
     */
    private String validateBorrowingInput(Borrowing borrowing, List<BorrowingDetail> details) {
        // Validate member
        if (borrowing.getMemberId() <= 0) {
            return "Silakan pilih anggota!";
        }

        if (!borrowingDAO.memberExists(borrowing.getMemberId())) {
            return "Anggota tidak ditemukan!";
        }

        // Validate borrow date
        if (borrowing.getBorrowDate() == null) {
            return "Tanggal peminjaman harus diisi!";
        }

        // Validate due date
        if (borrowing.getDueDate() == null) {
            return "Tanggal pengembalian harus diisi!";
        }

        // Due date must be after or equal to borrow date
        if (borrowing.getDueDate().isBefore(borrowing.getBorrowDate())) {
            return "Tanggal pengembalian harus setelah tanggal peminjaman!";
        }

        // Validate user (petugas)
        if (borrowing.getUserId() <= 0) {
            return "Data petugas tidak valid!";
        }

        // Validate details
        if (details == null || details.isEmpty()) {
            return "Minimal harus ada 1 buku yang dipinjam!";
        }

        for (BorrowingDetail detail : details) {
            if (detail.getBookId() <= 0) {
                return "Buku tidak valid!";
            }
            if (detail.getQuantity() <= 0) {
                return "Jumlah buku harus lebih dari 0!";
            }
        }

        return null;
    }
}
