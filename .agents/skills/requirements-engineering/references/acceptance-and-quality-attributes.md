---
description: |
  Reference for separating acceptance criteria, quality attributes, and engineering constraints.
  Use when: sharpening success criteria, defining non-functional requirements,
  or checking whether a requirement is testable and reviewable.
---

# Acceptance and Quality Attributes

## Acceptance Criteria

Good acceptance criteria are observable and binary enough to verify.

### Given/When/Then Pattern

Use the Gherkin-style format for behavioral acceptance criteria:

```
Given [precondition or initial state]
When  [action or event]
Then  [expected outcome]
```

Examples:

- Given a logged-in user with role ADMIN, when they request GET /api/v1/referencia/paises, then the response status is 200 and the body contains a non-empty list.
- Given an unauthenticated request, when it hits any protected endpoint, then the response status is 401 and no data is leaked.
- Given a Municipio with codigoIbge already in use, when a POST attempts to create a duplicate, then the response status is 409 with a descriptive error message.

### Criteria Quality Checks

- Each criterion is testable by a single automated test.
- Criteria avoid vague words ("fast", "user-friendly") unless quantified.
- Negative/edge cases are covered, not just the happy path.

## Quality Attributes (ISO 25010)

Capture non-functional expectations separately from functional behavior.

| Attribute | Sub-characteristics | Example Specification |
|-----------|--------------------|-----------------------|
| **Performance Efficiency** | Time behavior, Resource utilization, Capacity | P95 latency < 200ms for list endpoints under 50 concurrent users |
| **Reliability** | Maturity, Availability, Fault tolerance, Recoverability | 99.5% uptime monthly; automatic restart on OOM |
| **Security** | Confidentiality, Integrity, Non-repudiation, Accountability, Authenticity | JWT tokens expire in <= 30 min; all PII encrypted at rest |
| **Maintainability** | Modularity, Reusability, Analysability, Modifiability, Testability | Line coverage >= 80%; branch coverage >= 75% |
| **Compatibility** | Co-existence, Interoperability | REST API follows OpenAPI 3.0; no breaking changes within a major version |
| **Usability** | Learnability, Operability, Error protection | API error responses include machine-readable code and human-readable message |
| **Portability** | Adaptability, Installability, Replaceability | Deployable as WAR on any Servlet 4.0+ container |
| **Functional Suitability** | Completeness, Correctness, Appropriateness | All CRUD operations per entity specified in the requirements document |

### Quality Attribute Specification Format

For each important quality attribute, record:

1. **Attribute**: Which ISO 25010 attribute.
2. **Stimulus**: What triggers the scenario (e.g., 100 concurrent requests).
3. **Environment**: Under what conditions (e.g., production, peak hours).
4. **Response**: What the system does (e.g., serves all requests).
5. **Measure**: How success is quantified (e.g., P95 < 200ms).

## Constraints

Capture stack, regulatory, timeline, compatibility, and operational boundaries without confusing them with user value.

- Stack constraints: Java 11, Spring Boot 2.7.x, DB2.
- Regulatory constraints: Data residency, audit logging.
- Operational constraints: WAR deployment, no container orchestration assumed.
- Timeline constraints: Must ship by a specific milestone.
