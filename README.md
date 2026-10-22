# CRM Backend

A CRM backend I built with Java 17 and Spring Boot to practice backend development. It manages users, teams, customers, leads and deals, with JWT authentication and role-based access.

## Features

- **Auth**: login, refresh token, logout (JWT)
- **Users & Teams**: create/update users, assign roles and teams
- **Customers**: CRUD, reassign to another owner, search with filters
- **Leads**: CRUD, search, status flow (`NEW → CONTACTED → QUALIFIED → CONVERTED` or `DISQUALIFIED`), and convert a qualified lead into a customer
- **Deals**: linked to an active customer, starts as `IN_PROGRESS`, can be marked `WON` or `LOST` *(still in progress)*
## Roles

| Role | Can access |
|------|-----------|
| Admin | everything |
| Manager | own records + records of their team |
| Sales Employee | only their own records |

## Tech stack

Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA, MySQL, MapStruct, Lombok, Swagger (springdoc), JUnit 5 + Mockito

## Project structure

The code is organized by feature:

```
com.adham.crm_backend
├── auth/        login, refresh tokens
├── user/        users and roles
├── team/        teams
├── customer/    customers
├── lead/        leads and conversion
├── deal/        deals
└── common/      security, exceptions, shared specifications, utils
```

## How to run

**Requirements:** Java 17+, MySQL running locally.

1. Create a database named `crm` in MySQL.
2. Set these environment variables:
```
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_base64_secret
# optional (defaults shown in application.yml)
DB_URL=jdbc:mysql://localhost:3306/crm?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
```

3. Run the app:
```
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`. Tables are created automatically (`ddl-auto: update`).

## API docs

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

Login first, then click **Authorize** and paste the access token.

## Main endpoints

- `/api/v1/auth/*`
- `/api/v1/users/*`
- `/api/v1/teams/*`
- `/api/v1/customers/*` (including `/search`)
- `/api/v1/leads/*` (including `/search`, `/{id}/status`, `/{id}/convert`)
- `/api/v1/deals/*`
## Tests

```
./mvnw test
```

Unit tests with JUnit 5 and Mockito cover:
- access rules (`AssertControl`)
- lead status transitions
- lead creation and lead-to-customer conversion
  Deals tests are not written yet.

## What I learned / decisions

- Using JPA Specifications for dynamic search, with access rules (admin / manager / owner) applied on top of every query
- Storing refresh tokens as hashes instead of plain text
- Atomic update when converting a lead, so the same lead can't be converted twice at the same time
- A status enum with allowed transitions, to block invalid changes
## Roadmap

- [ ] Finish deals (search, tests)
- [ ] Opportunities
- [ ] More tests
## Author

Adham Mohamed Hassan