# GEMINI.md — Project Guidelines

## Project Overview

Java 17 Spring Boot application exposing a secured REST API that uses LangChain4j and Google Gemini to provide cooking recipe suggestions. All business logic follows Domain-Driven Design (DDD) principles and all code is validated through Test-Driven Development (TDD).

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x (latest stable) |
| AI / LLM | LangChain4j + Google Gemini |
| Persistence | PostgreSQL |
| Security | Spring Security (API key) |
| Build | Gradle |
| Testing | JUnit 5 + AssertJ + Testcontainers |

---

## Architecture

The project follows **Domain-Driven Design (DDD)** with the following layer separation:

```
src/main/java/com/myagent/
├── domain/           # Entities, value objects, domain services, repository interfaces
├── application/      # Use cases / application services
├── infrastructure/   # Repository implementations, LangChain4j adapters, DB config
└── api/              # REST controllers, DTOs, security config
```

---

## API

- Single REST API — no UI, no GraphQL, no gRPC.
- All endpoints are **secured** (authentication required on every route).
- The API only exposes cooking-related functionality.

### Endpoint: Recipe Suggestions

```
POST /api/v1/recipes/suggestions
X-API-Key: <your-api-key>
Content-Type: application/json

{
  "prompt": "Give me a vegetarian pasta recipe for 4 people"
}
```

**Behaviour:**
- Validates that the prompt is related to cooking / recipes.
- Rejects any prompt unrelated to cooking with `400 Bad Request`.
- Forwards valid prompts to Google Gemini via LangChain4j.
- Returns a structured cooking suggestion.

---

## LangChain4j Integration

- Use `langchain4j-google-ai-gemini` (or `langchain4j-vertex-ai-gemini`) as the Gemini provider.
- Define a typed `AiService` interface in the `domain` layer as a port.
- The infrastructure adapter implements that port using LangChain4j.
- System prompt must restrict the model to cooking topics only.

Example system prompt:

```
You are a professional chef assistant. You only provide cooking recipes and culinary advice.
If the user asks about anything unrelated to food, cooking, or recipes, politely refuse
and remind them that you can only help with culinary topics.
```

---

## Database

- PostgreSQL as the only data store.
- Use Spring Data JPA / Hibernate for persistence.
- Run schema migrations with **Flyway**.
- Use **Testcontainers** for integration tests (no H2 in-memory).

---

## Security

- Every endpoint requires authentication via API key.
- No anonymous access allowed.
- API key must be sent in the `X-API-Key` request header.
- Keys are validated against an allowlist stored in configuration or the database.
- Configure CORS appropriately.

---

## Development Principles

### DDD
- Domain layer has zero dependencies on frameworks.
- Infrastructure implements domain interfaces (dependency inversion).
- Use value objects for identifiers and domain concepts (e.g., `Prompt`, `RecipeSuggestion`).

### SOLID
- **S**ingle Responsibility: Each class has one reason to change.
- **O**pen/Closed: Entities are open for extension, closed for modification.
- **L**iskov Substitution: Subtypes must be substitutable for their base types.
- **I**nterface Segregation: Clients should not depend on methods they don't use.
- **D**ependency Inversion: Depend on abstractions, not concretions.

### TDD
- Write tests before implementation.
- Unit tests for domain logic (no Spring context).
- Integration tests for infrastructure adapters using Testcontainers.
- API/slice tests for controllers using `@WebMvcTest`.

---

## Testing

| Test type | Annotation / Tool | What it covers |
|---|---|---|
| Unit | Plain JUnit 5 | Domain logic, use cases |
| Slice | `@WebMvcTest` | Controller + security |
| Integration | `@SpringBootTest` + Testcontainers | DB, LangChain4j adapter |

Run all tests with:

```bash
./gradlew test
```

---

## Code Style Rules

- English only — code, comments, commits, documentation, and tests.
- Self-documenting code — no explanatory comments.
- Inclusive terminology: `allowlist` / `blocklist`, `primary` / `replica`.
- No mocks outside test files.
