# E-Commerce Microservices Backend

A comprehensive, production-ready e-commerce backend built with Java 11 Spring Boot microservices architecture, featuring **59+ REST APIs** across 5 microservices.

## 🚀 Quick Start

```bash
# 1. Clone the repository
git clone <repository-url>
cd backend

# 2. Setup databases (see DATABASE_SETUP.md)
# Execute SQL scripts in database-scripts/ folder

# 3. Build all services
mvn clean install

# 4. Start all services (requires 6 terminals)
# Terminal 1: cd api-gateway && mvn spring-boot:run
# Terminal 2: cd user-service && mvn spring-boot:run
# Terminal 3: cd product-service && mvn spring-boot:run
# Terminal 4: cd order-service && mvn spring-boot:run
# Terminal 5: cd payment-service && mvn spring-boot:run
# Terminal 6: cd inventory-service && mvn spring-boot:run

# 5. Access via API Gateway
curl http://localhost:8080/api/users
```

## 📋 Project Overview

### Microservices Architecture (5 Services + Gateway)

| Service | Port | APIs | Description |
|---------|------|------|-------------|
| **API Gateway** | 8080 | - | Routes requests to all services |
| **User Service** | 8081 | 15 | User & address management, authentication |
| **Product Service** | 8082 | 22 | Products, categories, reviews |
| **Order Service** | 8083 | 7 | Order processing & tracking |
| **Payment Service** | 8084 | 6 | Payment processing & refunds |
| **Inventory Service** | 8085 | 9 | Stock management & tracking |

**Total: 59 REST APIs** across all services

## 🏗️ Technology Stack

- **Language:** Java 11
- **Framework:** Spring Boot 2.7.18
- **Cloud:** Spring Cloud 2021.0.8
- **Database:** Microsoft SQL Server
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security + JWT
- **Gateway:** Spring Cloud Gateway
- **Build:** Maven
- **Utilities:** Lombok, Validation API

## 📁 Project Structure

```
backend/
├── api-gateway/           # API Gateway (Port 8080)
├── user-service/          # User Service (Port 8081)
├── product-service/       # Product Service (Port 8082)
├── order-service/         # Order Service (Port 8083)
├── payment-service/       # Payment Service (Port 8084)
├── inventory-service/     # Inventory Service (Port 8085)
├── database-scripts/      # MSSQL database schemas
├── pom.xml               # Parent Maven configuration
└── *.md                  # Documentation files
```

## 🗄️ Database Architecture

### 5 Databases, 11 Tables

1. **ecommerce_user_db** - users, addresses (2 tables)
2. **ecommerce_product_db** - products, categories, reviews (3 tables)
3. **ecommerce_order_db** - orders, order_items (2 tables)
4. **ecommerce_payment_db** - payments (1 table)
5. **ecommerce_inventory_db** - inventory (1 table)

## 🎯 Key Features

### ✅ User Management
- User registration & authentication (JWT)
- Profile management
- Multiple address support
- Role-based access control (CUSTOMER, ADMIN, VENDOR, SUPPORT)

### ✅ Product Catalog
- Full CRUD operations
- Hierarchical categories
- Product reviews & ratings
- Advanced search & filtering (name, brand, price, category)

### ✅ Order Processing
- Shopping cart to order conversion
- Multi-item orders
- Order status tracking (7 statuses)
- Order history & cancellation

### ✅ Payment System
- Multiple payment methods (6 types)
- Secure payment processing
- Transaction tracking
- Refund management

### ✅ Inventory Management
- Real-time stock tracking
- Stock reservation system
- Low stock alerts
- Warehouse location management

## 📚 Documentation

- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Complete API reference for all 59 APIs
- **[DATABASE_SETUP.md](DATABASE_SETUP.md)** - Database installation and configuration
- **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)** - Local, Docker, and cloud deployment
- **[TESTING_GUIDE.md](TESTING_GUIDE.md)** - API testing with cURL and Postman
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Comprehensive project overview

## 🔧 Prerequisites

- **JDK 11** or higher
- **Maven 3.6+**
- **Microsoft SQL Server 2019+**
- **Git** (for version control)

