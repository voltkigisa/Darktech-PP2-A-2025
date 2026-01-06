package com.library.pos.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Borrowing - Entity model for book borrowings (peminjaman)
 */
public class Borrowing {
    private int id;
    private int memberId;
    private int userId; // petugas yang memproses
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private String status; // BORROWED, RETURNED
    private LocalDateTime createdAt;

    // Joined fields for display purposes
    private String memberName;
    private String memberCode;
    private String userName;

    // Constructors
    public Borrowing() {
    }

    public Borrowing(int id, int memberId, int userId, LocalDate borrowDate, LocalDate dueDate, String status) {
        this.id = id;
        this.memberId = memberId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Check if borrowing is overdue
    public boolean isOverdue() {
        return status != null && status.equals("BORROWED")
                && dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", userId=" + userId +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}
