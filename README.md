# 📚 Library Management System - User Guide


## 1. Project Overview

The Library Management System is a full-stack web application designed to manage library operations such as user management, book borrowing, and system analytics.

This system follows modern web development practices including:

- RESTful API design

- JWT-based authentication and authorization

- Role-based access control (Admin, Librarian, Member)

- Responsive UI using Bootstrap and jQuery

- Data visualization using charts and tables

---

## 2. Features (User Stories)

The system implements the following key functionalities:

👤 User Management

- Admin can register Librarians and Members

- Role-based access control enforced via JWT

🔐 Authentication

- Secure login system using JWT

- Protected API endpoints based on roles

📚 Book Management

- Librarian can add books

- Librarians/Members can view available books (DataTables/Cards UI)

🔄 Borrowing System

- Members can borrow and return books

- System enforces borrowing rules (limits, duration)

📊 Analytics

- Admin dashboard shows borrowing statistics

- Charts for trends (most borrowed books, totals)

---

## 3. Technology Stack
Backend

- Spring Boot 3

- Spring Security

- Spring Data JPA

- JWT Authentication

Frontend

- HTML, CSS

- Bootstrap

- JavaScript / jQuery

- DataTables

Database

- MySQL

Testing & Quality

- JUnit 5

- Karate (API testing)

- Selenium (UI testing)

- JaCoCo (Code coverage)

Documentation

- Swagger (OpenAPI)

---

## 4. Setup Instructions

### 1. Prerequisites
- Java 17+
- MySQL database
- Maven
- Spring Boot 3.5.10
- Git

### 2. Clone the repository

```bash
git clone https://github.com/ZhangWanshi/library-management-system.git
cd library-management-system
```

### 3. Configure database (application.properties)
Create a database:
```bash
CREATE DATABASE library_db;
```

Update `src/main/resources/application.properties`:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build the project

```bash
mvn clean install
mvn spring-boot:run
```
Application runs at:
```bash
http://localhost:8081
```

### 5. API Documentation
The REST API endpoints are documented to showcase requests, responses, DTO structures, and required authentication headers.
Run the application first, swagger API collection is available with the fowlling link:

```bash
http://localhost:8080/swagger-ui.html
```

---


## 5. Security Implementation

The system uses JWT (JSON Web Token) authentication:

- Users login with credentials

- Server generates JWT token

- Token is included in request headers:

```bash
Authorization: Bearer <token>
```
#### Role-Based Access

| Role      | Permissions                          |
| --------- | ------------------------------------ |
| Admin     | Manage users, rules, view statistics |
| Librarian | Manage books, view borrow records    |
| Member    | Borrow/return books                  |

---


## 6. REST API Design

The API follows REST principles:

#### Proper HTTP methods:

- GET → retrieve data

- POST → create

- PUT → update

- DELETE → remove

#### Status codes:

- 200 OK

- 201 Created

- 400 Bad Request

- 401 Unauthorized

- 404 Not Found

#### Example Endpoints:

| Endpoint               | Method | Description |
| ---------------        | ------ | ----------- |
| /api/auth/login        | POST   | User login  |
| /api/users             | POST   | Create user |
| /api/books             | GET    | List books  |
| /api/borrowing{bookId} | POST   | Borrow book |

---

## 7. Testing & Code Coverage
The project includes a robust testing suite powered by Maven Failsafe, Selenium, Karate, and JaCoCo.
#### 1.Run Unit Tests:

```bash
mvn test
```
#### 2. Run Integration & UI Tests (Selenium/Karate):

```bash
mvn verify
```
#### 3. View Test Coverage:
After running mvn verify, JaCoCo enforces a 70% line coverage threshold. You can view the generated HTML report at:

```bash
target/site/jacoco/index.html
```

---

## 8. Future Improvements

- Pagination & search optimization

- Email notifications for overdue books

- Fine/penalty system

- Deployment (Docker / Cloud)



