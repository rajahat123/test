-- ========================================
-- E-Commerce Product Service Database Schema
-- ========================================

-- Create Database
CREATE DATABASE ecommerce_product_db;
GO

USE ecommerce_product_db;
GO

-- Categories Table
CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL UNIQUE,
    description NVARCHAR(MAX),
    parent_id BIGINT,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Category_Parent FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Products Table
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(19, 2) NOT NULL,
    discount_price DECIMAL(19, 2),
    sku NVARCHAR(100) NOT NULL UNIQUE,
    category_id BIGINT,
    brand NVARCHAR(255),
    status NVARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    image_url NVARCHAR(500),
    average_rating FLOAT DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Product_Category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT CK_Product_Status CHECK (status IN ('ACTIVE', 'INACTIVE', 'OUT_OF_STOCK', 'DISCONTINUED')),
    CONSTRAINT CK_Product_Price CHECK (price >= 0),
    CONSTRAINT CK_Product_Rating CHECK (average_rating >= 0 AND average_rating <= 5)
);

-- Reviews Table
CREATE TABLE reviews (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment NVARCHAR(MAX),
    user_name NVARCHAR(255),
    is_verified_purchase BIT DEFAULT 0,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Review_Product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT CK_Review_Rating CHECK (rating >= 1 AND rating <= 5)
);

-- Indexes
CREATE INDEX IDX_Categories_ParentId ON categories(parent_id);
CREATE INDEX IDX_Categories_IsActive ON categories(is_active);
CREATE INDEX IDX_Products_SKU ON products(sku);
CREATE INDEX IDX_Products_CategoryId ON products(category_id);
CREATE INDEX IDX_Products_Status ON products(status);
CREATE INDEX IDX_Products_Brand ON products(brand);
CREATE INDEX IDX_Products_Name ON products(name);
CREATE INDEX IDX_Reviews_ProductId ON reviews(product_id);
CREATE INDEX IDX_Reviews_UserId ON reviews(user_id);

GO
