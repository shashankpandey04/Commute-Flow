# CommuteFlow 🚐

> A backend platform for corporate employee transportation, fleet management, trip scheduling, bookings, and ride tracking.

CommuteFlow is a production-oriented **employee transportation management backend** built with **Java and Spring Boot**.

The platform models the complete lifecycle of corporate transportation — from employee registration and fleet management to route planning, trip scheduling, seat booking, and real-time ride events.

The project is designed as a **modular monolith**, with a strong focus on clean architecture, REST APIs, database design, authentication, authorization, transactional business logic, and backend engineering practices.

---

## 🚀 Overview

Corporate transportation systems involve significantly more than simply assigning a vehicle to employees.

CommuteFlow manages relationships between:

```text
Organization
     │
     ├── Employees
     ├── Drivers
     ├── Vehicles
     ├── Pickup Locations
     │
     └── Routes
           │
           └── Route Stops
                 │
                 └── Trips
                       │
                       └── Bookings
                             │
                             └── Ride Events
```

The backend provides APIs for managing these entities while enforcing business constraints such as:

* Employee-trip booking uniqueness
* Vehicle capacity limits
* Vehicle availability
* Driver availability
* Trip lifecycle management
* Organization ownership
* Route-stop ordering
* Booking lifecycle
* Role-based access control

---

# ✨ Key Features

## 🔐 Authentication & Authorization

CommuteFlow uses stateless JWT-based authentication.

Features include:

* User registration
* Secure password hashing using BCrypt
* JWT access tokens
* Token expiration
* Authentication middleware
* Role-based authorization
* Current-user endpoint
* Controlled privileged-role assignment

Supported roles:

```text
ADMIN
OPERATOR
DRIVER
EMPLOYEE
```

Public registration creates an `EMPLOYEE` account by default.

Privileged roles are assigned through controlled administrative operations rather than allowing users to select their own role during registration.

---

## 🏢 Organization Management

Organizations act as the top-level tenant/business entity.

An organization can manage:

* Employees
* Drivers
* Vehicles
* Pickup locations
* Routes

Example:

```text
MoveInSync Corp
│
├── Employees
├── Drivers
├── Vehicles
├── Pickup Locations
└── Routes
```

---

## 👨‍💻 Employee Management

Employees belong to an organization and can optionally have a default pickup location.

Employee information includes:

* Employee code
* Name
* Phone number
* Organization
* User account
* Pickup location

The system prevents duplicate employee codes within the same organization.

---

## 👨‍✈️ Driver Management

Drivers are associated with user accounts and organizations.

Driver lifecycle:

```text
AVAILABLE
    ↓
ASSIGNED
    ↓
AVAILABLE

OFF_DUTY
INACTIVE
```

Driver information includes:

* User account
* Organization
* Driving license number
* Current availability status

The system prevents:

* Duplicate license numbers
* One user being registered as multiple drivers

---

## 🚐 Fleet & Vehicle Management

Vehicles belong to organizations and contain:

* Registration number
* Model
* Passenger capacity
* Availability status

Vehicle lifecycle:

```text
AVAILABLE
    ↓
ASSIGNED
    ↓
AVAILABLE

MAINTENANCE
INACTIVE
```

Vehicle allocation can take passenger capacity into account, allowing the system to select an appropriate vehicle for a trip.

---

## 📍 Pickup Location Management

Organizations can define pickup locations containing:

* Name
* Address
* Latitude
* Longitude

Example:

```text
LPU Main Gate
Lovely Professional University, Phagwara, Punjab

31.2559, 75.7033
```

Pickup locations can subsequently be attached to route stops and employee transportation preferences.

---

## 🛣️ Route Management

Routes represent transportation paths between a source and destination.

A route contains:

* Source
* Destination
* Distance
* Estimated duration
* Ordered pickup stops

Example:

```text
Route: Phagwara → Jalandhar

        Source
          │
          ▼
    LPU Main Gate
          │
          ▼
    Model Town Stop
          │
          ▼
    Jalandhar Office
        Destination
```

Route stops maintain an explicit order, allowing transportation operators to define the sequence in which employees are picked up.

---

## 🚍 Trip Management

A trip represents an actual scheduled execution of a route.

A trip combines:

```text
Route
  +
Vehicle
  +
Driver
  +
Scheduled Time
```

Trip lifecycle:

```text
SCHEDULED
    ↓
BOARDING
    ↓
IN_PROGRESS
    ↓
COMPLETED

             ↘ CANCELLED
```

Trips cannot operate without the required route, vehicle, and driver assignments.

---

## 🎫 Booking Management

Employees can book seats on scheduled trips.

Booking rules include:

