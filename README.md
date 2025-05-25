# Task Management System 

This is a simple Task Management REST API built with **Spring Boot**. It allows users to create, update,
delete, and list tasks with filtering options. 

---

## Features

- Create, update, delete, list tasks
- Filter tasks by **status**, **priority**, **due date** 
- **Concurrency-safe in-memory storage**
- **Java 17+ features:** Streams, Optionals, Enums
- **Robust validation & error handling**
- **Unit tests with JUnit 5 + Mockito**
- **Swagger UI API documentation**
- **Future extensibility:** Users & Project grouping

---

## Getting Started

## Tech Stack

- Java 17
- Spring Boot 3.5
- Maven
- JUnit + Mockito
- Lombok
- Swagger

---

## How to Run

### 1. Clone the repo

```bash
git clone https://github.com/thewalkersingh/taskmanager.git
cd taskmanager
```

### **Run the REST API**

```bash
./mvnw spring-boot:run 
```

#### Example: Create Task using `curl`

```bash
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{"title":"Demo Task","priority":"MEDIUM"}'
```

#### Example: Filter by Priority

```bash
curl "http://localhost:8080/api/tasks?priority=HIGH"
```

#### Example: Filter by Due Date Range

```bash
curl "http://localhost:8080/api/tasks?from=2025-05-25&to=2025-06-01"
```

#### Example: Sort by Due Date

```bash
curl "http://localhost:8080/api/tasks?sortBy=dueDate"
```

---

## Swagger API Docs

Once the app is running:

http://localhost:8080/swagger-ui/index.html  
http://localhost:8080/v3/api-docs
---

## Testing

Run unit tests with:

```bash
./mvnw test
```

Includes:

- REST controller tests
- Service layer tests
- Validation & Exception Handling Tests

---

## Project Structure

```
com.thewa.taskmanager
|-- controller       # REST controllers
|-- dto              # Data Transfer Objects
|-- exception        # Custom exceptions & handler
|-- mapper           # Entity-DTO mappers
|-- model            # Domain models (Task, enums)
|-- repository       # In-memory repository
|-- service          # Business logic
|__ TaskManagerApplication.java
```

## Author

Crafted with **Java & Spring Boot** by **Diwakar Singh** 