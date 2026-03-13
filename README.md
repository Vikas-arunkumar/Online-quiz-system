# 📝 Online Quiz System

A full-stack quiz platform built with Java and Spring Boot, featuring role-based authentication for students and teachers.

## 🚀 Features

- **Role-based login** — separate dashboards and flows for students and teachers
- **Dynamic question rendering** — questions load and display in real time
- **Answer submission & results** — students can submit answers and instantly view scores
- **Teacher controls** — teachers can view all student results
- **MVC architecture** — clean separation of concerns using Spring MVC pattern
- **In-memory persistence** — powered by H2 database for lightweight data management

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java, Spring Boot |
| Frontend | Thymeleaf |
| Database | H2 (in-memory) |
| Architecture | MVC (Model-View-Controller) |

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven

### Run Locally
```bash
# Clone the repository
git clone https://github.com/Vikas-arunkumar/Online-quiz-system.git

# Navigate to project directory
cd Online-quiz-system

# Run the application
./mvnw spring-boot:run
```

Then open your browser and go to `http://localhost:8080`

## 🔐 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Teacher | teacher | pass |
| Student | student | pass |

> Update these in `application.properties` before deploying.

## 📁 Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── controller/     # Spring MVC controllers
│   │   └── model/          # Data models
│   │   └── repository/     # H2 database access
│   │   └── service/        # Business logic
│   └── resources/
│       ├── templates/      # Thymeleaf HTML templates
│       └── application.properties
```

## 🙋 Author

**Vikas Arunkumar** — [GitHub](https://github.com/Vikas-arunkumar) · [LinkedIn](https://www.linkedin.com/in/vikas-arunkumar-05586b2b5)
