# 🌾 AgroConnect - Spring Boot Microservices

AgroConnect is a Spring Boot Microservices based backend application developed for agricultural marketplace management.

The project follows an industry-standard microservices architecture using Spring Boot, Spring Cloud, Eureka Server, API Gateway, PostgreSQL, Spring Security and JWT Authentication.

---

# 🚀 Tech Stack

- Java 17
- Spring Boot 3.5.15
- Spring Security
- Spring Cloud Gateway
- Spring Cloud Eureka
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Maven
- Lombok

---

# 📦 Microservices

## User Service

Responsibilities

- User Registration
- User Login
- BCrypt Password Encryption
- JWT Token Generation
- User Profile API

Port

8081

---

## Product Service

Responsibilities

- Add Product
- Update Product
- Delete Product
- Get Product
- Get All Products

---

## Order Service

Responsibilities

- Create Order
- Cancel Order
- Update Order Status
- Get Orders
- WebClient Communication

---

## API Gateway

Responsibilities

- Central Entry Point
- Route Requests
- Service Discovery

---

## Eureka Server

Responsibilities

- Service Registry
- Service Discovery

---

# 🛠 Current Features

## User Authentication

- Register User
- Login User
- BCrypt Password Encryption
- JWT Token Generation
- Spring Security Configuration
- JWT Authentication Filter

---

## Product Management

- Create Product
- Update Product
- Delete Product
- Get Product
- List Products

---

## Order Management

- Create Order
- Cancel Order
- Update Status
- View Orders

---

## Microservices Communication

- Spring Cloud Gateway
- Eureka Discovery Server
- WebClient Communication

---

# 🏗 Architecture

```
                 Client
                    │
                    ▼
             API Gateway
                    │
        Service Discovery
                    │
                 Eureka
                    │
      ┌─────────────┼─────────────┐
      ▼             ▼             ▼
 User Service   Product Service  Order Service
                    │
                    ▼
               PostgreSQL
```

---

# 🔐 Authentication Flow

```
Client

↓

POST /api/users/login

↓

User Service

↓

Verify Email & Password

↓

Generate JWT

↓

Return JWT Token

↓

Client stores Token

↓

Authorization: Bearer <JWT>

↓

Protected APIs
```

---

# 📂 Project Structure

```
AgroConnect
│
├── userservice
├── productservice
├── orderservice
├── apigateway
└── eurekaserver
```

---

# 📚 Concepts Used

- Microservices Architecture
- REST APIs
- DTO Pattern
- Layered Architecture
- Constructor Dependency Injection
- Spring Security
- JWT Authentication
- BCrypt Password Encryption
- JPA Repository
- WebClient
- Eureka Discovery
- API Gateway

---

# 🚀 Future Enhancements

- JWT Validation in API Gateway
- Secure Product Service
- Secure Order Service
- Role Based Authentication
- Docker
- Docker Compose
- AWS Deployment
- CI/CD Pipeline

---

# 👨‍💻 Developer

Aakash Patil

Backend Developer

Spring Boot Microservices Project
