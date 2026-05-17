## Service Layer and REST API

### Architecture

The application follows a clean three-layer architecture:

Controller (REST API)  →  Service (business logic)  →  Repository (persistence)

Each layer has a single responsibility. Controllers handle HTTP only,
services enforce business rules, repositories handle storage. This
separation means the storage backend can change without touching
business logic, and the API contract stays stable regardless of
internal changes.

### API Documentation

Interactive API documentation is available via Swagger UI when the
application is running:

http://localhost:8080/swagger-ui.html

Three entities are fully documented as the foundation for group work:

| Entity | Base Path | Endpoints |
|---|---|---|
| Users | `/api/users` | Create, read, activate, deactivate, unlock, update role, reset password |
| Zones | `/api/zones` | Create, read, activate, deactivate, update threshold, refresh status |
| Meters | `/api/meters` | Register, read, activate, deactivate, decommission, assign consumer |

Every endpoint is annotated with its summary, description, and possible
HTTP response codes including error responses (400, 404).

![Swagger UI](./Screenshot (101).png)
![Swagger UI](./Screenshot (102).png)
![Swagger UI](./Screenshot (103).png)


### Error Handling

A `GlobalExceptionHandler` (`@RestControllerAdvice`) centralises error
handling. Any `IllegalArgumentException` thrown by the service layer
(e.g., duplicate email, invalid threshold) is automatically converted
into a clean `400 Bad Request` JSON response instead of a generic 500.

### Testing

| Test Type | Coverage | Database |
|---|---|---|
| Unit tests (Assignment 10) | Service logic in isolation, mocked repositories | None — Mockito |
| Integration tests (Assignment 12) | Full HTTP → controller → service → repository stack | In-memory H2 |

Integration tests use `@SpringBootTest` with a random port and a separate
H2 test database, so they verify the real running system end to end without
touching the production MySQL database.