* One booking per employee per trip
* Capacity validation
* No booking on cancelled trips
* No booking on completed trips
* Booking cancellation tracking
* Booking status lifecycle

Booking lifecycle:

```text
CONFIRMED
    │
    ├── CANCELLED
    ├── COMPLETED
    └── NO_SHOW
```

The booking layer is designed to support transactional capacity management and prevent overbooking under concurrent requests.

---

## 📡 Ride Events

CommuteFlow records events occurring during an employee's ride.

Supported event types:

```text
PICKUP
DROPOFF
NO_SHOW
```

Each event records:

* Trip
* Employee
* Event type
* Event timestamp
* Creation timestamp

Example:

```text
07:55 ── Employee boards vehicle
08:02 ── Employee picked up
08:45 ── Employee dropped off
```

This creates an auditable ride history for transportation operations.

---

# 🧠 Business Rules

CommuteFlow intentionally keeps important business rules inside the backend rather than relying solely on frontend validation.

Examples include:

### Booking

```text
Employee cannot book the same trip twice.
```

### Capacity

```text
Confirmed bookings <= Vehicle capacity
```

### Vehicle

```text
MAINTENANCE / INACTIVE vehicles
cannot be assigned to active trips.
```

### Driver

```text
A driver cannot be assigned to overlapping active trips.
```

### Trip

```text
A trip requires:
    Route
    Vehicle
    Driver
```

### Organization Isolation

Resources belonging to one organization should not be arbitrarily associated with another organization's resources.

---

# 🏗️ Architecture

CommuteFlow follows a **feature-based modular monolith** architecture.

```text
com.commuteflow
│
├── common
│   ├── entity
│   ├── exception
│   └── response
│
├── auth
│   ├── controller
│   ├── dto
│   ├── security
│   └── service
│
├── user
│   ├── entity
│   └── repository
│
├── organization
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── employee
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── driver
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── vehicle
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── location
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── route
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── trip
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── booking
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
└── ride
    ├── controller
    ├── dto
    ├── entity
    ├── repository
    └── service
```

The architecture keeps modules logically separated while avoiding unnecessary microservice complexity.

---

# 🛠️ Technology Stack

| Technology        | Purpose                           |
| ----------------- | --------------------------------- |
| Java 25 LTS       | Backend language                  |
| Spring Boot 4.1   | Application framework             |
| Spring Web MVC    | REST APIs                         |
| Spring Data JPA   | Persistence                       |
| Hibernate         | ORM                               |
| Spring Security   | Authentication & authorization    |
| JJWT              | JWT generation and validation     |
| PostgreSQL        | Relational database               |
| Supabase          | Managed PostgreSQL infrastructure |
| Lombok            | Boilerplate reduction             |
| Maven             | Dependency/build management       |
| JUnit             | Testing                           |
| Mockito           | Unit testing                      |
| OpenAPI / Swagger | API documentation                 |
| Docker            | Containerization                  |
| GitHub Actions    | CI/CD                             |

---

# 🗄️ Database Design

CommuteFlow uses PostgreSQL with UUID-based identifiers.

Core tables:

```text
organizations
users
employees
drivers
vehicles
pickup_locations
routes
route_stops
trips
bookings
ride_events
```

High-level relationships:

```text
organizations
      │
      ├────────────── employees
      │                    │
      │                    └── users
      │
      ├────────────── drivers
      │                    │
      │                    └── users
      │
      ├────────────── vehicles
      │
      ├────────────── pickup_locations
      │                    │
      │                    └── route_stops
      │
      └────────────── routes
                           │
                           └── route_stops
                                  │
                                  ▼
                                trips
                               /     \
                         vehicles    drivers
                              │
                              ▼
                           bookings
                              │
                              ▼
                         ride_events
```

PostgreSQL native enums are used for controlled lifecycle states such as:

```text
user_role
vehicle_status
driver_status
trip_status
booking_status
ride_event_type
```

The application uses Hibernate's named-enum support to map these PostgreSQL enum types correctly.

---

# 🔒 Security

CommuteFlow follows several security principles:

### Password Security

Passwords are never stored as plaintext.

```text
Password
   ↓
BCrypt
   ↓
password_hash
```

### JWT Authentication

Authenticated requests use:

```http
Authorization: Bearer <access-token>
```

### Stateless Security

The backend does not maintain server-side HTTP sessions.

```text
Client
   │
   │ JWT
   ▼
Spring Security
   │
   ▼
Protected API
```

### Role-Based Access

Authorization is based on application roles:

```text
ADMIN
OPERATOR
DRIVER
EMPLOYEE
```

Privileged roles are not exposed through public registration.

---

# 📡 API

Base URL:

```text
http://localhost:8080/api/v1
```

## Authentication

