# CRM Backend Architecture

## 1. Project Goal

The goal of this project is to build a production-style CRM backend system using Spring Boot.

The system should support customer management, deal pipeline management, follow-ups, interactions, dashboards, authentication, authorization, audit logging, testing, and deployment readiness.

## 2. Architecture Style

The project uses a Modular Monolith architecture.

This means the application is deployed as one backend service, but the code is organized into clear business modules.

Initial modules:

- Auth
- User
- Customer
- Deal
- Interaction
- FollowUp
- Dashboard
- AuditLog

## 3. Layered Architecture

Each module follows a layered structure:

- Controller: handles HTTP requests and responses.
- Service: contains business logic.
- Repository: handles database access.
- Entity: represents database tables.
- DTO: defines API request and response models.
- Mapper: maps between entities and DTOs.
- Exception: handles domain-specific errors.
- Security: handles authentication and authorization.

## 4. Main Design Decisions

### 4.1 DTO Pattern

Entities will not be exposed directly through the API.
DTOs are used to control request and response shapes and avoid leaking internal data.

### 4.2 Repository Pattern

Spring Data JPA repositories are used to abstract database access.

### 4.3 JWT Authentication

The system will use stateless JWT access tokens with refresh tokens.

### 4.4 Role-Based Authorization

The system supports:

- ADMIN
- MANAGER
- SALES_EMPLOYEE

### 4.5 Ownership-Based Authorization

Role checks are not enough.
A sales employee should only access data assigned to them.

Ownership checks will be implemented in the service layer.

### 4.6 Soft Delete

Important business records like customers and deals will use soft delete instead of hard delete.

### 4.7 BigDecimal for Money

Deal amounts will use BigDecimal, not double or float.

### 4.8 Specification Pattern

Dynamic search and filtering will use the Specification pattern.

### 4.9 Observer Pattern / Domain Events

Important business actions will publish domain events.

Examples:

- Deal stage changed
- Customer reassigned
- Follow-up completed
- Customer deleted

These events can later be used for audit logging and notifications.

## 5. Database

Database: MySQL 8.x

Naming convention:

- Table names: plural, snake_case
- Column names: snake_case

Examples:

- users
- roles
- customers
- deals
- followups
- activity_logs

## 6. Security Rules

- Passwords are stored using BCrypt hashing.
- JWT secrets are never committed to Git.
- Refresh tokens are stored hashed in the database.
- APIs return clean JSON errors for 401 and 403.
- Row-level ownership checks are enforced in service methods.

## 7. Testing Strategy

Testing will include:

- Unit tests with JUnit 5 and Mockito
- Repository/integration tests with Testcontainers and MySQL
- Controller tests with MockMvc
- End-to-end flow tests with SpringBootTest

## 8. Deployment

The project will be Dockerized using:

- Dockerfile
- docker-compose.yml
- .env.example

CI will be configured with GitHub Actions.