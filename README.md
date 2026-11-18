# StaySphere – Multi-Tenant PG Management System

**StaySphere** is a backend-focused, multi-tenant SaaS platform designed to simplify the management and operations of Paying Guest (PG) accommodations. It provides a centralized platform for accommodation providers to manage their organizations, users, roles, and property-related operations while maintaining logical isolation between different organizations.

The application is being developed using Java and Spring Boot, following a layered architecture and RESTful API design principles. Its long-term architecture is designed to support multiple organizations through database-level tenant isolation.

## Key Features

### 1. Company Registration and Management
- Register companies on the StaySphere platform.
- Maintain centralized company information in a platform-level database.
- Generate unique company codes during registration.
- Generate UUID-based tenant identifiers.
- Validate company registration requests using Jakarta Bean Validation.
- Maintain company registration status.

### 2. User Management
- Manage users within an organization.
- Support predefined user roles.
- Identify users using their full name and email address.
- Allow Company Admins to reassign user roles.
- Support user and role management operations.

### 3. Role-Based Access Control (RBAC)
- Implement role-based user management.
- Support predefined roles such as Company Admin, Staff, and Resident.
- Manage role assignments and role-related operations.
- Enforce business rules to ensure that every company retains at least one active Company Admin.

### 4. Multi-Tenant Architecture
StaySphere is designed around a database-per-tenant architecture.

- A centralized platform database stores company registration and tenant metadata.
- Each registered company is intended to have its own isolated MySQL database.
- Tenant identification is designed to be resolved using the authenticated user's identity.
- Tenant-specific database provisioning and dynamic database routing are part of the planned architecture.

## Technology Stack

| Category | Technologies |
|---|---|
| Programming Language | Java |
| Backend Framework | Spring Boot |
| API Development | Spring Web, REST APIs |
| ORM | JPA, Hibernate |
| Database | MySQL |
| Data Access | Spring Data JPA |
| Validation | Jakarta Bean Validation |
| Security | Spring Security (planned integration) |
| Authentication | JWT (planned integration) |
| Build & Dependency Management | Maven / Gradle, depending on project configuration |
| Database Management | DBeaver |
| Version Control | Git |

## Architecture

StaySphere follows a layered backend architecture that separates responsibilities across different application components.

```text
                 Client / API Consumer
                         |
                         v
                 REST Controllers
                         |
                         v
                    Services
                (Business Logic)
                         |
                         v
                   Repositories
                         |
                         v
                  JPA / Hibernate
                         |
                         v
                     MySQL
```

### Multi-Tenant Architecture (Target Design)

```text
                  StaySphere Platform
                          |
             +------------+------------+
             |                         |
             v                         v
       Platform Database         Authentication
       (Company Metadata)        & Tenant Identity
             |                         |
             +------------+------------+
                          |
                          v
                  Tenant Resolution
                          |
                          v
                 Dynamic DB Routing
                          |
             +------------+------------+
             |            |            |
             v            v            v
          Company A    Company B    Company C
           Database     Database     Database
```

The platform database acts as the central registry for registered companies. Each company is intended to have an independent database to ensure tenant-level data isolation.

*Note: The current implementation uses a single MySQL database (`staysphere_db`). Dynamic tenant resolution, individual database provisioning, and database routing are planned architectural enhancements.*

## Project Structure

The application follows a modular, layered package structure.

```text
src/
 └── main/
     ├── java/
     │   └── com/
     │       └── staysphere/
     │           ├── tenantprovisioning/
     │           │   ├── controller/
     │           │   ├── service/
     │           │   ├── repository/
     │           │   ├── entity/
     │           │   ├── dto/
     │           │   └── enums/
     │           │
     │           ├── usermanagement/
     │           │   ├── controller/
     │           │   ├── service/
     │           │   ├── repository/
     │           │   ├── entity/
     │           │   ├── dto/
     │           │   └── enums/
     │           │
     │           ├── exception/
     │           │   ├── ApiErrorResponse.java
     │           │   └── GlobalExceptionHandler.java
     │           │
     │           └── StaySphereApplication.java
     │
     └── resources/
         └── application.properties
```

The package structure represents the intended modular organization. Individual packages should be added as their respective modules are implemented.

## API Overview

### Company Registration

