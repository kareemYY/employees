# 👨‍💼 Employee REST API

A Spring Boot RESTful API for managing employees with full CRUD operations, role-based security, validation, exception handling, Swagger documentation, and comprehensive integration testing.

---

## 🚀 Features

* Create, Read, Update, Delete (CRUD) operations
* Role-based authorization using Spring Security
* DTO mapping layer
* Input validation using Bean Validation
* Global exception handling
* Swagger / OpenAPI documentation
* H2 database integration
* Service layer testing
* Controller integration testing using MockMvc
* Security authorization testing

---

## 🏗️ Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Spring Security
* H2 Database
* JUnit 5
* MockMvc
* Spring Boot Test
* Swagger / OpenAPI
* Maven

---

## 📦 API Endpoints

| Method | Endpoint            | Description         | Access Role |
| ------ | ------------------- | ------------------- | ----------- |
| GET    | /api/employees      | Get all employees   | EMPLOYEE    |
| GET    | /api/employees/{id} | Get employee by ID  | EMPLOYEE    |
| POST   | /api/employees      | Create new employee | MANAGER     |
| PUT    | /api/employees/{id} | Update employee     | MANAGER     |
| DELETE | /api/employees/{id} | Delete employee     | ADMIN       |

---

## 🔐 Security

This project uses Spring Security with role-based authorization.

### Roles

* EMPLOYEE → Read operations
* MANAGER → Create & Update operations
* ADMIN → Delete operations

---

## 🧪 Testing

The project includes comprehensive integration and service testing covering:

* CRUD operations
* Validation rules
* Exception handling
* Security authorization
* Controller endpoint testing
* Database validation

Tests use:

* Spring Boot Test
* MockMvc
* H2 Database
* JUnit 5

---

## ⚙️ Running the Project

```bash
mvn spring-boot:run
```

Application runs at:

```text
http://localhost:8080
```

---

## 📘 Swagger Documentation

Swagger UI is available at:

```text
http://localhost:8080/docs
```

---

## 🗄️ Database

This project uses H2 Database.

### H2 Console

```text
http://localhost:8080/h2-console
```

### Default Credentials

| Property | Value    |
| -------- | -------- |
| Username | sa       |
| Password | password |

---

## 📂 Project Structure

```text
src/main/java
│
├── controller
├── service
├── employeeDao
├── entity
├── mapper
├── security
├── exception
└── dto
```

---

## 🚀 Future Improvements

* Pagination
* Search API
* Docker Support
* API Response Wrapper
* Audit Fields

---

## 👨‍💻 Author

Kareem Yasser
