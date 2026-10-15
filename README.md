# SkillHub

It is a multi-tenant education  management system built with Spring Boot that provides secure management of tenants, students, instructors, courses, and enrollments through RESTful APIs. It features JWT-based authentication, role-based authorization, PostgreSQL integration, input validation, pagination, and centralized exception handling.

Live API: https://skillhub-yd1u.onrender.com/skillhub

Swagger UI: https://skillhub-yd1u.onrender.com/skillhub/swagger-ui/index.html

GitHub: https://github.com/amitesh-code/SkillHub

## 🚀 Features

- Manage tenants, students, instructors, courses, and enrollments
- Create and manage user accounts
- Assign instructors to courses
- Enroll students in courses
- Complete or cancel enrollments
- JWT-based authentication
- Role-based access for `SUPER_ADMIN`, `TENANT_ADMIN`, `INSTRUCTOR`, and `STUDENT`
- DTOs, request validation, and global exception handling
- Pagination for selected list endpoints
- Automated tests for services, repositories, APIs, and security

## 🛠️ Technologies Used

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT (JJWT)
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- MockMvc
- Swagger / OpenAPI
- Docker
- Render

## 🏗️ Application Architecture

The application follows a layered architecture:

```
Client (Swagger UI / Postman)
          ↓
    Spring Security
          ↓
      Controller
          ↓
       Service
          ↓
      Repository
          ↓
      PostgreSQL
```

### Controller

Handles HTTP requests and returns API responses.

### Service

Contains the application logic and uses service interfaces with implementations.

### Repository

Uses Spring Data JPA to interact with the database.

### Model

Contains the entities used to represent tenants, users, students, instructors, courses, and enrollments.

### DTO

Defines the request and response objects used by the APIs.

### Security

Handles JWT authentication and role-based authorization.

## 📁 Project Structure

The project is organized into packages for the main application layers:

```
SkillHub
├── Configuration
├── Controllers
├── DTO
├── Exception
├── Model
├── Repo
├── Security
├── Service
├── src/main/resources
├── src/test
├── Dockerfile
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## 🗄️ Database

The application uses **PostgreSQL** to store application data. Spring Data JPA and Hibernate handle database operations and object-relational mapping.

The main entities are:

- `Tenant`
- `Users`
- `Student`
- `Instructor`
- `Course`
- `Enrollment`

Student and instructor profiles are linked to user accounts. Courses can have multiple instructors, and students can have multiple enrollments.

Database credentials and JWT secrets should be configured through environment variables and should not be committed to GitHub.

## ▶️ How to Run

### 1. Clone the repository

```
git clone https://github.com/amitesh-code/SkillHub.git
cd SkillHub
```

### 2. Set up PostgreSQL

Create a PostgreSQL database for local development.

Configure the database connection and required secrets using your local application configuration or environment variables. For production, the application uses environment variables for database credentials and JWT configuration.

### 3. Run the application

Windows:

```
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```
./mvnw spring-boot:run
```

To build the project:

```
./mvnw clean package
```

On Windows, use `.\mvnw.cmd` instead of `./mvnw`.

### 4. Open the application

The application uses the context path:

```
/skillhub
```

The local base URL is:

```
http://localhost:8080/skillhub
```

## 🔐 Authentication

SkillHub uses JWT for authentication.

### Login

Send a `POST` request to:

```
/auth/login
```

with a JSON body containing the account email and password:

```
{
  "email": "your-email",
  "password": "your-password"
}
```

The response contains a JWT and user details. Use the token to access protected endpoints by adding this header:

```
Authorization: Bearer <JWT_TOKEN>
```

You can try the endpoints through Swagger UI or Postman.

## 🧪 Testing

The project uses:

- JUnit 5 for tests
- Mockito for unit testing
- MockMvc for API and security testing
- `@DataJpaTest` for repository integration tests with PostgreSQL

Run the tests using the Maven Wrapper.

Windows:

```
.\mvnw.cmd test
```

macOS/Linux:

```
./mvnw test
```

Repository tests that use PostgreSQL require a configured test database.

## 🚀 Deployment

SkillHub is deployed on **Render** using Docker, with PostgreSQL as the database.

The Dockerfile uses a multi-stage build to build the application with a Java 21 JDK image and run it using a Java 21 runtime image.

Production settings, including database credentials and JWT secrets, are provided through environment variables.

## 📚 What I Learned

While building SkillHub, I practiced:

- Structuring a Spring Boot application using layers
- Designing entities and relationships with JPA
- Building REST APIs using Spring MVC
- Using DTOs and Bean Validation
- Implementing global exception handling
- Working with Spring Security and JWT
- Applying role-based authorization
- Writing unit, repository, and API tests
- Working with PostgreSQL
- Building and deploying an application using Docker and Render

## 🔮 Future Improvements

- Add more security and edge-case test.
- Build a frontend for easier interaction with the system
- Strengthen tenant-level data isolation and authorization across all APIs.
- Add audit logs for important account and administrative actions.
- Add reports for enrollments, course activity, and student progress.
- Send email notifications for enrollment and course updates.
- Improve API examples and documentation
- Improve performance with caching and background jobs where useful.

## 👨‍💻 Author

Amitesh

GitHub: [https://github.com/amitesh-code](https://github.com/amitesh-code)
