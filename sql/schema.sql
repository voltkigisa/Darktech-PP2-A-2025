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

CREATE TABLE IF NOT EXISTS members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kode_anggota VARCHAR(20) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    alamat TEXT,
    telepon VARCHAR(15),
    status ENUM('AKTIF', 'NONAKTIF') DEFAULT 'AKTIF'
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