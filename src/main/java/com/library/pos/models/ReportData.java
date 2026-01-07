package com.library.pos.models;

import java.util.List;

/**
 * Model untuk menyimpan data laporan
 */
public class ReportData {
    // Statistik
    private int totalBooks;
    private int totalMembers;
    private int activeLoans;
    private int totalUsers;

    // Data Transaksi
    private List<Borrowing> recentTransactions;

    // Data Denda
    private List<Fine> recentFines;

    // Metadata
    private String generatedDate;
    private String generatedBy;

    public ReportData() {
    }

    // Getters and Setters
    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(int activeLoans) {
        this.activeLoans = activeLoans;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public List<Borrowing> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<Borrowing> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public List<Fine> getRecentFines() {
        return recentFines;
    }

    public void setRecentFines(List<Fine> recentFines) {
        this.recentFines = recentFines;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
}