## 🚀 Installation & Setup

### 1. Database Setup
```bash
# Start SQL Server (or use Docker)
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourPassword123" \
  -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest

# Execute database scripts
cd database-scripts
# Run each .sql file (01 through 05) in SQL Server Management Studio
```

### 2. Configure Services
Update `application.properties` in each service:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=DB_NAME
spring.datasource.username=sa
spring.datasource.password=YourPassword123
```

### 3. Build Project
```bash
cd backend
mvn clean install
```

### 4. Run Services
**Option A: Development Mode**
```bash
# Start each service in separate terminal
cd user-service && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd payment-service && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

**Option B: Production Mode**
```bash
# Build JARs
mvn clean package -DskipTests

# Run as JARs
java -jar user-service/target/user-service-1.0.0.jar &
java -jar product-service/target/product-service-1.0.0.jar &
java -jar order-service/target/order-service-1.0.0.jar &
java -jar payment-service/target/payment-service-1.0.0.jar &
java -jar inventory-service/target/inventory-service-1.0.0.jar &
java -jar api-gateway/target/api-gateway-1.0.0.jar &
```

## 🧪 Quick API Test

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"John","lastName":"Doe"}'

# 2. Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","price":999.99,"sku":"LAP001","status":"ACTIVE"}'

# 3. Get all products
curl http://localhost:8080/api/products
```

See **[TESTING_GUIDE.md](TESTING_GUIDE.md)** for complete testing workflows.

## 🌐 API Endpoints Summary

### Via API Gateway (Port 8080)
- `/api/users/**` → User Service
- `/api/addresses/**` → User Service
- `/api/products/**` → Product Service
- `/api/categories/**` → Product Service
- `/api/reviews/**` → Product Service
- `/api/orders/**` → Order Service
- `/api/payments/**` → Payment Service
- `/api/inventory/**` → Inventory Service

### Direct Service Access
- User Service: `http://localhost:8081`
- Product Service: `http://localhost:8082`
- Order Service: `http://localhost:8083`
- Payment Service: `http://localhost:8084`
- Inventory Service: `http://localhost:8085`

## 🔐 Security

- **JWT Authentication** for user sessions
- **BCrypt** password encryption
- **Role-based access control** (RBAC)
- **CORS** configuration for web clients
- **Input validation** on all endpoints

## 📊 Project Statistics

- **Total Classes:** 80+
- **Total APIs:** 59
- **Entity Classes:** 11
- **DTO Classes:** 12
- **Service Classes:** 8
- **Controller Classes:** 8
- **Repository Interfaces:** 8
- **Database Tables:** 11
- **Lines of Code:** ~5000+

## 🐳 Docker Deployment

```bash
# Build images
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 🎓 Design Patterns

- **Microservices Architecture** - Independent, scalable services
- **API Gateway Pattern** - Single entry point for clients
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - Data transfer objects
- **Layered Architecture** - Controller → Service → Repository
- **Dependency Injection** - Constructor-based injection

## 🔄 Complete E-Commerce Flow

1. **User Registration** → User Service
2. **Product Creation** → Product Service
3. **Inventory Setup** → Inventory Service
4. **Browse Products** → Product Service (search, filter, reviews)
5. **Create Order** → Order Service
6. **Reserve Stock** → Inventory Service
7. **Process Payment** → Payment Service
8. **Update Order Status** → Order Service
9. **Deduct Stock** → Inventory Service
10. **Order Tracking** → Order Service

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For issues and questions:
- Create an issue on GitHub
- Check documentation files
- Review API documentation

## 🎯 Future Enhancements

- [ ] Service Discovery (Eureka)
- [ ] Distributed Tracing (Zipkin)
- [ ] Circuit Breaker (Resilience4j)
- [ ] Message Queue (Kafka/RabbitMQ)
- [ ] Redis Caching
- [ ] Swagger/OpenAPI Documentation
- [ ] Docker & Kubernetes Support
- [ ] CI/CD Pipeline
- [ ] Monitoring Dashboard

---

**Built with ❤️ using Java 11 & Spring Boot**
