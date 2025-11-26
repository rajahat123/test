-- ========================================
-- E-Commerce Payment Service Database Schema
-- ========================================

-- Create Database
CREATE DATABASE ecommerce_payment_db;
GO

USE ecommerce_payment_db;
GO

-- Payments Table
CREATE TABLE payments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    transaction_id NVARCHAR(100) NOT NULL UNIQUE,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    payment_method NVARCHAR(50) NOT NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'PENDING',
    payment_gateway_response NVARCHAR(MAX),
    payment_date DATETIME2,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CK_Payment_Status CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED', 'CANCELLED')),
    CONSTRAINT CK_Payment_Method CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'BANK_TRANSFER', 'CASH_ON_DELIVERY', 'WALLET')),
    CONSTRAINT CK_Payment_Amount CHECK (amount >= 0)
);

-- Indexes
CREATE INDEX IDX_Payments_TransactionId ON payments(transaction_id);
CREATE INDEX IDX_Payments_OrderId ON payments(order_id);
CREATE INDEX IDX_Payments_UserId ON payments(user_id);
CREATE INDEX IDX_Payments_Status ON payments(status);
CREATE INDEX IDX_Payments_PaymentDate ON payments(payment_date);

GO
