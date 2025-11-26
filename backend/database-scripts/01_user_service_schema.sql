-- ========================================
-- E-Commerce User Service Database Schema
-- ========================================

-- Create Database
CREATE DATABASE ecommerce_user_db;
GO

USE ecommerce_user_db;
GO

-- Users Table
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    phone_number NVARCHAR(20) UNIQUE,
    status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    role NVARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    last_login_at DATETIME2,
    CONSTRAINT CK_User_Status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION')),
    CONSTRAINT CK_User_Role CHECK (role IN ('CUSTOMER', 'ADMIN', 'VENDOR', 'SUPPORT'))
);

-- Addresses Table
CREATE TABLE addresses (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    street NVARCHAR(255) NOT NULL,
    city NVARCHAR(100) NOT NULL,
    state NVARCHAR(100) NOT NULL,
    country NVARCHAR(100) NOT NULL,
    zip_code NVARCHAR(20) NOT NULL,
    address_type NVARCHAR(50) NOT NULL DEFAULT 'HOME',
    is_default BIT DEFAULT 0,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Address_User FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT CK_Address_Type CHECK (address_type IN ('HOME', 'WORK', 'BILLING', 'SHIPPING'))
);

-- Indexes
CREATE INDEX IDX_Users_Email ON users(email);
CREATE INDEX IDX_Users_Status ON users(status);
CREATE INDEX IDX_Addresses_UserId ON addresses(user_id);
CREATE INDEX IDX_Addresses_IsDefault ON addresses(is_default);

GO
