# CRM Backend - Customer Relationship Management System

A production-ready Customer Relationship Management (CRM) backend built with Java 17 and Spring Boot. This project implements a modular monolithic architecture with complete user, team, customer, and lead management capabilities, featuring JWT-based authentication and role-based access control.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration & Profiles](#configuration--profiles)
- [Authentication & Authorization](#authentication--authorization)
- [API Documentation](#api-documentation)
- [Database Setup](#database-setup)
- [Seed Data](#seed-data)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Docker & Deployment](#docker--deployment)
- [Development Workflow](#development-workflow)
- [Contact](#contact)

---

##  Project Overview

This CRM backend provides a comprehensive REST API for managing customer relationships, sales leads, users, and teams. It's designed with enterprise-grade architecture patterns including:

- **Modular Monolith Architecture**: Code organized into business modules (Auth, User, Customer, Lead, Team) but deployed as a single service
- **Layered Architecture**: Each module follows Controller → Service → Repository → Entity pattern
- **Security**: Stateless JWT authentication with refresh tokens and role-based access control
- **Data Access Control**: Row-level ownership checks ensure users only access their assigned data
- **Scalability**: Built to support enterprise features like audit logging, domain events, and soft deletes

### Business Use Cases

1. **User Management**: Create and manage users with specific roles and team assignments
2. **Lead Management**: Track potential customers through various sales stages (New → Qualified → Converted)
3. **Customer Management**: Maintain customer information with assignment to sales teams
4. **Team Management**: Organize sales teams and manage team members
5. **Role-Based Access**: Enforce security through ADMIN, MANAGER, and SALES_EMPLOYEE roles

---

##  Key Features

### Authentication & Security
- JWT-based authentication with access and refresh tokens
- Stateless API design - no session storage required
- Secure password hashing using BCrypt
- Token refresh mechanism for extended sessions
- Logout with token invalidation

### User Management
- Create, update, retrieve, and manage users
- Activate/deactivate user accounts
- Assign users to teams
- Role-based permissions (ADMIN, MANAGER, SALES_EMPLOYEE)
- User status tracking

### Customer Management
- Full CRUD operations on customers
- Customer status tracking (ACTIVE, INACTIVE, etc.)
- Customer ownership and team assignment
- Reassign customers between team members
- Search and filter customers
- Role-based data visibility (Admins see all, Managers see team data, Sales Employees see own data)

### Lead Management
- Track sales leads through multiple statuses (NEW, QUALIFIED, CONVERTED, LOST)
- Lead source tracking
- Convert qualified leads to customers
- Assign leads to team members
- Search and filter leads
- Lead status transitions

### Advanced Features
- **Dynamic Search**: Search and filter using specifications pattern
- **Pagination**: All list endpoints support pagination
- **Soft Delete**: Important records can be archived instead of permanently deleted
- **API Documentation**: Built-in Swagger UI for testing endpoints
- **Error Handling**: Comprehensive error responses with meaningful messages

---

## 🛠 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.5.15 |
| **Security** | Spring Security + JWT | 0.11.5 |
| **Database** | MySQL | 8.0 |
| **ORM** | Hibernate/JPA | Spring Data JPA |
| **Mapping** | MapStruct | 1.6.3 |
| **Code Generation** | Lombok | Latest |
| **API Documentation** | Springdoc OpenAPI | 2.8.17 |
| **Build Tool** | Maven | 3+ (wrapper included) |
| **Testing** | JUnit 5, Mockito, Testcontainers | Latest |
| **Containerization** | Docker & Docker Compose | Latest |

---

##  Project Structure

```
crm-backend/
├── src/
│   ├── main/
│   │   ├── java/com/adham/crm_backend/
│   │   │   ├── CrmBackendApplication.java          # Spring Boot entry point
│   │   │   ├── auth/                               # Authentication module
│   │   │   │   ├── AuthController.java             # Login/refresh/logout endpoints
│   │   │   │   ├── service/                        # Auth business logic
│   │   │   │   ├── dto/                            # Request/response DTOs
│   │   │   │   ├── exception/                      # Auth-specific exceptions
│   │   │   │   └── RefreshToken*.java              # Refresh token entity & repository
│   │   │   ├── user/                               # User management module
│   │   │   │   ├── UserController.java             # User CRUD endpoints
│   │   │   │   ├── UserService.java                # User business logic
│   │   │   │   ├── entity/                         # User, Role entities
│   │   │   │   ├── repository/                     # UserRepository, RoleRepository
│   │   │   │   ├── dto/                            # User request/response DTOs
│   │   │   │   ├── mapper/                         # MapStruct user mapper
│   │   │   │   └── exception/                      # User-specific exceptions
│   │   │   ├── customer/                           # Customer management module
│   │   │   │   ├── CustomerController.java         # Customer CRUD endpoints
│   │   │   │   ├── CustomerService.java            # Customer business logic
│   │   │   │   ├── Customer.java                   # Customer entity
│   │   │   │   ├── CustomerRepository.java         # Data access
│   │   │   │   ├── CustomerStatus.java             # Status enum
│   │   │   │   ├── dto/                            # Customer DTOs
│   │   │   │   ├── specification/                  # Search specifications
│   │   │   │   └── exception/                      # Customer exceptions
│   │   │   ├── lead/                               # Lead management module
│   │   │   │   ├── LeadController.java             # Lead endpoints
│   │   │   │   ├── LeadService.java                # Lead business logic
│   │   │   │   ├── Lead.java                       # Lead entity
│   │   │   │   ├── LeadStatus.java                 # Lead status enum
│   │   │   │   ├── LeadSource.java                 # Lead source enum
│   │   │   │   ├── dto/                            # Lead DTOs
│   │   │   │   └── specification/                  # Lead search
│   │   │   ├── team/                               # Team management module
│   │   │   │   ├── Team.java                       # Team entity
│   │   │   │   ├── TeamService.java                # Team operations
│   │   │   │   └── repository/                     # TeamRepository
│   │   │   └── common/                             # Shared utilities
│   │   │       ├── config/                         # Spring configurations
│   │   │       │   ├── SecurityConfig.java         # Security configuration
│   │   │       │   ├── OpenApiConfig.java          # Swagger setup
│   │   │       │   └── seeders/                    # Database seeders
│   │   │       ├── security/                       # Security utilities
│   │   │       │   ├── SecurityUtils.java          # Get current user
│   │   │       │   └── AssertControl.java          # Authorization checks
│   │   │       ├── exception/                      # Global exceptions
│   │   │       ├── specification/                  # JPA specifications
│   │   │       ├── util/                           # Utilities
│   │   │       └── documentation/                  # Swagger annotations
│   │   └── resources/
│   │       ├── application.yml                     # Main config (sets dev profile)
│   │       ├── application-dev.yml                 # Development environment
│   │       └── application-prod.yml                # Production environment
│   └── test/
│       └── java/com/adham/crm_backend/
│           ├── CrmBackendApplicationTests.java
│           ├── TestFixtures.java                   # Test data builders
│           └── [module tests]/
├── docs/
│   └── architecture.md                             # Detailed architecture documentation
├── docker-compose.yml                             # MySQL database for local development
├── pom.xml                                        # Maven project file
├── mvnw & mvnw.cmd                               # Maven wrapper for any OS
└── README.md                                      # This file
```

### Module Organization

Each major module (auth, user, customer, lead, team) follows this layered structure:

```
module/
├── ModuleController.java          # REST endpoints
├── ModuleService.java             # Business logic
├── ModuleRepository.java          # Database access
├── Module.java                    # Entity (DB model)
├── ModuleMapper.java              # DTO ↔ Entity mapping
├── dto/                           # Request/Response DTOs
│   ├── CreateModuleRequest.java
│   ├── UpdateModuleRequest.java
│   └── ModuleResponse.java
├── exception/                     # Domain-specific exceptions
│   └── Module*Exception.java
└── specification/                 # Search filters (if applicable)
    └── ModuleSpecifications.java
```

This structure ensures:
- **Separation of Concerns**: Each layer has a single responsibility
- **Testability**: Easy to test each layer independently
- **Maintainability**: Clear organization makes code easy to navigate
- **Scalability**: New features can be added to modules without affecting others

---

## 📋 Prerequisites

Before running this project, ensure you have:

- **Java 17 or higher** ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.6+** (optional - Maven wrapper is included)
- **Docker & Docker Compose** (for local MySQL database - recommended)
- **Git** (for version control)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

### Verify Installation

```powershell
# Check Java version
java -version

# Check Maven version (if installed)
mvn -version
```

---

##  Quick Start

### Step 1: Clone the Repository

```powershell
git clone https://github.com/yourusername/crm-backend.git
cd crm-backend
```

### Step 2: Start MySQL Database

The project includes a `docker-compose.yml` file that starts a MySQL database with pre-configured credentials:

```powershell
# Start the MySQL container in the background
docker-compose up -d

# Verify it's running
docker ps
```

**Default Database Credentials:**
- Host: `localhost`
- Port: `3309` (mapped to MySQL's 3306)
- Database: `crm_dev`
- Username: `adham`
- Password: `adham123`

### Step 3: Run the Application

#### Option A: Run with Maven Wrapper (Recommended)

```powershell
# Development mode with hot reload
.\mvnw spring-boot:run

# Or build and run as JAR
.\mvnw -DskipTests clean package
java -jar target/crm-backend-0.0.1-SNAPSHOT.jar
```

#### Option B: Run in IDE

1. Open the project in your IDE (IntelliJ IDEA recommended)
2. Navigate to `CrmBackendApplication.java`
3. Click the green "Run" button next to the main method
4. Or use keyboard shortcut: `Shift + F10` (IntelliJ)

### Step 4: Verify Application Started

After startup, check that:

1. **Application is running** on `http://localhost:8080`
2. **Health check endpoint** (should return status: UP):
   ```powershell
   curl http://localhost:8080/actuator/health
   ```
3. **Swagger UI** is accessible:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

**Expected output in terminal:**
```
2026-08-04 10:30:15.123  INFO  --- : Started CrmBackendApplication in 5.234 seconds (JVM running for 5.567)
```

---

## ⚙️ Configuration & Profiles

### Application Profiles

The project uses Spring profiles for different environments:

#### `application.yml` (Base Configuration)
Sets the default active profile and global settings:
```yaml
spring:
  application:
    name: crm-backend
  profiles:
    active: dev          # Default profile
server:
  port: 8080            # API port
```

#### `application-dev.yml` (Development)
Configured for local development with debug logging:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3309/crm_dev
    username: adham
    password: adham123
  jpa:
    hibernate:
      ddl-auto: update  # Auto-create/update tables

jwt:
  secretKey: VGhpc0lzQVN1cGVyU2VjdXJlMjU2Qml0S2V5Rm9ySldURGV2RW52aXJvbm1lbnQxMjM0NTY3ODkw
  access-token-ttl-minutes: 15
  refresh-token-ttl-days: 7
```

#### `application-prod.yml` (Production)
**Important**: Secrets must be injected via environment variables in production.

### Using Different Profiles

```powershell
# Run with specific profile
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw spring-boot:run

# Or via Maven property
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or set environment variables
$env:DB_URL = "jdbc:mysql://your-host:3306/crm_prod"
$env:DB_USERNAME = "prod_user"
$env:DB_PASSWORD = "secure_password"
$env:JWT_SECRET_KEY = "your-secure-256-bit-key"
```

### Configuration Parameters

| Parameter | Description | Dev Default |
|-----------|-------------|-------------|
| `DB_URL` | Database connection string | `jdbc:mysql://localhost:3309/crm_dev` |
| `DB_USERNAME` | Database username | `adham` |
| `DB_PASSWORD` | Database password | `adham123` |
| `JWT_SECRET_KEY` | Base64-encoded 256-bit JWT secret | Provided in dev config |
| `JWT_ACCESS_TOKEN_TTL` | Access token lifetime in minutes | `15` |
| `JWT_REFRESH_TOKEN_TTL` | Refresh token lifetime in days | `7` |

---

##  Authentication & Authorization

### How Authentication Works

This project uses **JWT (JSON Web Token)** based authentication:

1. **User logs in** with email and password → receives `accessToken` and `refreshToken`
2. **Access token** (15 min TTL) is used in API requests via `Authorization: Bearer <token>` header
3. **Refresh token** (7 day TTL) is stored in database and used to get new access tokens
4. **Access token expires** → use refresh token to get new access token without re-logging in
5. **Logout** invalidates the refresh token, requiring fresh login

### Authentication Flow

```
1. POST /api/v1/auth/login
   Request: { "email": "user@crm.com", "password": "password123" }
   Response: { "accessToken": "eyJ...", "refreshToken": "eyJ..." }

2. GET /api/v1/customers (with Authorization header)
   Header: Authorization: Bearer eyJ...
   Response: [list of customers]

3. Access token expires after 15 minutes

4. POST /api/v1/auth/refresh
   Request: { "refreshToken": "eyJ..." }
   Response: { "accessToken": "eyJ...", "refreshToken": "eyJ..." }

5. POST /api/v1/auth/logout
   Request: { "refreshToken": "eyJ..." }
   Response: 204 No Content
```

### Authentication Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| `POST` | `/api/v1/auth/login` | Authenticate and get tokens |
| `POST` | `/api/v1/auth/refresh` | Get new access token using refresh token |
| `POST` | `/api/v1/auth/logout` | Invalidate refresh token |

### Role-Based Access Control (RBAC)

Three roles are available:

| Role | Permissions | Typical User |
|------|-----------|--------------|
| **ADMIN** | Full access to all endpoints and data | System administrator |
| **MANAGER** | Access team and subordinate data, create/convert leads | Sales manager |
| **SALES_EMPLOYEE** | Access only assigned customers and leads | Sales representative |

### Authorization Examples

```java
// Only admins can access
@PreAuthorize("hasAuthority('ROLE_ADMIN')")

// Admins and managers
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")

// Anyone authenticated
@PreAuthorize("isAuthenticated()")
```

### Data Access Rules

- **ADMIN**: Sees all customers and leads
- **MANAGER**: Sees customers and leads owned by team members + their own
- **SALES_EMPLOYEE**: Sees only their own customers and leads

Row-level security is enforced at the service layer.

---

## 📚 API Documentation

### Swagger UI

Once the application starts, access the interactive API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

**Features:**
- Browse all endpoints by category
- View request/response schemas
- Test endpoints directly from UI
- Use "Authorize" button to add Bearer token
- See example requests and responses

### OpenAPI JSON

Raw OpenAPI specification available at:

```
http://localhost:8080/v3/api-docs
```

Can be imported into Postman or other API clients.

### Using Swagger UI to Test Endpoints

1. Open `http://localhost:8080/swagger-ui/index.html`
2. Click the "Authorize" button (lock icon)
3. Paste your access token: `eyJ0eXAiOiJKV1QiLC...`
4. Click "Authorize" and close the dialog
5. Expand any endpoint and click "Try it out"
6. Fill in parameters and click "Execute"
7. View the response

---

## 🗄️ Database Setup

### Automatic Schema Creation

On first startup, Hibernate automatically creates database tables based on entity definitions:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Automatically creates/updates tables
```

### Database Schema Overview

**Key Tables:**

| Table | Purpose |
|-------|---------|
| `users` | User accounts with email and password |
| `roles` | Available roles (ADMIN, MANAGER, SALES_EMPLOYEE) |
| `user_roles` | Many-to-many relationship between users and roles |
| `teams` | Sales teams and departments |
| `customers` | Customer records |
| `leads` | Sales leads and opportunities |
| `refresh_tokens` | Valid refresh tokens for logout support |

### Viewing Database

```powershell
# Access MySQL CLI
docker exec -it crm-mysql mysql -u adham -p

# Enter password: adham123

# View tables
SHOW TABLES;

# View table structure
DESCRIBE customers;

# Query data
SELECT * FROM users;
```

---

## 📦 Seed Data

### Automatic Seeding

On application startup, two seeders automatically run if data is missing:

#### 1. **RoleSeeder**
Creates default roles in the database:
- `ROLE_ADMIN` - Full system access
- `ROLE_MANAGER` - Team and reports management
- `ROLE_SALES_EMPLOYEE` - Customer and lead management

```java
// Runs automatically if roles table is empty
public class RoleSeeder implements CommandLineRunner {
    // Creates ADMIN, MANAGER, SALES_EMPLOYEE roles
}
```

#### 2. **UserSeeder**
Creates a default admin user if no users exist:

```
Email: admin@crm.com
Password: 123456
Roles: ADMIN
```

### First Login

1. Start the application (seeders run automatically)
2. Login at `/api/v1/auth/login`:
   ```json
   {
     "email": "admin@crm.com",
     "password": "123456"
   }
   ```
3. **IMPORTANT**: Change this password immediately using `/api/v1/users/{id}` endpoint
4. Create additional users for your team

### Disable Seeding (Advanced)

To disable automatic seeding, comment out the seeders in `SecurityConfig`:

```java
@Configuration
public class SecurityConfig {
    // @Bean
    // public RoleSeeder roleSeeder(RoleRepository roleRepository) {
    //     return new RoleSeeder(roleRepository);
    // }
}
```

---

## 🔌 API Endpoints

### Authentication

```
POST   /api/v1/auth/login          # Login with credentials
POST   /api/v1/auth/refresh        # Get new access token
POST   /api/v1/auth/logout         # Logout and invalidate token
```

### User Management

```
GET    /api/v1/users/me            # Get current authenticated user
GET    /api/v1/users               # List all users (paginated)
GET    /api/v1/users/{id}          # Get user by ID
POST   /api/v1/users               # Create new user
PATCH  /api/v1/users/{id}          # Update user
PATCH  /api/v1/users/{id}/activate    # Activate user
PATCH  /api/v1/users/{id}/deactivate  # Deactivate user
POST   /api/v1/users/{id}/assign-team # Assign user to team
```

### Customer Management

```
GET    /api/v1/customers           # List customers (paginated)
GET    /api/v1/customers/{id}      # Get customer details
POST   /api/v1/customers           # Create new customer
PATCH  /api/v1/customers/{id}      # Update customer
PATCH  /api/v1/customers/{id}/reassign  # Reassign customer
GET    /api/v1/customers/search    # Search and filter customers
```

### Lead Management

```
GET    /api/v1/leads               # List leads (paginated)
GET    /api/v1/leads/{id}          # Get lead details
POST   /api/v1/leads               # Create new lead
PATCH  /api/v1/leads/{id}          # Update lead
PATCH  /api/v1/leads/{id}/status   # Change lead status
GET    /api/v1/leads/search        # Search and filter leads
POST   /api/v1/leads/{id}/convert  # Convert lead to customer
```

### Team Management

```
GET    /api/v1/teams               # List teams
GET    /api/v1/teams/{id}          # Get team details
POST   /api/v1/teams               # Create new team
PATCH  /api/v1/teams/{id}          # Update team
```

For detailed request/response schemas, use Swagger UI at `/swagger-ui/index.html`

---

##  Running Tests

### Run All Tests

```powershell
# Using Maven wrapper
.\mvnw test

# Using Maven (if installed globally)
mvn test
```

### Run Specific Test Class

```powershell
.\mvnw test -Dtest=UserServiceTest
```

### Run Tests with Coverage

```powershell
.\mvnw test jacoco:report

# Coverage report: target/site/jacoco/index.html
```

### Test Types

The project includes:

- **Unit Tests**: Test individual methods in isolation (Mockito)
- **Integration Tests**: Test database interactions (Testcontainers)
- **Controller Tests**: Test REST endpoints (MockMvc)
- **End-to-End Tests**: Full application flow tests (@SpringBootTest)

### Example Test Structure

```java
@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        // Given
        CreateUserRequest request = new CreateUserRequest(...);

        // When
        UserResponse response = userService.createUser(request);

        // Then
        assertEquals("john@crm.com", response.getEmail());
    }
}
```

---

##  Docker & Deployment

### Development with Docker

The `docker-compose.yml` file provides MySQL for local development:

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: crm-mysql
    environment:
      MYSQL_DATABASE: crm_dev
      MYSQL_USER: adham
      MYSQL_PASSWORD: adham123
    ports:
      - "3309:3306"
    volumes:
      - crm_mysql_data:/var/lib/mysql
```

### Docker Commands

```powershell
# Start database
docker-compose up -d

# View logs
docker-compose logs -f mysql

# Stop database
docker-compose down

# Remove all data
docker-compose down -v

# Rebuild services
docker-compose up -d --build
```

### Production Deployment

For production:

1. **Create Dockerfile** (example):
   ```dockerfile
   FROM openjdk:17-slim
   COPY target/crm-backend-0.0.1-SNAPSHOT.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **Use environment variables** for all secrets:
   ```bash
   docker run -e DB_URL=... -e DB_USERNAME=... -e JWT_SECRET_KEY=... crm-backend
   ```

3. **Never commit secrets** to Git - use secret management systems:
   - AWS Secrets Manager
   - Azure Key Vault
   - HashiCorp Vault
   - Kubernetes Secrets

4. **Security checklist**:
   - [ ] Use strong JWT secret (256+ bits)
   - [ ] Enable HTTPS/TLS in production
   - [ ] Configure CORS properly
   - [ ] Set appropriate log levels
   - [ ] Use database connection pooling
   - [ ] Enable audit logging

---

##  Development Workflow

### Project Setup for Development

1. **Fork and Clone**
   ```powershell
   git clone https://github.com/yourusername/crm-backend.git
   cd crm-backend
   ```

2. **Create Feature Branch**
   ```powershell
   git checkout -b feature/my-new-feature
   ```

3. **Make Changes**
   - Follow the existing code structure
   - Keep modules separated
   - Write tests for new features
   - Follow Java conventions and style

4. **Build & Test Locally**
   ```powershell
   .\mvnw clean test
   .\mvnw spring-boot:run
   ```

5. **Commit and Push**
   ```powershell
   git commit -m "Add: my new feature"
   git push origin feature/my-new-feature
   ```

6. **Create Pull Request**
   - Write clear description
   - Include screenshots if UI changes
   - Link related issues

### Code Style

- **Language**: Java 17+
- **Build Tool**: Maven 3.6+
- **Formatting**: Use IDE default formatting
- **Naming**:
   - Classes: `PascalCase`
   - Methods/Variables: `camelCase`
   - Constants: `UPPER_SNAKE_CASE`
   - Database Tables: `snake_case`
   - Database Columns: `snake_case`

### Useful Maven Commands

```powershell
# Clean build
.\mvnw clean build

# Build without tests
.\mvnw -DskipTests clean package

# Run application
.\mvnw spring-boot:run

# Run tests
.\mvnw test

# Create dependency tree
.\mvnw dependency:tree

# Check for dependency conflicts
.\mvnw dependency:analyze
```

### IDE Setup (IntelliJ IDEA)

1. **Install Plugins**:
   - Lombok (for @Data, @Builder, etc.)
   - MapStruct Support

2. **Enable Annotation Processors**:
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"

3. **Code Style**:
   - Settings → Editor → Code Style → Scheme: Project

---

##  Architecture Highlights

### Design Patterns Used

| Pattern | Usage | Example |
|---------|-------|---------|
| **Singleton** | Spring beans | `@Service`, `@Repository`, `@Component` |
| **Builder** | Object construction | Lombok's `@Builder` |
| **Mapper** | DTO ↔ Entity conversion | MapStruct mappers |
| **Repository** | Data access abstraction | Spring Data JPA repositories |
| **Strategy** | Different auth strategies | JWT vs OAuth (extensible) |
| **Factory** | Create complex objects | Spring bean factories |
| **Observer** | Domain events | Event publishing (future) |

### Security Architecture

```
HTTP Request
    ↓
[JWT Filter] - Validates Bearer token
    ↓
[Spring Security Context] - Sets authentication
    ↓
[Authorization Checks] - @PreAuthorize annotations
    ↓
[Service Layer] - Row-level access control
    ↓
[Database] - Only authorized data returned
```

### Error Handling

All endpoints return consistent error responses:

```json
{
  "timestamp": "2026-08-04T10:30:15Z",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with Id: 999",
  "path": "/api/v1/customers/999"
}
```

---

##  Key Files Reference

| File | Purpose |
|------|---------|
| `CrmBackendApplication.java` | Spring Boot entry point |
| `pom.xml` | Maven dependencies and build configuration |
| `docker-compose.yml` | Local MySQL database setup |
| `application.yml` | Main Spring configuration |
| `application-dev.yml` | Development configuration |
| `application-prod.yml` | Production configuration |
| `docs/architecture.md` | Detailed architecture documentation |
| `SecurityConfig.java` | Spring Security and JWT configuration |
| `OpenApiConfig.java` | Swagger/OpenAPI configuration |

---

##  Troubleshooting

### Problem: Database Connection Failed

**Error**: `com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`

**Solution**:
```powershell
# Ensure Docker container is running
docker ps

# If not running, start it
docker-compose up -d

# Check logs
docker-compose logs mysql
```

### Problem: Port Already in Use

**Error**: `Bind exception: Address already in use: bind`

**Solution**:
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <PID> /F

# Or use different port
$env:SERVER_PORT = 8081
.\mvnw spring-boot:run
```

### Problem: JWT Secret Invalid

**Error**: `io.jsonwebtoken.security.InvalidKeyException`

**Solution**:
- Ensure `JWT_SECRET_KEY` is a valid Base64-encoded string
- Key must be at least 256 bits (32 bytes)

### Problem: Tests Failing

**Solution**:
```powershell
# Clear Maven cache
.\mvnw clean

# Rebuild
.\mvnw clean package

# Run tests with debug output
.\mvnw test -X
```

### Problem: Swagger UI Not Loading

**Error**: Swagger UI shows blank page

**Solution**:
1. Check if application started successfully
2. Verify endpoint: `http://localhost:8080/actuator/health`
3. Clear browser cache and reload
4. Check browser console for errors

---

##  Additional Resources

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **JWT Guide**: https://jwt.io/
- **MapStruct**: https://mapstruct.org/
- **Lombok**: https://projectlombok.org/
- **Springdoc OpenAPI**: https://springdoc.org/

---

##  Contact & Support

**Project Author**: Adham Mohamed Hassan Abdel Fadil

**For Questions or Issues**:
1. Check existing GitHub issues
2. Review architecture documentation in `docs/architecture.md`
3. Consult API documentation at `/swagger-ui/index.html`
4. Open a new issue with detailed description

---

##  License

[Specify your license here - MIT, Apache 2.0, etc.]

---

##  Future Enhancements

Planned features for future versions:

- [ ] Dashboard with sales metrics
- [ ] Deal/Pipeline management
- [ ] Interaction and activity tracking
- [ ] Follow-up reminders
- [ ] Audit logging
- [ ] File attachments
- [ ] Email notifications
- [ ] Advanced reporting
- [ ] API versioning
- [ ] Rate limiting

---

## ✅ Checklist for New Developers

Before committing code, ensure:

- [ ] Code follows project structure and conventions
- [ ] All tests pass: `.\mvnw test`
- [ ] No compiler warnings
- [ ] DTOs are used (entities not exposed in API)
- [ ] Proper exception handling
- [ ] Authorization checks in place
- [ ] API documented in Swagger
- [ ] Database migrations handled
- [ ] No secrets hardcoded
- [ ] PR description explains changes

---

**Happy coding! **
