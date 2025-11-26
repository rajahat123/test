# Database Setup Guide

## Prerequisites
- Microsoft SQL Server 2019 or later
- SQL Server Management Studio (SSMS) or Azure Data Studio

## Setup Instructions

### 1. Create Databases
Run the SQL scripts in the following order:

```bash
# Navigate to database-scripts folder
cd backend/database-scripts

# Execute scripts in SQL Server Management Studio in this order:
1. 01_user_service_schema.sql
2. 02_product_service_schema.sql
3. 03_order_service_schema.sql
4. 04_payment_service_schema.sql
5. 05_inventory_service_schema.sql
```

### 2. Configure Connection Strings

Update the `application.properties` files in each service with your SQL Server credentials:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Database Information

#### User Service Database
- **Database Name:** `ecommerce_user_db`
- **Tables:** users, addresses
- **Port:** 1433 (default)

#### Product Service Database
- **Database Name:** `ecommerce_product_db`
- **Tables:** categories, products, reviews
- **Port:** 1433 (default)

#### Order Service Database
- **Database Name:** `ecommerce_order_db`
- **Tables:** orders, order_items
- **Port:** 1433 (default)

#### Payment Service Database
- **Database Name:** `ecommerce_payment_db`
- **Tables:** payments
- **Port:** 1433 (default)

#### Inventory Service Database
- **Database Name:** `ecommerce_inventory_db`
- **Tables:** inventory
- **Port:** 1433 (default)

## Verification

After running all scripts, verify the setup:

```sql
-- Check if all databases exist
SELECT name FROM sys.databases 
WHERE name LIKE 'ecommerce_%';

-- Should return 5 databases:
-- ecommerce_user_db
-- ecommerce_product_db
-- ecommerce_order_db
-- ecommerce_payment_db
-- ecommerce_inventory_db
```

## Schema Features

### Constraints
- Primary Keys on all tables
- Foreign Keys for referential integrity
- Check constraints for data validation
- Unique constraints where applicable

### Indexes
- Indexes on frequently queried columns
- Foreign key indexes for join performance
- Composite indexes where beneficial

### Data Types
- BIGINT for IDs
- DECIMAL(19,2) for monetary values
- NVARCHAR for text fields
- DATETIME2 for timestamps
- BIT for boolean values

## Sample Data (Optional)

You can create sample data insertion scripts for testing purposes. Here's an example:

```sql
USE ecommerce_user_db;

-- Insert sample user
INSERT INTO users (email, password, first_name, last_name, phone_number, status, role)
VALUES ('admin@example.com', '$2a$10$encoded_password', 'Admin', 'User', '+1234567890', 'ACTIVE', 'ADMIN');
```

## Troubleshooting

### Connection Issues
- Verify SQL Server is running
- Check firewall settings
- Ensure TCP/IP protocol is enabled
- Verify SQL Server authentication mode

### Permission Issues
- Ensure user has CREATE DATABASE permission
- Grant appropriate permissions to the service user
- Check SQL Server login credentials

## Backup and Maintenance

### Backup Strategy
```sql
-- Full backup example
BACKUP DATABASE ecommerce_user_db 
TO DISK = 'C:\Backup\ecommerce_user_db.bak'
WITH FORMAT, COMPRESSION;
```

### Regular Maintenance
- Schedule regular backups
- Monitor database size and performance
- Update statistics regularly
- Rebuild fragmented indexes
