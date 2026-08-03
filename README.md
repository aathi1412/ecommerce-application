# 🛒 Ecommerce API

A Spring Boot REST API for a simple e-commerce backend: product catalog, user profiles, shopping cart, and order checkout. Backed by MySQL with Flyway migrations, and includes a Docker Compose setup for one-command local development.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Domain Model](#domain-model)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Option A: Docker Compose](#option-a-docker-compose-recommended)
  - [Option B: Run Locally](#option-b-run-locally)
- [API Reference](#api-reference)
- [Testing](#testing)
- [Known Limitations](#known-limitations)
- [Roadmap](#roadmap)
- [License](#license)

---

## Features

- **Products** — create, update, delete, list, list active-only, search by keyword
- **Users** — create/update profile with a linked address, look up by email
- **Cart** — add/update/remove items per user, with stock validation (out-of-stock and insufficient-stock checks)
- **Orders** — checkout a cart into an order (snapshots item prices at time of purchase), view order history
- Consistent JSON error responses (timestamp, status, error, message, request path)
- Database schema managed via Flyway migrations

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4.1 (Web MVC, Data JPA, Actuator) |
| Database | MySQL, Flyway migrations |
| Local dev | Docker Compose (app + MySQL + CloudBeaver DB browser) |
| Testing | Postman collection (included in repo) |
| Tooling | Maven, Lombok |

---

## Domain Model

```
User (1) ─── (1) Address
User (1) ─── (1) Cart ─── (*) CartItem ─── (1) Product
User (1) ─── (*) Order ─── (*) OrderItem ─── (1) Product
```

- A `Cart` belongs to exactly one `User` and holds `CartItem`s, each snapshotting the product's price at the time it was added.
- Checking out (`POST /api/orders/{userId}`) converts the current cart into an `Order` with its own `OrderItem`s (again snapshotting price), then clears the cart.

---

## Project Structure

```
ecommerce-API/
├── docker-compose.yml
├── postman client requests/
│   └── ecommerce.postman_collection.json
└── ecommerce-backend/
    ├── pom.xml
    └── src/main/
        ├── java/com/app/ecommerce/
        │   ├── controller/    # REST controllers (products, users, cart, orders)
        │   ├── dto/           # Request/response records
        │   ├── enums/         # Role, OrderStatus
        │   ├── exceptions/    # Custom exceptions + global handler
        │   ├── models/        # JPA entities
        │   ├── repository/    # Spring Data repositories
        │   └── service/       # Business logic
        └── resources/
            ├── application.properties
            └── db/migration/  # Flyway SQL migrations
```

---

## Getting Started

### Option A: Docker Compose (recommended)

This starts the API, a MySQL instance, and CloudBeaver (a web-based DB browser) together:

```bash
docker compose up
```

- API: `http://localhost:8080`
- MySQL: exposed on `localhost:3307` (mapped from container port 3306)
- CloudBeaver (DB browser): `http://localhost:8978`

> Note: `docker-compose.yml` pulls a pre-built image (`1adhi/ecommerce-application`) rather than building from this source. If you've made local changes, build and push your own image, or point the compose file at a local Dockerfile instead.

### Option B: Run Locally

**Prerequisites:** Java 21+, Maven, MySQL

Create the database:

```sql
CREATE DATABASE ecommerce_db;
```

Update `ecommerce-backend/src/main/resources/application.properties` with your local MySQL credentials, or export them as environment variables and reference them there instead of hardcoding:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Then run:

```bash
cd ecommerce-backend
./mvnw spring-boot:run
```

Flyway applies all migrations automatically on startup.

---

## API Reference

Base URL: `http://localhost:8080`

### Products (`/api/products`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | List all products |
| GET | `/active` | List active products *(see [Known Limitations](#known-limitations))* |
| GET | `/{id}` | Get a product by ID |
| GET | `/search?keyword=` | Search products by name keyword |
| POST | `/add` | Create a product |
| PUT | `/update/{id}` | Update a product |
| DELETE | `/delete/{id}` | Delete a product |

### Users (`/api/users`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/` | List all users |
| GET | `/{email}` | Get a user by email |
| POST | `/create` | Create a user |
| PUT | `/update` | Update a user's profile/address |

### Cart (`/api/cart`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/?userId=` | Get a user's cart |
| POST | `/?userId=` | Add an item to the cart |
| PUT | `/items?userId=` | Update an item's quantity |
| DELETE | `/items?userId=&productId=` | Remove an item from the cart |

### Orders (`/api/orders`)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/{userId}` | Get a user's order history |
| POST | `/{userId}` | Checkout the current cart into a new order |

A ready-to-import Postman collection is included at `postman client requests/ecommerce.postman_collection.json`.

---

## Testing

Import the Postman collection and try:

- Create a product, then fetch it by ID and by search keyword
- Create a user, add items to their cart, then checkout and confirm the cart is cleared
- Add more stock than is available and confirm you get an `InsufficientStockException` (409)
- Try to add an out-of-stock product and confirm `OutOfStockException` (409)

---

## Known Limitations

This API is functional but not yet safe to use with real user data. Worth knowing before you extend or deploy it:

- **No authentication or authorization.** Every endpoint is open — there's no login, no session/token check, and no Spring Security dependency in the project at all. Anyone who can reach the server can create/update/delete products or read the full user list.
- **Passwords are stored in plaintext.** There's no password hashing (e.g. BCrypt) anywhere in the codebase yet. Add a `PasswordEncoder` before storing or checking any real password.
- **No ownership checks on `userId`.** Cart and order endpoints accept `userId` as a plain parameter — any caller can view or modify another user's cart or order history by passing a different ID. This needs to be tied to an authenticated session once auth is added.
- **`GET /api/products/active` may error with more than one active product.** The underlying repository method is typed to return a single `Optional<Product>` when it should return a `List<Product>`; with multiple active products this can throw a runtime exception instead of listing them.
- Local dev credentials in `docker-compose.yml` and `application.properties` (`ecommerce`/`ecommerce`, `root`/`root`) are fine for local-only use but should be replaced with environment variables before any shared or public deployment.

---

## Roadmap

- [ ] Add Spring Security with JWT-based authentication
- [ ] Hash passwords with BCrypt
- [ ] Scope cart/order access to the authenticated user instead of a raw `userId` parameter
- [ ] Fix `findByActiveTrue()` to return a `List<Product>`
- [ ] Add pagination to product listing and search
- [ ] Add integration tests covering cart and checkout flows
- [ ] Externalize all credentials to environment variables
- [ ] CI/CD pipeline

---
