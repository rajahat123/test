# Deployment Guide

## Prerequisites

### Required Software
- JDK 11
- Maven 3.6+
- Microsoft SQL Server 2019+
- Git (for version control)

### Recommended Tools
- IntelliJ IDEA or Eclipse
- SQL Server Management Studio (SSMS)
- Postman (for API testing)

## Local Development Setup

### 1. Clone Repository
```bash
git clone <repository-url>
cd backend
```

### 2. Configure Database

#### Install SQL Server
```bash
# For Windows: Download from Microsoft website
# For macOS (using Docker):
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=YourPassword123" \
  -p 1433:1433 --name mssql \
  -d mcr.microsoft.com/mssql/server:2019-latest

# For Linux:
sudo apt-get update
sudo apt-get install -y mssql-server
sudo /opt/mssql/bin/mssql-conf setup
```

#### Create Databases
```bash
# Connect to SQL Server and execute:
cd database-scripts
# Execute each .sql file in order (01 through 05)
```

### 3. Update Configuration Files

Update `application.properties` in each service:
```properties
spring.datasource.url=jdbc:sqlserver://YOUR_HOST:1433;databaseName=DB_NAME
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 4. Build All Services
```bash
# From backend directory
mvn clean install
```

### 5. Start Services

#### Option A: Manual Start (6 separate terminals)
```bash
# Terminal 1: API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 2: User Service
cd user-service
mvn spring-boot:run

# Terminal 3: Product Service
cd product-service
mvn spring-boot:run

# Terminal 4: Order Service
cd order-service
mvn spring-boot:run

# Terminal 5: Payment Service
cd payment-service
mvn spring-boot:run

# Terminal 6: Inventory Service
cd inventory-service
mvn spring-boot:run
```

#### Option B: Using Start Script
Create `start-all.sh`:
```bash
#!/bin/bash

echo "Starting all microservices..."

cd api-gateway
mvn spring-boot:run &
API_GATEWAY_PID=$!

cd ../user-service
mvn spring-boot:run &
USER_SERVICE_PID=$!

cd ../product-service
mvn spring-boot:run &
PRODUCT_SERVICE_PID=$!

cd ../order-service
mvn spring-boot:run &
ORDER_SERVICE_PID=$!

cd ../payment-service
mvn spring-boot:run &
PAYMENT_SERVICE_PID=$!

cd ../inventory-service
mvn spring-boot:run &
INVENTORY_SERVICE_PID=$!

echo "All services started!"
echo "API Gateway PID: $API_GATEWAY_PID"
echo "To stop all services, run: kill $API_GATEWAY_PID $USER_SERVICE_PID $PRODUCT_SERVICE_PID $ORDER_SERVICE_PID $PAYMENT_SERVICE_PID $INVENTORY_SERVICE_PID"

wait
```

Make it executable:
```bash
chmod +x start-all.sh
./start-all.sh
```

### 6. Verify Deployment
```bash
# Check if all services are running
curl http://localhost:8080  # API Gateway
curl http://localhost:8081  # User Service
curl http://localhost:8082  # Product Service
curl http://localhost:8083  # Order Service
curl http://localhost:8084  # Payment Service
curl http://localhost:8085  # Inventory Service
```

## Production Deployment

### 1. Create Executable JARs
```bash
cd backend
mvn clean package -DskipTests

# JARs will be created in each service's target directory:
# api-gateway/target/api-gateway-1.0.0.jar
# user-service/target/user-service-1.0.0.jar
# product-service/target/product-service-1.0.0.jar
# order-service/target/order-service-1.0.0.jar
# payment-service/target/payment-service-1.0.0.jar
# inventory-service/target/inventory-service-1.0.0.jar
```

### 2. Run as JAR Files
```bash
# Run each service as a JAR
java -jar api-gateway/target/api-gateway-1.0.0.jar &
java -jar user-service/target/user-service-1.0.0.jar &
java -jar product-service/target/product-service-1.0.0.jar &
java -jar order-service/target/order-service-1.0.0.jar &
java -jar payment-service/target/payment-service-1.0.0.jar &
java -jar inventory-service/target/inventory-service-1.0.0.jar &
```

### 3. Create Systemd Services (Linux)

Create service files for each microservice:

**Example: `/etc/systemd/system/user-service.service`**
```ini
[Unit]
Description=E-Commerce User Service
After=network.target

[Service]
Type=simple
User=ecommerce
WorkingDirectory=/opt/ecommerce
ExecStart=/usr/bin/java -jar /opt/ecommerce/user-service-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start services:
```bash
sudo systemctl daemon-reload
sudo systemctl enable user-service
sudo systemctl start user-service
sudo systemctl status user-service
```

Repeat for all services.

## Docker Deployment

### 1. Create Dockerfile for Each Service

**Example: `user-service/Dockerfile`**
```dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/user-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Create Docker Compose File

**`docker-compose.yml`**
```yaml
version: '3.8'

