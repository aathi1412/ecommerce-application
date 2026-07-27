# 🛒 E-Commerce Backend API

A RESTful backend application for an E-Commerce platform built with **Java**, **Spring Boot**, and **MySQL**. The project provides APIs for managing products, categories, carts, and orders while following a clean layered architecture.

---

## 🚀 Features

### Product Management
- Create, update, delete products
- View all products
- View product details
- Product categorization

### Category Management
- Create categories
- Update categories
- Delete categories
- Retrieve categories

### Shopping Cart
- Add products to cart
- Update cart quantity
- Remove products from cart
- View cart

### Order Management
- Place orders
- View order history
- Order details

### Database
- MySQL database
- Flyway database migrations
- Spring Data JPA with Hibernate

### Developer Features
- RESTful API design
- Layered Architecture
- DTO Pattern
- Global Exception Handling
- Validation
- Dockerized MySQL

---

# 🏗️ Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Flyway
- Maven
- Docker
- Git
- Postman

---

# 📂 Project Structure

```
src
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
├── config
└── resources
```

---

# ⚙️ Getting Started

## Prerequisites

- Java 24+
- Maven
- Docker
- MySQL

## Clone Repository

```bash
git clone https://github.com/aathi1412/ecommerce-application.git
cd ecommerce-application
```

## Configure Database

Update `application.properties` with your MySQL credentials.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Start MySQL

```bash
docker compose up -d
```

## Run

```bash
mvn spring-boot:run
```

---

# 📦 API Modules

- Products
- Categories
- Cart
- Orders

---

# 🗄️ Database

- MySQL
- Spring Data JPA
- Hibernate
- Flyway Migration

---

# 🧪 API Testing

You can test the APIs using:

- Postman
- Thunder Client
- Insomnia

---

# 📈 Future Enhancements

- JWT Authentication
- User Management
- Payment Gateway
- Wishlist
- Product Reviews
- Inventory Management
- Swagger/OpenAPI Documentation
- Unit & Integration Tests

---

# 📚 Learning Outcomes

- Spring Boot REST API Development
- Spring Data JPA
- Hibernate ORM
- MySQL Database Design
- Flyway Migrations
- Docker
- Exception Handling
- Layered Architecture
- REST API Best Practices

---

# 👨‍💻 Author

**Aathi**

GitHub: https://github.com/aathi1412
