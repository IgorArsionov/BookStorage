# Book Storage API

Book Storage is a RESTful API for an online bookstore. The project allows you to manage books and categories, register users, log in with a JWT token, manage your shopping cart, and place orders.

The project's goal is to demonstrate the practical use of Spring Boot for developing a secure backend application with a clear structure, role-based access model, and API documentation.

## Features

- New user registration
- User authorization and obtaining a JWT token
- Viewing a list of books and information about a specific book
- Creating, updating, and deleting books for the administrator
- Managing book categories
- Adding books to the cart
- Viewing and updating the cart
- Creating orders
- Viewing user orders
- Managing order statuses for the administrator
- Interactive API documentation via Swagger UI

## Technologies used

- Java 17
- Spring Boot
- Spring Web
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- MySQL
- Liquibase
- MapStruct
- Lombok
- Swagger/OpenAPI
- Maven
- JUnit 5
- Mockito
- Testcontainers
- Docker Compose

# Security

The application implements JWT-based authentication.

After successful login, the user receives a token.

The project includes the following roles:
USER — can manage the catalog, cart, and orders;
ADMIN — can also manage books, categories, and order statuses.
Swagger UI and the authorization endpoint are available without a JWT token.


## Main API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/registration` | Register a new user |
| `POST` | `/auth/login` | Authenticate and receive a JWT token |

### Books

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/books` | All users | Get a list of books |
| `GET` | `/books/{id}` | All users | Get a book by ID |
| `POST` | `/books` | `ADMIN` | Create a new book |
| `PUT` | `/books/{id}` | `ADMIN` | Update a book |
| `DELETE` | `/books/{id}` | `ADMIN` | Delete a book |

### Categories

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/categories` | All users | Get a list of categories |
| `GET` | `/categories/{id}` | All users | Get a category by ID |
| `POST` | `/categories` | `ADMIN` | Create a new category |
| `PUT` | `/categories/{id}` | `ADMIN` | Update a category |
| `DELETE` | `/categories/{id}` | `ADMIN` | Delete a category |

### Running a Project
What you need to install
- JDK 17 or higher
- Docker and Docker Compose
- Maven

Create `.env` file in the project root and fill in the following variables:

```env
MYSQLDB_USER=your_mysql_username
MYSQLDB_PASSWORD=your_mysql_password
MYSQLDB_DATABASE=your_database_name
MYSQLDB_ROOT_PASSWORD=root_password
MYSQLDB_LOCAL_PORT=local_port
MYSQLDB_DOCKER_PORT=docker_port

SPRING_LOCAL_PORT=spring_local_port
SPRING_DOCKER_PORT=spring_docker_port
DEBUG_PORT=debug_port
```

## Challenges and Solutions

During development, I faced several practical backend challenges and addressed them with the following solutions:

### Role-Based Access Control

**Challenge:** Restricting access to API endpoints based on user roles.

**Solution:** Implemented Spring Security with `@PreAuthorize` annotations and role-based authorization using `USER` and `ADMIN` roles.

### Stateless Authentication

**Challenge:** Building secure authentication without storing user sessions on the server.

**Solution:** Implemented JWT-based authentication. After a successful login, the client receives a token and sends it with every protected request using the `Authorization: Bearer <token>` header.

### API Documentation

**Challenge:** Making the API easy to explore and test.

**Solution:** Integrated Swagger/OpenAPI to automatically generate interactive API documentation with available endpoints, request parameters, and response formats.

### Database Schema Management

**Challenge:** Managing and tracking database structure changes consistently across environments.

**Solution:** Used Liquibase for version-controlled database migrations, ensuring that schema changes are applied in a predictable and repeatable way.

## Author

**Ihor Arsonov**  
Java Backend Developer