| Property | Details |
|---|---|
| Endpoint | `/api/companies/register` |
| HTTP Method | POST |
| Content-Type | application/json |
| Purpose | Register a new company |

**Sample Request**

```json
{
  "companyName": "Sunrise PG Management"
}
```

**Request Validation**
- Company name cannot be blank.
- Company name must satisfy the configured length constraints.
- Company code is generated internally rather than accepted from the client.

The registration workflow creates the company record and generates its associated tenant information.

*The exact response fields and status values are defined by the application's DTOs.*

## Error Handling and Validation

StaySphere uses centralized exception handling to provide consistent API error responses.

Implemented mechanisms include:

- Jakarta Bean Validation for request validation.
- `@ControllerAdvice` for global exception handling.
- `MethodArgumentNotValidException` handling.
- Field-level validation error extraction.
- Structured API error responses using a custom `ApiErrorResponse` DTO.

This avoids duplicating exception-handling logic across controllers and provides consistent error responses to API consumers.

## Database Design

The current development environment uses MySQL.

**Database Configuration**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/staysphere_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Database credentials should be supplied through environment variables rather than committed to version control.

The current database is used for development. Schema migrations and production database configuration will be introduced as the project evolves.

## Getting Started

### Prerequisites

Install the following:

- Java Development Kit (JDK 21, if configured for the project).
- MySQL Server.
- Git.
- IntelliJ IDEA or another Java IDE.
- DBeaver (optional, for database management).

### 1. Clone the Repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd StaySphere
```

### 2. Create the MySQL Database

Open MySQL and execute:

```sql
CREATE DATABASE staysphere_db;
```

### 3. Configure Database Credentials

Set the following environment variables:

```bash
export DB_USERNAME=your_mysql_username
export DB_PASSWORD=your_mysql_password
```

Ensure the Spring Boot datasource configuration references these variables.

### 4. Build the Application

If the project uses Maven:

```bash
./mvnw clean install
```

If the project uses Gradle:

```bash
./gradlew clean build
```

### 5. Run the Application

Maven:

```bash
./mvnw spring-boot:run
```

Gradle:

```bash
./gradlew bootRun
```

The application will start using the configured Spring Boot server port (8080 by default).

### 6. Test the API

You can use Postman or cURL to test the company registration endpoint.

```bash
curl -X POST http://localhost:8080/api/companies/register \
-H "Content-Type: application/json" \
-d '{
  "companyName": "Sunrise PG Management"
}'
```

## Engineering Principles

The project emphasizes the following backend engineering practices:

- **Separation of Concerns:** Controllers, services, and repositories have distinct responsibilities.
- **DTO Pattern:** Request and response objects are separated from persistence entities.
- **Input Validation:** Invalid requests are rejected through declarative validation.
- **Centralized Exception Handling:** Consistent error handling across REST APIs.
- **Database Abstraction:** Spring Data JPA and Hibernate simplify persistence operations.
- **Business Rule Enforcement:** Critical domain invariants are handled within the business layer.
- **Tenant Isolation:** Database-per-tenant architecture is the intended approach for organization-level data isolation.

## Roadmap

The following features represent the planned development direction.

- [ ] JWT-based authentication and authorization.
- [ ] Spring Security integration.
- [ ] Dynamic tenant identification and database routing.
- [ ] Automated database provisioning for new companies.
- [ ] Tenant-specific user management and access control.
- [ ] Property and PG management.
- [ ] Room and bed allocation.
- [ ] Resident onboarding and lifecycle management.
- [ ] Rent and payment management.
- [ ] Maintenance request management.
- [ ] Dashboard and operational reporting.
- [ ] Redis integration for caching.
- [ ] Apache Kafka for asynchronous event processing.
- [ ] Docker containerization and Docker Compose.
- [ ] Kubernetes deployment.
- [ ] Unit and integration testing using JUnit, Mockito, and Testcontainers.
- [ ] API documentation using OpenAPI / Swagger.

## Future Scope

StaySphere aims to evolve into a scalable accommodation management platform that supports multiple PG operators, property administrators, staff members, and residents.

The long-term objective is to combine secure multi-tenancy, modular backend services, asynchronous processing, caching, and containerized deployment into a production-oriented SaaS application.

---

**Project:** StaySphere  
**Category:** Multi-Tenant SaaS | Backend Engineering | Accommodation Management  
**Status:** Under Development