services:
  sqlserver:
    image: mcr.microsoft.com/mssql/server:2019-latest
    environment:
      - ACCEPT_EULA=Y
      - SA_PASSWORD=YourPassword123
    ports:
      - "1433:1433"
    volumes:
      - sqldata:/var/opt/mssql

  api-gateway:
    build: ./api-gateway
    ports:
      - "8080:8080"
    depends_on:
      - user-service
      - product-service
      - order-service
      - payment-service
      - inventory-service

  user-service:
    build: ./user-service
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce_user_db
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=YourPassword123
    depends_on:
      - sqlserver

  product-service:
    build: ./product-service
    ports:
      - "8082:8082"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce_product_db
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=YourPassword123
    depends_on:
      - sqlserver

  order-service:
    build: ./order-service
    ports:
      - "8083:8083"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce_order_db
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=YourPassword123
    depends_on:
      - sqlserver

  payment-service:
    build: ./payment-service
    ports:
      - "8084:8084"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce_payment_db
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=YourPassword123
    depends_on:
      - sqlserver

  inventory-service:
    build: ./inventory-service
    ports:
      - "8085:8085"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce_inventory_db
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=YourPassword123
    depends_on:
      - sqlserver

volumes:
  sqldata:
```

### 3. Build and Run with Docker
```bash
# Build all services
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## Cloud Deployment (AWS Example)

### 1. EC2 Deployment
```bash
# Create EC2 instances (t2.medium recommended)
# Install Java and dependencies
sudo yum update -y
sudo yum install java-11-openjdk -y

# Transfer JAR files
scp -i key.pem *.jar ec2-user@<instance-ip>:/home/ec2-user/

# Run services
java -jar user-service-1.0.0.jar
```

### 2. RDS for SQL Server
- Create RDS SQL Server instance
- Update connection strings in application.properties
- Migrate databases using SQL scripts

### 3. Elastic Beanstalk
```bash
# Install EB CLI
pip install awsebcli

# Initialize EB application
eb init -p java-11 ecommerce-backend

# Create environment
eb create ecommerce-env

# Deploy
eb deploy
```

### 4. ECS/EKS (Kubernetes)
Create Kubernetes deployment files:

**`user-service-deployment.yaml`**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: your-registry/user-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:sqlserver://db-host:1433;databaseName=ecommerce_user_db"
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
  ports:
  - port: 8081
    targetPort: 8081
  type: LoadBalancer
```

Deploy to Kubernetes:
```bash
kubectl apply -f user-service-deployment.yaml
kubectl apply -f product-service-deployment.yaml
kubectl apply -f order-service-deployment.yaml
kubectl apply -f payment-service-deployment.yaml
kubectl apply -f inventory-service-deployment.yaml
kubectl apply -f api-gateway-deployment.yaml
```

## Environment-Specific Configurations

### Development (application-dev.properties)
```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.root=DEBUG
```

### Staging (application-staging.properties)
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
logging.level.root=INFO
```

### Production (application-prod.properties)
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
server.error.include-message=never
server.error.include-stacktrace=never
```

Run with specific profile:
```bash
java -jar -Dspring.profiles.active=prod user-service-1.0.0.jar
```

## Monitoring and Logging

### Add Actuator for Health Checks
Add to each service's `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Configure Logging
Add to `application.properties`:
```properties
logging.file.name=logs/application.log
logging.level.com.ecommerce=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## Performance Tuning

### JVM Options
```bash
java -Xms512m -Xmx2048m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar application.jar
```

### Database Connection Pool
Add to `application.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

## Security Checklist

- [ ] Change default passwords
- [ ] Enable HTTPS/TLS
- [ ] Configure firewall rules
- [ ] Set up rate limiting
- [ ] Enable CORS properly
- [ ] Use environment variables for secrets
- [ ] Regular security updates
- [ ] Enable SQL Server encryption
- [ ] Implement API authentication
- [ ] Set up logging and monitoring

## Backup Strategy

### Database Backup
```sql
-- Automated backup script
BACKUP DATABASE ecommerce_user_db
TO DISK = '/backup/user_db_backup.bak'
WITH FORMAT, COMPRESSION;
```

### Application Backup
```bash
# Backup configuration files
tar -czf config-backup-$(date +%Y%m%d).tar.gz */application.properties

# Backup JAR files
tar -czf jars-backup-$(date +%Y%m%d).tar.gz */target/*.jar
```

## Troubleshooting

### Service Won't Start
1. Check port availability: `netstat -an | grep PORT`
2. Check logs: `tail -f logs/application.log`
3. Verify Java version: `java -version`
4. Check database connectivity

### High Memory Usage
1. Analyze heap dump: `jmap -heap PID`
2. Adjust JVM settings
3. Check for memory leaks
4. Monitor with JVisualVM

### Database Connection Issues
1. Test connection: `telnet db-host 1433`
2. Verify credentials
3. Check connection pool settings
4. Review SQL Server logs

## Rollback Procedure

```bash
# Stop current version
./stop-all.sh

# Deploy previous version
java -jar backup/user-service-1.0.0-previous.jar &
# Repeat for all services

# Restore database if needed
sqlcmd -S server -U sa -P password -Q "RESTORE DATABASE ..."
```

---

## Quick Reference

### Service URLs
- API Gateway: http://localhost:8080
- User Service: http://localhost:8081
- Product Service: http://localhost:8082
- Order Service: http://localhost:8083
- Payment Service: http://localhost:8084
- Inventory Service: http://localhost:8085

### Key Commands
```bash
# Build: mvn clean install
# Run: mvn spring-boot:run
# Package: mvn clean package
# Test: mvn test
# Clean: mvn clean
```
