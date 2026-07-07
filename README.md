# Hospital Microservice System

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-2E7D32?style=for-the-badge&logo=springsecurity)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Server%20Side%20UI-005F0F?style=for-the-badge&logo=thymeleaf)
![Oracle](https://img.shields.io/badge/Oracle%20Database-XE-F80000?style=for-the-badge&logo=oracle)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?style=for-the-badge&logo=swagger)

**A production-minded Hospital Management Platform built with Spring Boot, Spring Security, JWT, JPA, Thymeleaf, MapStruct, OpenAPI, and a microservice-ready architecture.**

</div>

‫> 🚧 **Project Status: Active Refactoring**
>
> This HIS is currently a **modular monolith with a strong CoreService**.
> I'm actively extracting microservices – **Auth Service is in progress**.
> Target architecture: **8 isolated domain services**
> `Auth · Core · Clinical · Inventory · Billing · Lab · Admin · Notification`
>
> ✅ CoreService – Done  
> 🔄 Auth Service / JWT – In Progress  
> 📋 Clinical, Billing, Inventory, Lab, Admin, Notification – Roadmap
>
> The codebase is microservice-ready: clean domain boundaries, DTO/MapStruct layer, JWT security – designed for easy service extraction.

---

## Overview

**Hospital Microservice System** is a full-featured healthcare management platform designed for hospitals, clinics, and medical centers. The project focuses on real-world hospital workflows such as authentication, role-based access control, patient management, doctor management, nurse management, appointment scheduling, room allocation, shift planning, and dashboards for different user roles.

The system is built around a **microservice-oriented domain design** and currently provides a strong `CoreService` module that contains the main business capabilities. The codebase is structured to support future extraction into independent services such as Authentication Service, Patient Service, Doctor Service, Appointment Service, Department Service, and API Gateway.

This repository is suitable for:

- Spring Boot portfolio projects
- Java backend developer interviews
- Academic software engineering presentations
- Healthcare domain demonstrations
- JWT/Spring Security practice
- Server-side rendered dashboard development with Thymeleaf
- Future microservice expansion

---

## Key Features

### Authentication & Authorization

- Secure login flow with Spring Security
- JWT access token and refresh token support
- Role-based access control
- Custom `UserDetailsService`
- Custom `SecurityUser` adapter
- Password hashing with BCrypt
- Account status fields such as enabled, locked, expired, failed attempts, and password expiration

### User, Role & Permission Management

- User registration and profile management
- Role assignment and removal
- Permission model for fine-grained access control
- Role and permission CRUD APIs
- User search, status management, bulk operations, and password management

### Doctor Management

- Doctor profile creation and update
- Doctor specialization and sub-specialization management
- Department assignment
- Active/inactive doctor handling
- Doctor search and filtering
- Doctor dashboard with appointments, patients, schedules, and available slots

### Patient Management

- Patient registration and profile completion
- Duplicate national ID and phone number checks
- Patient search and filtering
- Patient status management
- Room assignment and unassignment
- Patient appointment conflict checks

### Appointment Scheduling

- Appointment creation and update
- Patient self-booking flow
- Doctor availability calculation
- Available time slot generation
- Appointment cancellation, check-in, completion, and rescheduling
- Appointment filtering by doctor, patient, department, date, status, and time range

### Department, Room, Nurse & Shift Management

- Department CRUD and activation/deactivation
- Head doctor and head nurse assignment
- Room creation, capacity tracking, occupation status, and patient assignment
- Nurse profile management
- Nurse department and shift assignment
- Shift management and active/inactive states

### UI Dashboards with Thymeleaf

- Public landing page
- Admin dashboard
- Doctor dashboard
- Patient dashboard
- Nurse dashboard
- Appointment booking pages
- Doctor, patient, nurse, department, room, and shift pages
- Server-side rendered views integrated with Spring MVC

### API Documentation

- OpenAPI 3 integration
- Swagger UI for interactive API testing
- API documentation grouped around hospital domains

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Web | Spring MVC, REST APIs, Thymeleaf |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, Hibernate |
| Database | Oracle Database XE by default, H2 available for development/testing adaptation |
| Mapping | MapStruct |
| Validation | Jakarta Bean Validation |
| Documentation | SpringDoc OpenAPI, Swagger UI |
| Monitoring | Spring Boot Actuator, Micrometer Prometheus Registry |
| Build Tool | Maven Wrapper |
| UI | Thymeleaf templates, HTML, CSS, JavaScript |

---

## Project Structure

```text
hospital-microservice-system/
├── CoreService/
│   ├── src/main/java/hospital/coreservice/
│   │   ├── config/              # Application configuration classes
│   │   ├── controller/          # REST and MVC controllers
│   │   ├── dto/                 # Request and response DTOs
│   │   ├── exception/           # Domain-specific exceptions and global handler
│   │   ├── mapper/              # MapStruct mappers
│   │   ├── model/               # JPA entities and enums
│   │   ├── repository/          # Spring Data repositories
│   │   ├── runner/              # Startup data initialization
│   │   ├── security/            # JWT and Spring Security components
│   │   └── service/             # Service interfaces and implementations
│   ├── src/main/resources/
│   │   ├── static/              # Static assets
│   │   ├── templates/           # Thymeleaf views
│   │   ├── application.yaml     # Main application configuration
│   │   └── application-docker.yml
│   └── pom.xml
└── README.md
```

---

## Domain Model Highlights

The platform includes multiple connected healthcare aggregates:

- `User`
- `Role`
- `Permission`
- `Patient`
- `Doctor`
- `DoctorSchedule`
- `Appointment`
- `Department`
- `Nurse`
- `Room`
- `Shift`
- `AuditLog`
- `RefreshTokens`
- `BlackListedTokens`
- `PasswordToken`

Important relationships include:

- One user can have multiple roles.
- A doctor is connected to a user account.
- A patient is connected to a user account.
- A doctor belongs to a department.
- A patient can be assigned to a room.
- Appointments connect patient, doctor, department, date, time, status, and type.
- Doctor schedules define available working days and time slots.

---

## Main API Areas

### Authentication

```http
POST /api/v1/auth/login
POST /api/v1/auth/refresh
GET  /api/v1/auth/me
```

### Users

```http
POST  /api/v1/users/register
GET   /api/v1/users/by-username
GET   /api/v1/users/{id}/profile
PUT   /api/v1/users/{id}
PATCH /api/v1/users/{id}/change-password
PATCH /api/v1/users/{id}/roles/{roleId}/add
```

### Doctors

```http
POST  /api/v1/doctor
GET   /api/v1/doctor
GET   /api/v1/doctor/{id}
GET   /api/v1/doctor/by-department-id
GET   /api/v1/doctor/active
PATCH /api/v1/doctor/deactivate/{id}
PATCH /api/v1/doctor/activate/{id}
```

### Patients

```http
POST  /api/v1/patient
GET   /api/v1/patient/{id}
GET   /api/v1/patient/by-national-id
GET   /api/v1/patient/search
PATCH /api/v1/patient/assign/room/{patientId}
POST  /api/v1/patient/complete-registration
```

### Appointments

```http
POST  /api/v1/appointments
GET   /api/v1/appointments/{id}
GET   /api/v1/appointments/doctor/{doctorId}
GET   /api/v1/appointments/patient/{patientId}
GET   /api/v1/appointments/doctor/available
PATCH /api/v1/appointments/{id}/cancel
PATCH /api/v1/appointments/{id}/check-in
PATCH /api/v1/appointments/{id}/complete
PUT   /api/v1/appointments/{id}/reschedule
```

### Departments, Rooms, Nurses, Shifts, Roles, Permissions

The project also includes complete APIs for:

- Department management
- Room management
- Nurse management
- Shift management
- Role management
- Permission management
- Audit log reporting

---

## Thymeleaf Pages

The application includes server-rendered pages for both public and authenticated workflows.

| Page | Route |
|---|---|
| Home | `/` or `/home` |
| Login | `/login` |
| Admin Dashboard | `/dashboard` |
| Doctors | `/doctors` |
| Doctor Dashboard | `/doctor/dashboard` |
| Doctor Appointments | `/doctor/appointments` |
| Doctor Patients | `/doctor/patients` |
| Doctor Schedule | `/doctor/schedule` |
| Patient Dashboard | `/patient_dashboard` |
| Patient Booking | `/patient/book` |
| Departments | `/departments` |
| Appointments | `/appointments` |
| Rooms | `/rooms` |
| Nurses | `/nurses` |
| Shifts | `/shifts` |

---

## Getting Started

### Prerequisites

Make sure the following tools are installed:

- Java 17
- Maven or Maven Wrapper
- Oracle Database XE, or an adapted development database profile
- Git

Check Java version:

```bash
java -version
```

Expected Java version:

```text
17.x
```

---

### Clone the Repository

```bash
git clone https://github.com/MobinaRahi/hospital-microservice-system.git
cd hospital-microservice-system/CoreService
```

---

### Configure Database

Default database configuration is located in:

```text
CoreService/src/main/resources/application.yaml
```

Default Oracle configuration:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    driver-class-name: oracle.jdbc.OracleDriver
    username: java
    password: java123
```

For production-style execution, prefer environment variables and avoid hardcoded secrets.

Example:

```bash
export SPRING_DATASOURCE_URL=jdbc:oracle:thin:@localhost:1521:XE
export SPRING_DATASOURCE_USERNAME=java
export SPRING_DATASOURCE_PASSWORD=java123
export JWT_SECRET=change-this-secret-before-production
```

---

### Run the Application

Linux/macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Actuator health:

```text
http://localhost:8080/actuator/health
```

---

## Sample Data

The project includes a startup initializer that creates sample roles, users, departments, doctors, nurses, patients, rooms, shifts, schedules, and appointments.

Common demo accounts:

| Username | Password | Role |
|---|---|---|
| `admin` | `Admin@123` | Admin / Super Admin |
| `dr.ali` | `Doctor@123` | Doctor |
| `dr.maryam` | `Doctor@123` | Doctor |
| `nurse.fatemeh` | `Nurse@123` | Nurse |
| `patient.reza` | `Patient@123` | Patient |
| `patient.zahra` | `Patient@123` | Patient |

---

## Security Design

The security layer includes:

- `SecurityConfig`
- `JwtAuthenticationFilter`
- `JwtTokenProvider`
- `CustomUserDetailsService`
- `SecurityUser`
- Custom authentication entry point
- Custom access denied handler
- Custom logout handling
- Role-based route protection

Access tokens and refresh tokens are generated through the authentication API. Passwords are encoded using BCrypt.

---

## Architecture Roadmap

The project is designed for continuous evolution toward a distributed healthcare platform. Planned architecture improvements include:

- API Gateway module
- Authentication Service
- Doctor Service
- Patient Service
- Appointment Service
- Department and Room Service
- Service discovery
- Centralized configuration
- Event-driven communication with RabbitMQ or Kafka
- Docker Compose environment
- Kubernetes deployment manifests
- Observability with Prometheus and Grafana
- Database migration with Flyway or Liquibase
- Testcontainers-based integration tests

---

## Developer Notes

### Recommended Local Workflow

```bash
cd CoreService
./mvnw clean compile
./mvnw test
./mvnw spring-boot:run
```

### Suggested Production Improvements

- Move all secrets to environment variables.
- Add database migrations with Flyway or Liquibase.
- Add Docker and Docker Compose configuration.
- Add integration tests for authentication and appointment booking.
- Add database-level constraints for critical scheduling conflicts.
- Add CI/CD workflow with GitHub Actions.
- Add service-level logs and structured tracing.

---

## Portfolio Highlights

This project demonstrates practical experience with:

- Designing a real healthcare domain model
- Building layered Spring Boot applications
- Implementing JWT authentication
- Applying DTO and Mapper patterns
- Managing complex JPA relationships
- Creating role-based dashboards
- Rendering server-side UI with Thymeleaf
- Building scalable API structures
- Preparing a system for microservice expansion

---

## Keywords

`Spring Boot Hospital Management System`, `Java Healthcare Project`, `Spring Security JWT`, `Hospital Appointment Scheduling`, `Thymeleaf Dashboard`, `Spring Data JPA`, `MapStruct DTO Mapper`, `Oracle Database Spring Boot`, `Microservice Ready Architecture`, `Healthcare Backend System`, `Doctor Patient Appointment System`, `Spring Boot Portfolio Project`.

---

## Author

**Mobina Rahi**

- GitHub: [MobinaRahi](https://github.com/MobinaRahi)
- Project: [hospital-microservice-system](https://github.com/MobinaRahi/hospital-microservice-system)

---

## License

This project is created for educational, portfolio, and professional demonstration purposes. Add a dedicated license file before using it in production or commercial environments.
