# Employee REST API

  A Spring Boot RESTful API for managing employees with full CRUD operations, role-based security, validation, exception handling, Swagger documentation, comprehensive integration testing, and deployment on AWS.

---

##  Features

  * Full CRUD operations for employee management
  * Role-based authorization using Spring Security
  * DTO mapping layer
  * Input validation using Bean Validation
  * Global exception handling
  * Swagger / OpenAPI documentation
  * H2 database integration
  * Service layer testing
  * Controller integration testing using MockMvc
  * Security authorization testing
  * Deployment on AWS Elastic Beanstalk
  * Amazon RDS database integration

---

## Tech Stack
  
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
  * Amazon Web Services (EC2, Elastic Beanstalk, RDS, Systems Manager Parameter Store, IAM)

---

##  API Endpoints Documentation
  - @GET("/api/employees") =========================> Get all employees
  - @GET("/api/employees/{employeeId}") ========> Get one employee by id
  - @PUT("/api/employees/{employeeId}") ========> Update data for one employee by id
  - @POST("/api/employees") ========================> Adding new employee to database
  - @DELETE("/api/employees/{employeeId}") =========> Deleting employee by id
     
---

### Roles

  - EMPLOYEE ======> Read-only data ( GET method )
  - MANAGER ======> Create & Update employee ( POST & PUT  method)
  - ADMIN =========> Full access

---

##  Testing

Testing covering:
  - CRUD operations
  - Validation rules
  - Exception handling
  - Security authorization
  - Controller endpoint testing
  - Database validation
 
---

## Live Demo
  Application deployed on AWS Elastic Beanstalk:
  -- http://employees-restapi.us-east-1.elasticbeanstalk.com/ --

  
