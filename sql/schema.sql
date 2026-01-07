-- Library POS System Database Schema
-- Run this script to create the database and tables

CREATE DATABASE IF NOT EXISTS library_pos;

USE library_pos;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'MANAGER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    issn VARCHAR(20) UNIQUE,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100),
    publisher VARCHAR(100),
    number_of_pages INT,
    genre VARCHAR(50),
    language VARCHAR(50),
    publication_place VARCHAR(100),
    year YEAR,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Book categories
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Pivot table book-category
CREATE TABLE IF NOT EXISTS book_categories (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

-- Members table
CREATE TABLE IF NOT EXISTS members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    age CHAR(3),
    phone VARCHAR(20),
    address TEXT,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Borrow transactions
CREATE TABLE IF NOT EXISTS borrowings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    user_id INT NOT NULL, -- petugas
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status ENUM('BORROWED', 'RETURNED') DEFAULT 'BORROWED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Borrowing details
CREATE TABLE IF NOT EXISTS borrowing_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrowing_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT DEFAULT 1,
    returned_quantity INT DEFAULT 0,
    FOREIGN KEY (borrowing_id) REFERENCES borrowings (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id)
);

-- Returns table
CREATE TABLE IF NOT EXISTS returns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrowing_id INT NOT NULL,
    return_date DATE NOT NULL,
    fine DECIMAL(10, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (borrowing_id) REFERENCES borrowings (id)
);

-- Fines table for detailed fine management
CREATE TABLE IF NOT EXISTS fines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    borrowing_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    days_overdue INT NOT NULL,
    total_books INT NOT NULL,
    fine_per_day DECIMAL(10, 2) DEFAULT 1000,
    payment_status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
    payment_method ENUM('CASH', 'DIGITAL') NULL,
    paid_at TIMESTAMP NULL,
    paid_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (borrowing_id) REFERENCES borrowings (id),
    FOREIGN KEY (paid_by) REFERENCES users (id)
);

-- Insert sample users
INSERT INTO
    users (
        username,
        password,
        name,
        role
    )
VALUES (
        'admin',
        'admin123',
        'Administrator',
        'ADMIN'
    ),
    (
        'manager',
        'manager123',
        'Library Manager',
        'MANAGER'
    )
ON DUPLICATE KEY UPDATE
    username = username;