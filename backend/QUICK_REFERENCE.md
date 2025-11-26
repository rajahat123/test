# E-Commerce Backend - Quick Reference Card

## 🚀 Quick Commands

### Build & Run
```bash
mvn clean install              # Build all services
mvn spring-boot:run            # Run service
mvn clean package              # Create JARs
java -jar target/*.jar         # Run JAR
```

### Database
```bash
# Start SQL Server (Docker)
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourPassword123" \
  -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest
```

## 📡 Service Ports

| Service | Port | Path |
|---------|------|------|
| API Gateway | 8080 | / |
| User Service | 8081 | /api/users, /api/addresses |
| Product Service | 8082 | /api/products, /api/categories, /api/reviews |
| Order Service | 8083 | /api/orders |
| Payment Service | 8084 | /api/payments |
| Inventory Service | 8085 | /api/inventory |

## 🎯 Essential API Calls

### User Service
```bash
# Register
POST /api/users/register
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}

# Login
POST /api/users/login
{
  "email": "user@example.com",
  "password": "password123"
}

# Get All Users
GET /api/users
```

### Product Service
```bash
# Create Product
POST /api/products
{
  "name": "Product Name",
  "price": 99.99,
  "sku": "SKU001",
  "status": "ACTIVE"
}

# Search Products
GET /api/products/search?query=laptop

# Get by Price Range
GET /api/products/price-range?minPrice=100&maxPrice=1000
```

### Order Service
```bash
# Create Order
POST /api/orders
{
  "userId": 1,
  "items": [{
    "productId": 1,
    "quantity": 2,
    "unitPrice": 99.99
  }]
}

# Get User Orders
GET /api/orders/user/1

# Update Status
PATCH /api/orders/1/status?status=SHIPPED
```

### Payment Service
```bash
# Process Payment
POST /api/payments
{
  "orderId": 1,
  "userId": 1,
  "amount": 199.98,
  "paymentMethod": "CREDIT_CARD"
}

# Refund
POST /api/payments/1/refund
```

### Inventory Service
```bash
# Create Inventory
POST /api/inventory
{
  "productId": 1,
  "quantity": 100,
  "reorderLevel": 20
}

# Reserve Stock
POST /api/inventory/product/1/reserve?quantity=2

# Get Low Stock
GET /api/inventory/low-stock
```

## 🔐 Authentication

### JWT Token Usage
```bash
# 1. Login to get token
TOKEN=$(curl -s -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}' \
  | jq -r '.token')

# 2. Use token in requests
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/users/1
```

## 📊 Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Success |
| 201 | Created - Resource created |
| 204 | No Content - Deleted successfully |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Auth required |
| 404 | Not Found - Resource not found |
| 500 | Server Error |

## 🗄️ Database Connections

```properties
# User Service
jdbc:sqlserver://localhost:1433;databaseName=ecommerce_user_db

# Product Service
jdbc:sqlserver://localhost:1433;databaseName=ecommerce_product_db

# Order Service
jdbc:sqlserver://localhost:1433;databaseName=ecommerce_order_db

# Payment Service
jdbc:sqlserver://localhost:1433;databaseName=ecommerce_payment_db

# Inventory Service
jdbc:sqlserver://localhost:1433;databaseName=ecommerce_inventory_db
```

## 📝 Configuration

### application.properties Template
```properties
server.port=808X
spring.application.name=service-name

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=DB_NAME
spring.datasource.username=sa
spring.datasource.password=YourPassword123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect
```

## 🔄 Complete Test Flow

```bash
# 1. Register user
curl -X POST http://localhost:8080/api/users/register -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"pass123","firstName":"Test","lastName":"User"}'

# 2. Create category
curl -X POST http://localhost:8080/api/categories -H "Content-Type: application/json" \
  -d '{"name":"Electronics","isActive":true}'

# 3. Create product
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" \
  -d '{"name":"Laptop","price":999.99,"sku":"LAP001","categoryId":1,"status":"ACTIVE"}'

# 4. Create inventory
curl -X POST http://localhost:8080/api/inventory -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":50,"reorderLevel":10}'

# 5. Create order
curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" \
  -d '{"userId":1,"items":[{"productId":1,"quantity":1,"unitPrice":999.99}]}'

# 6. Process payment
curl -X POST http://localhost:8080/api/payments -H "Content-Type: application/json" \
  -d '{"orderId":1,"userId":1,"amount":999.99,"paymentMethod":"CREDIT_CARD"}'
```

## 🐛 Debugging

### Check Service Status
```bash
curl http://localhost:8081  # User Service
curl http://localhost:8082  # Product Service
curl http://localhost:8083  # Order Service
curl http://localhost:8084  # Payment Service
curl http://localhost:8085  # Inventory Service
curl http://localhost:8080  # API Gateway
```

### View Logs
```bash
tail -f logs/application.log
```

### Database Query
```sql
-- Check table records
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM products;
SELECT COUNT(*) FROM orders;
```

## 🎯 Enumerations

### UserStatus
`ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`

### UserRole
`CUSTOMER`, `ADMIN`, `VENDOR`, `SUPPORT`

### AddressType
`HOME`, `WORK`, `BILLING`, `SHIPPING`

### ProductStatus
`ACTIVE`, `INACTIVE`, `OUT_OF_STOCK`, `DISCONTINUED`

### OrderStatus
`PENDING`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `REFUNDED`

### PaymentStatus
`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `REFUNDED`, `CANCELLED`

### PaymentMethod
`CREDIT_CARD`, `DEBIT_CARD`, `PAYPAL`, `BANK_TRANSFER`, `CASH_ON_DELIVERY`, `WALLET`

## 📚 Documentation Files

- **README.md** - Main documentation
- **API_DOCUMENTATION.md** - All 59 APIs
- **DATABASE_SETUP.md** - DB setup guide
- **DEPLOYMENT_GUIDE.md** - Deployment instructions
- **TESTING_GUIDE.md** - Testing procedures
- **PROJECT_SUMMARY.md** - Project overview
- **QUICK_REFERENCE.md** - This file

## 💡 Useful Maven Commands

```bash
mvn clean                    # Clean build artifacts
mvn compile                  # Compile source code
mvn test                     # Run tests
mvn package                  # Create JAR/WAR
mvn install                  # Install to local repo
mvn spring-boot:run          # Run Spring Boot app
mvn dependency:tree          # View dependencies
mvn clean install -DskipTests  # Build without tests
```

## 🐳 Docker Commands

```bash
docker-compose up -d         # Start all services
docker-compose down          # Stop all services
docker-compose logs -f       # View logs
docker-compose ps            # List services
docker-compose restart       # Restart services
```

---

**Quick Access:** Save this file for instant reference during development!
