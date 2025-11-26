# E-Commerce Microservices - Complete API Documentation

## Overview
This document provides comprehensive documentation for all 59+ REST APIs across 5 microservices.

---

## User Service APIs (Port: 8081)

### User Management (9 APIs)

#### 1. Register User
- **Endpoint:** `POST /api/users/register`
- **Description:** Register a new user account
- **Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}
```

#### 2. User Login
- **Endpoint:** `POST /api/users/login`
- **Description:** Authenticate user and get JWT token
- **Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### 3. Get User by ID
- **Endpoint:** `GET /api/users/{id}`
- **Description:** Retrieve user details by ID

#### 4. Get User by Email
- **Endpoint:** `GET /api/users/email/{email}`
- **Description:** Retrieve user details by email

#### 5. Get All Users
- **Endpoint:** `GET /api/users`
- **Description:** Retrieve all users (admin only)

#### 6. Search Users
- **Endpoint:** `GET /api/users/search?query={searchTerm}`
- **Description:** Search users by name

#### 7. Update User
- **Endpoint:** `PUT /api/users/{id}`
- **Description:** Update user information

#### 8. Delete User
- **Endpoint:** `DELETE /api/users/{id}`
- **Description:** Delete user account

#### 9. Update User Status
- **Endpoint:** `PATCH /api/users/{id}/status?status={status}`
- **Description:** Update user account status

### Address Management (6 APIs)

#### 10. Create Address
- **Endpoint:** `POST /api/addresses`
- **Description:** Add new address for a user

#### 11. Get Address by ID
- **Endpoint:** `GET /api/addresses/{id}`
- **Description:** Retrieve address details

#### 12. Get Addresses by User
- **Endpoint:** `GET /api/addresses/user/{userId}`
- **Description:** Get all addresses for a user

#### 13. Update Address
- **Endpoint:** `PUT /api/addresses/{id}`
- **Description:** Update address information

#### 14. Delete Address
- **Endpoint:** `DELETE /api/addresses/{id}`
- **Description:** Remove an address

#### 15. Set Default Address
- **Endpoint:** `PATCH /api/addresses/{addressId}/default?userId={userId}`
- **Description:** Set address as default

---

## Product Service APIs (Port: 8082)

### Product Management (11 APIs)

#### 16. Create Product
- **Endpoint:** `POST /api/products`
- **Description:** Add new product to catalog

#### 17. Get Product by ID
- **Endpoint:** `GET /api/products/{id}`
- **Description:** Retrieve product details

#### 18. Get Product by SKU
- **Endpoint:** `GET /api/products/sku/{sku}`
- **Description:** Retrieve product by SKU

#### 19. Get All Products
- **Endpoint:** `GET /api/products`
- **Description:** Retrieve all products

#### 20. Get Products by Category
- **Endpoint:** `GET /api/products/category/{categoryId}`
- **Description:** Get products in a category

#### 21. Search Products
- **Endpoint:** `GET /api/products/search?query={searchTerm}`
- **Description:** Search products by name

#### 22. Get Products by Brand
- **Endpoint:** `GET /api/products/brand/{brand}`
- **Description:** Get products by brand name

#### 23. Get Products by Price Range
- **Endpoint:** `GET /api/products/price-range?minPrice={min}&maxPrice={max}`
- **Description:** Filter products by price

#### 24. Update Product
- **Endpoint:** `PUT /api/products/{id}`
- **Description:** Update product information

#### 25. Delete Product
- **Endpoint:** `DELETE /api/products/{id}`
- **Description:** Remove product from catalog

#### 26. Update Product Status
- **Endpoint:** `PATCH /api/products/{id}/status?status={status}`
- **Description:** Update product availability status

### Category Management (6 APIs)

#### 27. Create Category
- **Endpoint:** `POST /api/categories`
- **Description:** Create new product category

#### 28. Get Category by ID
- **Endpoint:** `GET /api/categories/{id}`
- **Description:** Retrieve category details

#### 29. Get All Categories
- **Endpoint:** `GET /api/categories`
- **Description:** Retrieve all categories

#### 30. Get Subcategories
- **Endpoint:** `GET /api/categories/{parentId}/subcategories`
- **Description:** Get child categories

#### 31. Update Category
- **Endpoint:** `PUT /api/categories/{id}`
- **Description:** Update category information

#### 32. Delete Category
- **Endpoint:** `DELETE /api/categories/{id}`
- **Description:** Remove category

### Review Management (5 APIs)

#### 33. Create Review
- **Endpoint:** `POST /api/reviews`
- **Description:** Add product review

#### 34. Get Review by ID
- **Endpoint:** `GET /api/reviews/{id}`
- **Description:** Retrieve review details

#### 35. Get Reviews by Product
- **Endpoint:** `GET /api/reviews/product/{productId}`
- **Description:** Get all reviews for a product

#### 36. Get Reviews by User
- **Endpoint:** `GET /api/reviews/user/{userId}`
- **Description:** Get all reviews by a user

#### 37. Delete Review
- **Endpoint:** `DELETE /api/reviews/{id}`
- **Description:** Remove a review

---

## Order Service APIs (Port: 8083)

### Order Management (7 APIs)

#### 38. Create Order
- **Endpoint:** `POST /api/orders`
- **Description:** Create new order
- **Request Body:**
```json
{
  "userId": 1,
  "shippingAddress": "123 Main St, City, State 12345",
  "billingAddress": "123 Main St, City, State 12345",
  "items": [
    {
      "productId": 1,
      "productName": "Product Name",
      "productSku": "SKU123",
      "quantity": 2,
      "unitPrice": 29.99
    }
  ],
  "taxAmount": 5.00,
  "shippingAmount": 10.00
}
```

#### 39. Get Order by ID
- **Endpoint:** `GET /api/orders/{id}`
- **Description:** Retrieve order details

#### 40. Get Order by Number
- **Endpoint:** `GET /api/orders/number/{orderNumber}`
- **Description:** Retrieve order by order number

#### 41. Get Orders by User
- **Endpoint:** `GET /api/orders/user/{userId}`
- **Description:** Get all orders for a user

#### 42. Get Orders by Status
- **Endpoint:** `GET /api/orders/status/{status}`
- **Description:** Filter orders by status

#### 43. Update Order Status
- **Endpoint:** `PATCH /api/orders/{id}/status?status={status}`
- **Description:** Update order status

#### 44. Cancel Order
- **Endpoint:** `POST /api/orders/{id}/cancel`
- **Description:** Cancel an order

---

## Payment Service APIs (Port: 8084)

### Payment Management (6 APIs)

#### 45. Process Payment
- **Endpoint:** `POST /api/payments`
- **Description:** Process payment for order
- **Request Body:**
```json
{
  "orderId": 1,
  "userId": 1,
  "amount": 99.99,
  "paymentMethod": "CREDIT_CARD"
}
```

#### 46. Get Payment by ID
- **Endpoint:** `GET /api/payments/{id}`
- **Description:** Retrieve payment details

#### 47. Get Payment by Transaction ID
- **Endpoint:** `GET /api/payments/transaction/{transactionId}`
- **Description:** Retrieve payment by transaction ID

#### 48. Get Payments by Order
- **Endpoint:** `GET /api/payments/order/{orderId}`
- **Description:** Get all payments for an order

#### 49. Get Payments by User
- **Endpoint:** `GET /api/payments/user/{userId}`
- **Description:** Get all payments by a user

#### 50. Refund Payment
- **Endpoint:** `POST /api/payments/{id}/refund`
- **Description:** Process payment refund

---

## Inventory Service APIs (Port: 8085)

### Inventory Management (9 APIs)

#### 51. Create Inventory
- **Endpoint:** `POST /api/inventory`
- **Description:** Create inventory record for product

#### 52. Get Inventory by ID
- **Endpoint:** `GET /api/inventory/{id}`
- **Description:** Retrieve inventory details

#### 53. Get Inventory by Product
- **Endpoint:** `GET /api/inventory/product/{productId}`
- **Description:** Get inventory for specific product

#### 54. Get All Inventory
- **Endpoint:** `GET /api/inventory`
- **Description:** Retrieve all inventory records

#### 55. Get Low Stock Items
- **Endpoint:** `GET /api/inventory/low-stock`
- **Description:** Get products with low stock

#### 56. Update Stock
- **Endpoint:** `PATCH /api/inventory/product/{productId}/stock?quantity={quantity}`
- **Description:** Add or remove stock (positive/negative quantity)

#### 57. Reserve Stock
- **Endpoint:** `POST /api/inventory/product/{productId}/reserve?quantity={quantity}`
- **Description:** Reserve stock for order

#### 58. Release Stock
- **Endpoint:** `POST /api/inventory/product/{productId}/release?quantity={quantity}`
- **Description:** Release reserved stock

#### 59. Deduct Stock
- **Endpoint:** `POST /api/inventory/product/{productId}/deduct?quantity={quantity}`
- **Description:** Deduct stock after order completion

---

## API Gateway (Port: 8080)

All services are accessible through the API Gateway at port 8080. Simply prefix the service endpoints with `http://localhost:8080`.

Example:
- Direct: `http://localhost:8081/api/users/1`
- Via Gateway: `http://localhost:8080/api/users/1`

---

## Status Codes

- `200 OK` - Successful GET/PUT/PATCH request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Enumerations

### UserStatus
- ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION

### UserRole
- CUSTOMER, ADMIN, VENDOR, SUPPORT

### AddressType
- HOME, WORK, BILLING, SHIPPING

### ProductStatus
- ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED

### OrderStatus
- PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED

### PaymentStatus
- PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, CANCELLED

### PaymentMethod
- CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, CASH_ON_DELIVERY, WALLET