```http
POST /auth/register
POST /auth/login
GET  /auth/me
PATCH /auth/users/{userId}/role
```

## Organizations

```http
POST /organizations
GET  /organizations
GET  /organizations/{id}
```

## Employees

```http
POST /employees
GET  /employees
GET  /employees/{id}
DELETE /employees/{id}
```

## Drivers

```http
POST /drivers
GET  /drivers
GET  /drivers/{id}
```

## Vehicles

```http
POST /vehicles
GET  /vehicles
GET  /vehicles/{id}
```

Example vehicle filtering:

```http
GET /vehicles?organizationId={id}
GET /vehicles?organizationId={id}&available=true
```

## Pickup Locations

```http
POST /pickup-locations
GET  /pickup-locations
GET  /pickup-locations/{id}
```

## Routes

```http
POST /routes
GET  /routes
GET  /routes/{id}
POST /routes/{id}/stops
```

## Trips

```http
POST /trips
GET  /trips
GET  /trips/{id}
PATCH /trips/{id}/status
```

## Bookings

```http
POST /bookings
GET  /bookings
GET  /bookings/{id}
PATCH /bookings/{id}/cancel
```

## Ride Events

```http
POST /ride-events
GET  /ride-events
GET  /ride-events/trip/{tripId}
```

> The API surface is intentionally organized around domain resources rather than generic CRUD endpoints.

---

# 📦 Project Setup

## Prerequisites

Install:

* Java 25 LTS
* Git
* PostgreSQL or a Supabase PostgreSQL project

Maven does not need to be installed globally because the project uses the Maven Wrapper.

Verify Java:

```bash
java -version
```

Expected:

```text
java version "25..."
```

---

# ⚙️ Configuration

Create a local configuration file:

```text
src/main/resources/application-local.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>:<port>/<database>?sslmode=require
    username: <username>
    password: <password>
    driver-class-name: org.postgresql.Driver

    hikari:
      read-only: false

  jpa:
    hibernate:
      ddl-auto: none
    open-in-view: false

server:
  port: 8080

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration-ms: 3600000
```

Never commit real database credentials or JWT secrets.

Recommended `.gitignore` entries:

```gitignore
src/main/resources/application-local.yml
.env
```

---

# ▶️ Running the Application

Clone the repository:

```bash
git clone https://github.com/shashankpandey04/Commute-Flow.git
cd Commute-Flow
```

Run tests:

```powershell
.\mvnw.cmd clean test
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Health check:

```http
GET /actuator/health
```

---

# 🧪 Testing

Run the complete test suite:

```powershell
.\mvnw.cmd clean test
```

Testing strategy includes:

* Service unit tests
* Controller tests
* Repository tests
* Validation tests
* Authentication tests
* Authorization tests
* Business-rule tests
* Integration tests for critical workflows

Critical scenarios include:

```text
Register user
Login
JWT validation
Unauthorized access
Role-based authorization
Create organization
Create employee
Create driver
Create vehicle
Create route
Create trip
Create booking
Prevent duplicate booking
Prevent overbooking
Cancel booking
Complete trip
Record ride events
```

---

# 📚 API Documentation

OpenAPI/Swagger documentation is intended to provide an interactive API reference.

Once enabled, it can be accessed through the application's Swagger UI endpoint.

Typical development URL:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger can be used to:

* Explore endpoints
* Inspect request/response schemas
* Authenticate using JWT
* Test APIs interactively

---

# 🐳 Docker

CommuteFlow is designed to be container-friendly.

A production deployment can run the application as:

```text
                ┌───────────────────┐
                │    Client / API   │
                └─────────┬─────────┘
                          │
                          ▼
                ┌───────────────────┐
                │   CommuteFlow     │
                │   Spring Boot     │
                └─────────┬─────────┘
                          │
                          ▼
                ┌───────────────────┐
                │    PostgreSQL     │
                │     / Supabase    │
                └───────────────────┘
```

The application itself remains stateless, allowing multiple application instances to be deployed when required.

---

# 🔄 CI/CD

GitHub Actions can be used to automatically:

```text
Push / Pull Request
        ↓
Checkout
        ↓
Setup Java
        ↓
Maven Build
        ↓
Run Tests
        ↓
Package Application
        ↓
Build Docker Image
```

This keeps the main branch protected against builds that fail compilation or automated tests.

---

# 📈 Scalability Considerations

CommuteFlow is intentionally implemented as a modular monolith rather than immediately splitting the system into microservices.

The architecture leaves room for future scaling.

Potential future components include:

```text
                    CommuteFlow
                        │
          ┌─────────────┼─────────────┐
          │             │             │
       REST API      Redis Cache    Background Jobs
          │
      PostgreSQL
