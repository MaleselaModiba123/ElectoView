# Contributing to ElectroView

Thank you for your interest in contributing to ElectroView, an electricity
usage analytics dashboard built with Spring Boot. This guide will get you
set up and explain how to submit changes.

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Git

### Setup Instructions

1. **Fork** this repository (click the Fork button at the top right).

2. **Clone** your fork:
```bash
   git clone https://github.com/YOUR-USERNAME/ElectoView.git
   cd ElectoView/electroview
```

3. **Create the database** in MySQL:
```sql
   CREATE DATABASE IF NOT EXISTS electroview;
```

4. **Configure** `src/main/resources/application.properties` with your MySQL
   username and password.

5. **Build and run** the project:
```bash
   mvn clean install
   mvn spring-boot:run
```

6. **Verify** the app is running by opening the Swagger UI:

http://localhost:8080/swagger-ui.html

---

## Coding Standards

- **Language:** Java 17
- **Style:** Follow standard Java naming conventions (PascalCase for classes,
  camelCase for methods and variables, UPPER_SNAKE_CASE for constants).
- **Architecture:** Respect the existing layered structure —
  `controller → service → repository`. Business logic belongs in services,
  not controllers or repositories.
- **Lombok:** Use Lombok annotations (`@Getter`, `@Setter`, `@Builder`)
  rather than writing boilerplate by hand.
- **Tests:** Every new feature or bug fix must include tests. We use JUnit 5
  and Mockito. Do not submit a PR that lowers test coverage.

### Running Tests

Before submitting a PR, make sure all tests pass:

```bash
mvn test
```

The CI pipeline will also run these automatically on your PR. A PR cannot
be merged unless all tests pass.

---

## How to Pick an Issue

1. Browse the [Issues](../../issues) tab.
2. Look for issues labeled **`good-first-issue`** if you are new — these are
   small, well-scoped tasks ideal for getting familiar with the codebase.
3. Look for **`feature-request`** issues if you want to take on something larger.
4. Comment on the issue to let others know you are working on it, so two
   people don't duplicate effort.

---

## Submitting a Pull Request

1. Create a branch from `main` with a descriptive name:
```bash
   git checkout -b fix/anomaly-escalation-bug
```

2. Make your changes and commit with a clear message:
```bash
   git commit -m "Fix anomaly escalation not triggering after 48 hours"
```

3. Push your branch to your fork:
```bash
   git push origin fix/anomaly-escalation-bug
```

4. Open a Pull Request targeting the `main` branch of this repository.

5. In the PR description, clearly explain:
   - What problem you are solving
   - How you solved it
   - Which issue it closes (e.g., "Closes #12")

6. Wait for CI to pass and for a maintainer to review. Address any
   requested changes by pushing more commits to the same branch.

---

## Code of Conduct

Be respectful and constructive in all discussions. I welcome contributors
of all experience levels. Questions are always welcome — open an issue if
you are stuck.

---

Thank you for helping make ElectroView better!