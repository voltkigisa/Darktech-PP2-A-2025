package com.library.pos.models;

import java.time.LocalDateTime;

/**
 * Book - Entity model for books
 */
public class Book {
    private int id;
    private String isbn;
    private String issn;
    private String title;
    private String author;
    private String publisher;
    private Integer numberOfPages;
    private String genre;
    private String language;
    private String publicationPlace;
    private Integer year;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Book() {
    }

    public Book(int id, String title, int stock) {
        this.id = id;
        this.title = title;
        this.stock = stock;
    }

    public Book(int id, String isbn, String issn, String title, String author, String publisher,
            Integer numberOfPages, String genre, String language, String publicationPlace,
            Integer year, int stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.isbn = isbn;
        this.issn = issn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.language = language;
        this.publicationPlace = publicationPlace;
        this.year = year;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(String publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
