# AGENTS.md — AI Agent Instructions

This file provides context and rules for any AI coding assistant (Gemini, Claude, Copilot, etc.) working on this project.

---

## Project Summary

| Attribute | Value |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.x |
| Design | Domain-Driven Design (DDD) + Test-Driven Development (TDD) |
| API | Single secured REST API |
| AI Integration | LangChain4j + Google Gemini |
| Database | PostgreSQL |
| Domain | Cooking recipe suggestions only |

---

## Package Structure

Always place new code in the correct layer:

```
com.myagent.
├── domain.model          # Entities, value objects
├── domain.port           # Repository & service interfaces (ports)
├── domain.service        # Domain services
├── application.usecase   # Application use cases / command handlers
├── infrastructure.persistence   # JPA repositories, entities
├── infrastructure.ai            # LangChain4j adapter
├── api.controller        # REST controllers
├── api.dto               # Request / response DTOs
└── api.security          # Security configuration
```

---

## Non-Negotiable Rules

1. **TDD first** — always write the test before the implementation.
2. **Domain layer is framework-free** — no Spring annotations, no JPA, no LangChain4j in `domain.*`.
3. **Ports & Adapters** — domain defines interfaces; infrastructure implements them.
4. **No mocks outside test files** — never use `Mockito` or similar in production code.
5. **English only** — all identifiers, comments, Javadoc, commit messages, and docs.
6. **Self-documenting code** — expressive names, no inline explanatory comments.
7. **Inclusive language** — `allowlist` / `blocklist`, `primary` / `replica`.
8. **SOLID principles** — follow Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion.

---

## Security Requirements

- All REST endpoints must be authenticated — no public routes except `/actuator/health`.
- Authentication via API key only — sent in the `X-API-Key` request header.
- Keys are validated against an allowlist stored in configuration or the database.
- Do not log sensitive data (API keys, user input).

---

## LangChain4j / Gemini Rules

- Define the AI port interface inside `domain.port` (e.g., `RecipeSuggestionPort`).
- Implement the adapter in `infrastructure.ai`.
- The system prompt **must** restrict the model exclusively to cooking topics.
- Validate the user prompt **before** sending it to the LLM:
  - Reject prompts that are clearly unrelated to cooking (keyword check or a classifier).
  - Return `400 Bad Request` with a descriptive message when rejected.

---

## Database Rules

- PostgreSQL only — no H2 or in-memory databases in any profile.
- Schema managed by **Flyway** — never modify tables manually.
- Use **Testcontainers** (`org.testcontainers:postgresql`) for all integration tests.
- JPA entities live in `infrastructure.persistence` — never in `domain`.

---

## Testing Strategy

| Test type | Annotation / Tool | What it covers |
|---|---|---|
| Unit | Plain JUnit 5 | Domain logic, use cases |
| Slice | `@WebMvcTest` | Controller + security |
| Integration | `@SpringBootTest` + Testcontainers | DB, LangChain4j adapter |
| Contract | (future) Pact or Spring Cloud Contract | API contract |

Run all tests with:

```bash
./gradlew test
```

---

## Adding a New Feature — Checklist

- [ ] Define / update the domain model in `domain.model`
- [ ] Add or update the port interface in `domain.port`
- [ ] Write the use case in `application.usecase` (test first)
- [ ] Implement the infrastructure adapter in `infrastructure.*` (test first)
- [ ] Add / update the REST controller in `api.controller` (slice test first)
- [ ] Add a Flyway migration if the DB schema changes
- [ ] Update this file if architectural decisions change

---

## Out of Scope

The following topics are **outside the domain** of this application. Any prompt or feature request in these areas must be rejected:

- Non-cooking LLM queries (news, code help, travel, finance, health, etc.)
- User management beyond authentication
- Any UI / frontend
- Any protocol other than REST (no WebSocket, no GraphQL, no gRPC)
