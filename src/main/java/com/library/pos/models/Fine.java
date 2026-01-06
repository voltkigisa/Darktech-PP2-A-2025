package com.library.pos.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fine - Entity model for overdue fines (denda)
 */
public class Fine {
    private int id;
    private int borrowingId;
    private BigDecimal amount;
    private int daysOverdue;
    private int totalBooks;
    private BigDecimal finePerDay;
    private String paymentStatus; // UNPAID, PAID
    private String paymentMethod; // CASH, DIGITAL
    private LocalDateTime paidAt;
    private Integer paidBy;
    private LocalDateTime createdAt;

    // Joined fields for display
    private String memberName;
    private String memberCode;
    private String paidByName;

    // Default fine per day (Rp 1.000)
    public static final BigDecimal DEFAULT_FINE_PER_DAY = new BigDecimal("1000");

    // Constructors
    public Fine() {
        this.finePerDay = DEFAULT_FINE_PER_DAY;
        this.paymentStatus = "UNPAID";
    }

    public Fine(int borrowingId, int daysOverdue, int totalBooks) {
        this.borrowingId = borrowingId;
        this.daysOverdue = daysOverdue;
        this.totalBooks = totalBooks;
        this.finePerDay = DEFAULT_FINE_PER_DAY;
        this.paymentStatus = "UNPAID";
        calculateAmount();
    }

    /**
     * Calculate fine amount: totalBooks × daysOverdue × finePerDay
     */
    public void calculateAmount() {
        this.amount = finePerDay
                .multiply(new BigDecimal(daysOverdue))
                .multiply(new BigDecimal(totalBooks));
    }

    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(int daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public BigDecimal getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(BigDecimal finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public Integer getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Integer paidBy) {
        this.paidBy = paidBy;
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

    public String getPaidByName() {
        return paidByName;
    }

    public void setPaidByName(String paidByName) {
        this.paidByName = paidByName;
    }

    /**
     * Get formatted amount for display (e.g., "Rp 5.000")
     */
    public String getFormattedAmount() {
        if (amount == null)
            return "Rp 0";
        return "Rp " + String.format("%,.0f", amount.doubleValue());
    }

    @Override
    public String toString() {
        return "Fine{" +
                "id=" + id +
                ", borrowingId=" + borrowingId +
                ", amount=" + amount +
                ", daysOverdue=" + daysOverdue +
                ", totalBooks=" + totalBooks +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
