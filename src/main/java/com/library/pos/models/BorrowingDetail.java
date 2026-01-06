package com.library.pos.models;

/**
 * BorrowingDetail - Entity model for borrowing details (book items)
 */
public class BorrowingDetail {
    private int id;
    private int borrowingId;
    private int bookId;
    private int quantity;
    private int returnedQuantity;

    // Joined fields for display purposes
    private String bookTitle;
    private String bookIsbn;

    // Constructors
    public BorrowingDetail() {
        this.quantity = 1;
        this.returnedQuantity = 0;
    }

    public BorrowingDetail(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.returnedQuantity = 0;
    }

    public BorrowingDetail(int id, int borrowingId, int bookId, int quantity, int returnedQuantity) {
        this.id = id;
        this.borrowingId = borrowingId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.returnedQuantity = returnedQuantity;
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

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    @Override
    public String toString() {
        return "BorrowingDetail{" +
                "id=" + id +
                ", borrowingId=" + borrowingId +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", returnedQuantity=" + returnedQuantity +
                ", bookTitle='" + bookTitle + '\'' +
                '}';
    }
}
