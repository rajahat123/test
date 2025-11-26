-- ========================================
-- E-Commerce Order Service Database Schema
-- ========================================

-- Create Database
CREATE DATABASE ecommerce_order_db;
GO

USE ecommerce_order_db;
GO

-- Orders Table
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_number NVARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL,
    tax_amount DECIMAL(19, 2) DEFAULT 0,
    shipping_amount DECIMAL(19, 2) DEFAULT 0,
    discount_amount DECIMAL(19, 2) DEFAULT 0,
    status NVARCHAR(50) NOT NULL DEFAULT 'PENDING',
    shipping_address NVARCHAR(MAX),
    billing_address NVARCHAR(MAX),
    order_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    shipped_date DATETIME2,
    delivered_date DATETIME2,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CK_Order_Status CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')),
    CONSTRAINT CK_Order_TotalAmount CHECK (total_amount >= 0)
);

-- Order Items Table
CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name NVARCHAR(255),
    product_sku NVARCHAR(100),
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_OrderItem_Order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT CK_OrderItem_Quantity CHECK (quantity > 0),
    CONSTRAINT CK_OrderItem_Price CHECK (unit_price >= 0 AND total_price >= 0)
);

-- Indexes
CREATE INDEX IDX_Orders_OrderNumber ON orders(order_number);
CREATE INDEX IDX_Orders_UserId ON orders(user_id);
CREATE INDEX IDX_Orders_Status ON orders(status);
CREATE INDEX IDX_Orders_OrderDate ON orders(order_date);
CREATE INDEX IDX_OrderItems_OrderId ON order_items(order_id);
CREATE INDEX IDX_OrderItems_ProductId ON order_items(product_id);

GO
