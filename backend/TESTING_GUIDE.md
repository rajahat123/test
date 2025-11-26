# API Testing Guide

## Testing Tools

### Recommended Tools
1. **Postman** - For API testing
2. **cURL** - Command-line testing
3. **Swagger UI** - Interactive API documentation (future enhancement)

## Quick Test Commands

### User Service Tests

#### 1. Register User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }'
```

#### 2. Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

#### 3. Get All Users
```bash
curl -X GET http://localhost:8080/api/users
```

### Product Service Tests

#### 4. Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and gadgets",
    "isActive": true
  }'
```

#### 5. Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone X",
    "description": "Latest smartphone with advanced features",
    "price": 699.99,
    "sku": "PHONE-X-001",
    "categoryId": 1,
    "brand": "TechBrand",
    "status": "ACTIVE"
  }'
```

#### 6. Search Products
```bash
curl -X GET "http://localhost:8080/api/products/search?query=smartphone"
```

#### 7. Get Products by Price Range
```bash
curl -X GET "http://localhost:8080/api/products/price-range?minPrice=100&maxPrice=1000"
```

#### 8. Create Review
```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "userId": 1,
    "rating": 5,
    "comment": "Excellent product!",
    "userName": "John Doe"
  }'
```

### Order Service Tests

#### 9. Create Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Main St, City, State 12345",
    "billingAddress": "123 Main St, City, State 12345",
    "items": [
      {
        "productId": 1,
        "productName": "Smartphone X",
        "productSku": "PHONE-X-001",
        "quantity": 1,
        "unitPrice": 699.99
      }
    ],
    "taxAmount": 56.00,
    "shippingAmount": 15.00
  }'
```

#### 10. Get Orders by User
```bash
curl -X GET http://localhost:8080/api/orders/user/1
```

#### 11. Update Order Status
```bash
curl -X PATCH "http://localhost:8080/api/orders/1/status?status=SHIPPED"
```

### Payment Service Tests

#### 12. Process Payment
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "userId": 1,
    "amount": 770.99,
    "paymentMethod": "CREDIT_CARD"
  }'
```

#### 13. Get Payments by Order
```bash
curl -X GET http://localhost:8080/api/payments/order/1
```

### Inventory Service Tests

#### 14. Create Inventory
```bash
curl -X POST http://localhost:8080/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "productSku": "PHONE-X-001",
    "quantity": 100,
    "reorderLevel": 20,
    "reorderQuantity": 50,
    "warehouseLocation": "Warehouse A - Aisle 5"
  }'
```

#### 15. Get Low Stock Items
```bash
curl -X GET http://localhost:8080/api/inventory/low-stock
```

#### 16. Reserve Stock
```bash
curl -X POST "http://localhost:8080/api/inventory/product/1/reserve?quantity=2"
```

#### 17. Update Stock
```bash
curl -X PATCH "http://localhost:8080/api/inventory/product/1/stock?quantity=50"
```

## Testing Workflow

### Complete E-Commerce Flow Test

```bash
# 1. Register a user
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "+1234567890"
  }')

# 2. Create a category
CATEGORY_RESPONSE=$(curl -s -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices",
    "isActive": true
  }')

# 3. Create a product
PRODUCT_RESPONSE=$(curl -s -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro",
    "description": "High-performance laptop",
    "price": 1299.99,
    "sku": "LAPTOP-PRO-001",
    "categoryId": 1,
    "brand": "TechBrand",
    "status": "ACTIVE"
  }')

# 4. Create inventory for product
curl -s -X POST http://localhost:8080/api/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "productSku": "LAPTOP-PRO-001",
    "quantity": 50,
    "reorderLevel": 10,
    "warehouseLocation": "Warehouse A"
  }'

# 5. Create an order
ORDER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Main St",
    "billingAddress": "123 Main St",
    "items": [{
      "productId": 1,
      "productName": "Laptop Pro",
      "productSku": "LAPTOP-PRO-001",
      "quantity": 1,
      "unitPrice": 1299.99
    }],
    "taxAmount": 104.00,
    "shippingAmount": 20.00
  }')

# 6. Process payment
curl -s -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "userId": 1,
    "amount": 1423.99,
    "paymentMethod": "CREDIT_CARD"
  }'

# 7. Reserve stock
curl -s -X POST "http://localhost:8080/api/inventory/product/1/reserve?quantity=1"

# 8. Update order status
curl -s -X PATCH "http://localhost:8080/api/orders/1/status?status=CONFIRMED"

echo "Complete e-commerce flow tested successfully!"
```

## Postman Collection Variables

Create these variables in Postman:

```
base_url = http://localhost:8080
user_service = http://localhost:8081
product_service = http://localhost:8082
order_service = http://localhost:8083
payment_service = http://localhost:8084
inventory_service = http://localhost:8085

# Test Data
test_user_email = test@example.com
test_user_password = password123
```

## Response Codes to Expect

- **200 OK** - Successful retrieval or update
- **201 Created** - Resource created successfully
- **204 No Content** - Successful deletion
- **400 Bad Request** - Invalid input
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

## Common Testing Scenarios

### 1. User Registration & Login Flow
1. Register user → 201 Created
2. Login → 200 OK (with JWT token)
3. Get user profile → 200 OK

### 2. Product Catalog Flow
1. Create category → 201 Created
2. Create product → 201 Created
3. Add inventory → 201 Created
4. Search products → 200 OK
5. Add review → 201 Created

### 3. Order Processing Flow
1. Create order → 201 Created
2. Process payment → 201 Created
3. Reserve inventory → 200 OK
4. Update order status → 200 OK
5. Track order → 200 OK

### 4. Error Testing
- Try duplicate email registration → 400 Bad Request
- Try invalid product ID → 404 Not Found
- Try negative quantity → 400 Bad Request
- Try insufficient stock → 400 Bad Request

## Health Check Endpoints

```bash
# Check if services are running
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Product Service
curl http://localhost:8083/actuator/health  # Order Service
curl http://localhost:8084/actuator/health  # Payment Service
curl http://localhost:8085/actuator/health  # Inventory Service
```

Note: Add Spring Boot Actuator dependency to enable health endpoints.

## Troubleshooting

### Service Not Responding
1. Check if service is running on correct port
2. Verify database connection
3. Check application logs
4. Ensure all dependencies are downloaded

### Database Connection Errors
1. Verify SQL Server is running
2. Check connection string in application.properties
3. Verify credentials
4. Ensure database exists

### API Returns 404
1. Verify service is running
2. Check API Gateway configuration
3. Verify endpoint path is correct

### Authentication Errors
1. Ensure JWT token is valid
2. Check token expiration
3. Verify user credentials
