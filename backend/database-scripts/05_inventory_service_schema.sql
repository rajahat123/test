-- ========================================
-- E-Commerce Inventory Service Database Schema
-- ========================================

-- Create Database
CREATE DATABASE ecommerce_inventory_db;
GO

USE ecommerce_inventory_db;
GO

-- Inventory Table
CREATE TABLE inventory (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    product_sku NVARCHAR(100),
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT DEFAULT 0,
    reorder_level INT DEFAULT 10,
    reorder_quantity INT DEFAULT 50,
    warehouse_location NVARCHAR(255),
    last_restocked_at DATETIME2,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CK_Inventory_Quantity CHECK (quantity >= 0),
    CONSTRAINT CK_Inventory_ReservedQuantity CHECK (reserved_quantity >= 0)
);

-- Indexes
CREATE INDEX IDX_Inventory_ProductId ON inventory(product_id);
CREATE INDEX IDX_Inventory_ProductSku ON inventory(product_sku);
CREATE INDEX IDX_Inventory_WarehouseLocation ON inventory(warehouse_location);
CREATE INDEX IDX_Inventory_Quantity ON inventory(quantity);

GO
