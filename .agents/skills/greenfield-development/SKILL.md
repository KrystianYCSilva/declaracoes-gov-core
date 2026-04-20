---
name: greenfield-development
description: |
  Guide greenfield project setup with test-first architecture, CI/CD gates, and production-ready structure from day one.
  Use when: creating new projects, bootstrapping microservices, setting up project scaffolding, choosing tech stacks, or establishing test infrastructure for new codebases.
activation: Auto
estimated_tokens: 1220
---

# Greenfield Development

This skill captures patterns for starting projects right — so you never need brownfield refactoring later.
Every rule comes from real failures observed across 10+ sessions of LLM-assisted development.

## The Formula

```
3 Artefatos + 1 Exemplo = 95.8% pattern compliance (validated with fresh agent)
```

1. **AGENTS.md** — project conventions the LLM reads every session (~150 lines max)
2. **One working example** — a complete vertical slice (entity → DAO → service → test → controller)
3. **CI/CD gate** — `mvn verify` / `npm test` with coverage threshold blocks non-compliant code

## How to Bootstrap Project Structure

Start with the vertical slice, not with architecture. Ship one feature end-to-end before expanding.

```
project/
├── AGENTS.md                           ← conventions for AI agents (load templates/agents-md.md)
├── pom.xml / package.json / go.mod     ← build + dependencies + test plugins
├── src/main/java/com/example/
│   ├── model/        Entity.java       ← 1 entity with annotations
│   ├── dao/          EntityDAO.java    ← 1 DAO with CRUD
│   ├── service/      EntityService.java← 1 service with business logic
│   └── controller/   EntityCtrl.java   ← 1 controller (if web)
├── src/test/java/com/example/
│   ├── model/        EntityTest.java   ← persist/retrieve test
│   ├── dao/          EntityDAOTest.java← CRUD test via H2/testcontainers
│   ├── service/      EntityServiceTest.java ← unit test with mocks
│   └── support/      TestSupport.java  ← base class (auto-rollback, test DB)
└── src/test/resources/
    └── test-config.xml / application-test.yml
```

Load `templates/project-checklist.md` for the full pre-flight checklist.

## How to Choose Tech Stack

Pick boring technology. Every novel choice costs 10x in LLM accuracy.

| Need | Recommended | Why |
|------|-------------|-----|
| Java build | Maven + Surefire + JaCoCo | Every LLM knows Maven; Gradle DSL causes hallucination |
| Java test | JUnit 5 + Mockito + H2 | Most examples in LLM training data |
| Node build | npm + Jest + ESLint | Standard, zero-config |
| Python | pytest + coverage + ruff | Simple, fast, well-known |
| Coverage gate | JaCoCo / Jest --coverage / pytest-cov | Must be in CI, not optional |

Load `references/tech-stack-decisions.md` for detailed rationale and version recommendations.

## How to Write the First Test

The first test proves the infrastructure works. Write it BEFORE any business logic.

```java
// Java example — "can I persist and retrieve an entity?"
@Test
void shouldPersistAndRetrieveEntity() {
    Entity e = new Entity();
    e.setName("test");
    dao.save(e);
    Entity found = dao.findById(e.getId());
    assertNotNull(found);
    assertEquals("test", found.getName());
}
```

If this test passes, your DB config, ORM mapping, and test infrastructure all work. Everything else is incremental.

## How to Set Coverage Gates

Set coverage gates on day one. Retroactive coverage is 10x harder.

```xml
<!-- Maven: JaCoCo in pom.xml -->
<rule>
  <element>BUNDLE</element>
  <limit><counter>LINE</counter><value>COVEREDRATIO</value><minimum>0.80</minimum></limit>
</rule>
```

For greenfield, target **80% line coverage** (not 70% like brownfield — you control all the code).

## How to Write AGENTS.md for a New Project

Keep it under 150 lines. Include ONLY:

1. **Stack** — language, framework, build tool, test framework, versions
2. **Conventions** — naming, package structure, error handling pattern
3. **One rule per trap** — each rule exists because an LLM failed without it
4. **Example pointer** — "read src/main/.../model/Entity.java as the reference implementation"

Load `templates/agents-md.md` for a starter template.

## Common Traps in Greenfield

Load `references/common-traps.md` for the 5 most frequent greenfield failures with fixes.

Quick summary:
1. **Over-architecture** — agent creates 15 interfaces before writing one test
2. **Framework soup** — agent adds Spring Boot + Quarkus + Micronaut in same project
3. **Test-last** — "I'll add tests after the feature works" → tests never happen
4. **Config hallucination** — agent invents YAML keys that don't exist
5. **Premature abstraction** — agent creates AbstractBaseGenericFactory before the first concrete class