```

If system requirements eventually justify service decomposition, domain boundaries already provide natural extraction candidates:

```text
Auth Service
Fleet Service
Trip Service
Booking Service
Ride Tracking Service
```

The initial architecture avoids introducing distributed-system complexity before it is necessary.

---

# 🧠 Interesting Backend Problems

CommuteFlow is intended to demonstrate practical backend engineering rather than simple CRUD.

Important technical challenges include:

### Concurrent Booking

Two employees may attempt to book the final available seat simultaneously.

The system must ensure:

```text
confirmed_bookings <= vehicle_capacity
```

even under concurrent requests.

---

### Vehicle Allocation

Given a passenger requirement:

```text
Passengers = 18
```

and vehicles:

```text
Mini Bus → 20 seats
Bus      → 30 seats
Coach    → 45 seats
```

the allocation algorithm can select an appropriate available vehicle while avoiding unnecessary capacity.

---

### Driver Scheduling

A driver should not be assigned to overlapping active trips.

Conceptually:

```text
Trip A
09:00 ───────── 10:00

Trip B
09:30 ───────── 11:00

        ❌ OVERLAP
```

---

### State Machines

Trip and booking statuses are treated as controlled state transitions rather than arbitrary strings.

Example:

```text
SCHEDULED
    │
    ▼
BOARDING
    │
    ▼
IN_PROGRESS
    │
    ▼
COMPLETED
```

Invalid transitions should be rejected by the service layer.

---

# 🗂️ Development Philosophy

CommuteFlow follows several principles:

### Modular over Monolithic Chaos

The project is a monolith at deployment level, but modules are separated by business domain.

### Business Logic in the Backend

Critical rules are enforced server-side.

### Database Integrity

Constraints are enforced at both:

```text
Application Layer
       +
Database Layer
```

### Secure by Default

Authentication and authorization are treated as core infrastructure rather than an afterthought.

### Avoid Premature Complexity

The project deliberately avoids introducing technologies such as Kafka, Kubernetes, or multiple microservices unless an actual requirement justifies them.

---

# 🛣️ Development Roadmap

The system is developed incrementally:

```text
[x] Project initialization
[x] PostgreSQL / Supabase integration
[x] Database schema
[x] JPA entity layer
[x] Common API response handling
[x] Global exception handling
[x] User registration
[x] BCrypt password hashing
[x] JWT authentication
[x] Role-based authorization foundation
[x] Organization management
[x] Employee management
[x] Pickup location management
[x] Vehicle management
[x] Driver management

[ ] Route management
[ ] Route stop management
[ ] Trip management
[ ] Booking management
[ ] Ride event tracking
[ ] Booking concurrency protection
[ ] Vehicle allocation logic
[ ] Driver scheduling validation
[ ] OpenAPI documentation
[ ] Comprehensive automated tests
[ ] Docker packaging
[ ] GitHub Actions CI/CD
```

The roadmap represents the intended evolution of the project and does not require the architecture to change as individual modules are completed.

---

# 📊 Example End-to-End Workflow

A typical CommuteFlow workflow looks like:

```text
1. Organization is created
             ↓
2. Employee account is registered
             ↓
3. Employee is associated with organization
             ↓
4. Pickup location is created
             ↓
5. Employee selects pickup location
             ↓
6. Driver is registered
             ↓
7. Vehicle is added to fleet
             ↓
8. Route is created
             ↓
9. Pickup locations become route stops
             ↓
10. Trip is scheduled
             ↓
11. Vehicle + Driver are assigned
             ↓
12. Employee books trip
             ↓
13. Employee boards vehicle
             ↓
14. PICKUP event recorded
             ↓
15. Employee reaches destination
             ↓
16. DROPOFF event recorded
             ↓
17. Trip completed
```

This workflow represents the core business lifecycle modeled by the backend.

---

# 🎯 Project Goals

CommuteFlow is built to demonstrate practical knowledge of:

* Java backend development
* Spring Boot
* REST API design
* Spring Security
* JWT authentication
* Role-based authorization
* PostgreSQL
* Hibernate / JPA
* Database relationships
* Transaction management
* Validation
* Exception handling
* Concurrent business operations
* State management
* Fleet management
* Clean modular architecture
* Automated testing
* Containerization
* CI/CD

The goal is not simply to create another CRUD application, but to model the backend problems involved in a real-world employee transportation platform.

---

# 👨‍💻 Author

**Shashank Pandey**

Full Stack / Backend Developer
Java • Spring Boot • Python • FastAPI • Node.js • PostgreSQL • AWS

GitHub:

`https://github.com/shashankpandey04`

---

# 📄 License

This project is intended primarily as a portfolio and learning project.

Add an explicit open-source license here if the repository is intended to be distributed or reused publicly.
