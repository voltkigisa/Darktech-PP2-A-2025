package com.library.pos.controllers;

import com.library.pos.dao.FineDAO;
import com.library.pos.models.Fine;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * FineController - Controller for Fine operations with role-based access
 */
public class FineController {
    private FineDAO fineDAO;

    public FineController() {
        this.fineDAO = new FineDAO();
    }

    /**
     * Get all fines
     */
    public List<Fine> getAllFines() {
        // Auto-generate fines for overdue borrowings first
        fineDAO.generateFinesForOverdue();
        return fineDAO.getAll();
    }

    /**
     * Get unpaid fines
     */
    public List<Fine> getUnpaidFines() {
        fineDAO.generateFinesForOverdue();
        return fineDAO.getUnpaidFines();
    }

    /**
     * Get fine by ID
     */
    public Fine getFineById(int id) {
        return fineDAO.getById(id);
    }

    /**
     * Calculate fine preview for a borrowing
     */
    public Fine calculateFine(int borrowingId) {
        return fineDAO.calculateFine(borrowingId);
    }

    /**
     * Pay a fine with validation
     */
    public boolean payFine(int fineId, String paymentMethod, int paidBy) {
        // Validate payment method
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Silakan pilih metode pembayaran!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!paymentMethod.equals("CASH") && !paymentMethod.equals("DIGITAL")) {
            JOptionPane.showMessageDialog(null, "Metode pembayaran tidak valid!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if fine exists
        Fine fine = fineDAO.getById(fineId);
        if (fine == null) {
            JOptionPane.showMessageDialog(null, "Denda tidak ditemukan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if already paid
        if (fine.isPaid()) {
            JOptionPane.showMessageDialog(null, "Denda sudah dibayar sebelumnya!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Process payment
        if (fineDAO.markAsPaid(fineId, paymentMethod, paidBy)) {
            String methodDisplay = paymentMethod.equals("CASH") ? "Tunai" : "Digital";
            JOptionPane.showMessageDialog(null,
                    "Pembayaran denda berhasil!\n\nMetode: " + methodDisplay +
                            "\nJumlah: " + fine.getFormattedAmount(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Gagal memproses pembayaran!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Update fine (admin only)
     */
    public boolean updateFine(Fine fine, String userRole) {
        // Check role
        if (!"ADMIN".equals(userRole)) {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki akses untuk mengubah denda!",
                    "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate
        if (fine.getAmount() == null || fine.getAmount().doubleValue() < 0) {
            JOptionPane.showMessageDialog(null, "Jumlah denda tidak valid!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if fine exists
        Fine existingFine = fineDAO.getById(fine.getId());
        if (existingFine == null) {
            JOptionPane.showMessageDialog(null, "Denda tidak ditemukan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Cannot update paid fine
        if (existingFine.isPaid()) {
            JOptionPane.showMessageDialog(null, "Denda yang sudah dibayar tidak dapat diubah!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (fineDAO.update(fine)) {
            JOptionPane.showMessageDialog(null, "Denda berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui denda!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Delete fine (admin only)
     */
    public boolean deleteFine(int id, String userRole) {
        // Check role
        if (!"ADMIN".equals(userRole)) {
            JOptionPane.showMessageDialog(null, "Anda tidak memiliki akses untuk menghapus denda!",
                    "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Confirm delete
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus denda ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (fineDAO.delete(id)) {
                JOptionPane.showMessageDialog(null, "Denda berhasil dihapus!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus denda!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    /**
     * Generate fines for all overdue borrowings
     */
    public int generateOverdueFines() {
        int count = fineDAO.generateFinesForOverdue();
        if (count > 0) {
            JOptionPane.showMessageDialog(null,
                    count + " denda baru berhasil dibuat untuk peminjaman terlambat!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        return count;
    }
}
