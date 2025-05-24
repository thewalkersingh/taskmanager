# Task Management System (Spring Boot)

This is a simple Task Management REST API built with **Spring Boot**. It allows users to create, update,
delete, and list tasks with filtering options.
It Supports both **REST API** and **Command-Line Interface (CLI)** via Spring Profiles.

---

## Features

- Create, update, delete, list tasks
- Filter tasks by `status`, `priority`, `due date`
- Sort tasks by `priority`, `due date`, or `status`
- In-memory storage (no database required)
- Dual-mode: CLI or REST API
- Java 8+ features (Streams, Optionals, Enums)
- Robust validation and error handling
- Unit tests with JUnit + Mockito

---

## 🚀 How to Run

### 🔧 1. Clone the repo

```bash
git clone https://github.com/your-username/task-manager.git
cd task-manager
```

### 🖥️ 2. Run in CLI mode

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=cli
```

Or set profile in `application.yml`:

```yaml
spring:
  profiles:
    active: cli
```

#### CLI Menu

```
1. List all tasks
2. Create a task
4. List tasks sorted by priority
5. List tasks sorted by due date
3. Exit
```

---

### 🌐 3. Run in **REST API mode**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=default
```

#### Example: Create Task using `curl`

```bash
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{"title":"My Task","priority":"MEDIUM"}'
```

#### Example: Filter by Priority

```bash
curl http://localhost:8080/api/tasks?priority=HIGH
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

### 📖 Swagger UI

Once the app is running:

🔗 http://localhost:8080/swagger-ui/index.html  
📄 http://localhost:8080/v3/api-docs
---

## 🧪 Testing

Run unit tests with:

```bash
./mvnw test
```

Includes:

- REST controller tests
- Service layer tests
- Validation failure cases
- Exception handling

---

## 🗂️ Project Structure

```
com.thewa.taskmanager
├── cli/              # CLI runner
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── exception/        # Custom exceptions & handler
├── mapper/           # Entity-DTO mappers
├── model/            # Domain models (Task, enums)
├── repository/       # In-memory repository
├── service/          # Business logic
└── TaskManagerApplication.java
```

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.5
- Maven
- JUnit 5 + Mockito
- Lombok
- Swagger

---

## 🧠 Design Decisions

- Clean layered architecture with separation of concerns
- In-memory repository for simplicity (can switch to DB later)
- DTOs + validation annotations ensure robust API
- CLI separated by profile to avoid unnecessary boot-time logic
- Builder pattern used for DTO immutability

---

## ✅ TODOs (For Extension)

- [ ] Add OpenAPI docs
- [ ] Add user authentication
- [ ] Support pagination
- [ ] Export tasks as CSV
- [ ] Persist to file or database

---

## 👨‍💻 Author

Made with ☕ and 💻 by Diwakar Singh 