# CRM Backend (Spring Boot)

A production-style Customer Relationship Management (CRM) backend built with Java 17 and Spring Boot. This repository implements a modular-monolith architecture with layered separation (controllers, services, repositories, entities, DTOs, mappers) and JWT-based authentication.

This README explains how to run and test the project locally, the main architecture, important endpoints, and developer notes.

---

## Checklist (what this README contains)

- Project summary and key features
- Tech stack
- Project structure / architecture overview
- Prerequisites
- Quick start (local development with Docker)
- Environment configuration and profiles
- Authentication and important endpoints
- API documentation (Swagger)
- Seed data and initial credentials
- Running tests
- Contribution and contact information

---

## Project summary

This backend provides user, team and customer management APIs with role-based access control (RBAC). Security uses short-lived access tokens and longer-lived refresh tokens. The REST API prefix is `/api/v1`.

Key components:
- Authentication (login / refresh / logout)
- User management (create/update/activate/deactivate, assign team/roles)
- Customer management (CRUD, reassign)
- Seeders to create initial roles and an admin user on startup

## Tech stack

- Java 17
- Spring Boot (Web, Security, Data JPA)
- Spring Security + JWT
- MapStruct (mappers)
- Lombok
- Hibernate / JPA
- MySQL
- Maven (wrapper `mvnw` included)
- Swagger / OpenAPI (springdoc)
- JUnit 5, Mockito, Testcontainers (for tests)

## Project structure (high level)

Source root: `src/main/java/com/adham/crm_backend`

- `controller/` — REST controllers annotated with `@RestController`
- `service/` — business logic
- `repository/` — Spring Data JPA repositories
- `entity/` — JPA entities
- `dto/` — request/response DTOs with validation
- `mapper/` — MapStruct mappers
- `config/` — configuration classes (security, OpenAPI, seeders)

See `docs/architecture.md` for more details.

## Prerequisites

- Java 17+
- Maven (or use the provided `mvnw` wrapper)
- Docker & Docker Compose (optional but recommended for database during development)

## Quick start (local development)

1) Start MySQL with the provided Docker Compose (defaults match `application-dev.yml`):

```powershell
docker-compose up -d
```

2) (Optional) Override environment variables if needed (PowerShell example):

```powershell
# Example (uncomment and edit if you want to set env vars in the current session)
#$env:DB_URL = "jdbc:mysql://localhost:3309/crm_dev?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
#$env:DB_USERNAME = "adham"
#$env:DB_PASSWORD = "adham123"
#$env:SPRING_PROFILES_ACTIVE = "dev"
```

3) Run the application using the Maven wrapper:

```powershell
# Run in development mode
.\mvnw spring-boot:run

# Or build and run the jar (skip tests for faster local build)
.\mvnw -DskipTests package ; java -jar target\*.jar
```

After startup the server listens on port 8080 by default (unless overridden by config).

## Configuration & profiles

- `src/main/resources/application.yml` — base configuration (sets default profile to `dev`).
- `src/main/resources/application-dev.yml` — development profile (DB connection, JWT defaults). See this file for example values.
- `src/main/resources/application-prod.yml` — production profile (override secrets via environment variables in production).

Important defaults from `application-dev.yml`:
- DB URL: `jdbc:mysql://localhost:3309/crm_dev` (container maps host port 3309 to MySQL 3306)
- DB user: `adham`
- DB password: `adham123`
- JWT secret is stored as a Base64 string in the dev profile — replace in production with a safe secret management system.

## Authentication (JWT)

Authentication endpoints:
- POST `/api/v1/auth/login` — returns access token and refresh token
- POST `/api/v1/auth/refresh` — exchange a refresh token for a new access token
- POST `/api/v1/auth/logout` — invalidate a refresh token

Use the `Authorization` header with a Bearer access token for protected endpoints:

```
Authorization: Bearer <access-token>
```

Access tokens are short-lived (example: 15 minutes). Refresh tokens have longer TTL (example: 7 days) and are stored/persisted for revocation.

## API documentation (Swagger / OpenAPI)

OpenAPI is configured via `OpenApiConfig`. After the app starts you can access:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Use the Swagger UI to test endpoints and provide the Bearer token via the UI "Authorize" control.

## Seed data

Two seeders run at startup to populate initial data if not present:
- `RoleSeeder` — inserts role records defined in `RoleName` enum.
- `UserSeeder` — creates a default admin user if not present.

Default admin credentials (development only):
- Email: `admin@crm.com`
- Password: `123456`

Change the default password immediately after first startup in any real environment.

## Important endpoints (summary)

- Authentication: `/api/v1/auth/*`
- Users management: `/api/v1/users/*` (role-restricted)
- Customers management: `/api/v1/customers/*`
- Teams management: `/api/v1/teams/*` (if present)

Refer to Swagger for full endpoint details, request and response DTOs, and error schemas.

## Running tests

To run unit and integration tests locally:

```powershell
.\mvnw test
```

Some integration tests may rely on Testcontainers or a running Docker environment.

## Docker & production notes

- `docker-compose.yml` included to run a MySQL database for development. It does not build the application container.
- A `Dockerfile` for the application is not included; create one if you need to containerize the app itself.
- Always inject production secrets (DB credentials, JWT secret) via environment variables or a secret manager — do not commit them to source control.

## Contribution

Contributions are welcome. Suggested workflow:
1. Open an issue describing the bug or feature.
2. Create a feature branch, make changes with tests, and open a Pull Request with a clear description and rationale.

## Author / Contact

Adham Mohamed Hassan Abdel Fadil
