---
description: |
  5 validated traps in greenfield development with LLM agents.
  Use when: starting a new project with AI assistance and want to avoid known failures.
---

# Common Greenfield Traps

## Trap 1: Over-Architecture

**Symptom:** Agent creates `AbstractRepository<T>`, `BaseService<T, ID>`, `GenericMapper<S, D>` before writing a single concrete class.

**Cause:** LLM training data is heavy on tutorial/framework code that demonstrates patterns, not production code that solves problems.

**Fix:** Explicit instruction in AGENTS.md:
```
RULE: No abstract base classes until there are 3+ concrete implementations that share >70% of their code.
Start with concrete classes. Extract abstractions only when duplication is proven.
```

**Evidence:** In npd-service-academico, a fresh agent with this rule achieved 95.8% compliance. Without it, agents created 8 abstract classes for 3 concrete ones.

---

## Trap 2: Framework Soup

**Symptom:** Agent adds multiple competing frameworks (Spring MVC + JAX-RS, Hibernate + MyBatis, JUnit 4 + JUnit 5 + TestNG).

**Cause:** LLM doesn't distinguish "alternatives" from "complements" — if it's seen both in training data, it uses both.

**Fix:** Lock the stack in AGENTS.md:
```
STACK (do not add alternatives):
- Web: Spring Boot 3.2.x (NO JAX-RS, NO Quarkus)
- ORM: Spring Data JPA (NO raw Hibernate, NO MyBatis)
- Test: JUnit 5 + Mockito (NO JUnit 4, NO TestNG)
```

**Evidence:** SGE refactoring — agent tried to add JUnit 5 alongside JUnit 4.10 in same project, causing classpath conflicts. Explicit "NO JUnit 4" rule fixed it.

---

## Trap 3: Test-Last Development

**Symptom:** Agent writes 500 lines of business logic, then says "Now let's add tests" — tests are shallow, miss edge cases, or test implementation details.

**Cause:** LLM optimizes for the immediate request. If you say "create a user service", it creates the service. Tests are a separate request = separate context = worse quality.

**Fix:** In AGENTS.md:
```
RULE: Every new class must have its test created in the same commit.
The test file is created FIRST (empty skeleton), then the implementation.
No PR/commit passes CI without corresponding test files.
```

**CI enforcement:**
```yaml
# GitHub Actions: fail if new .java files don't have corresponding *Test.java
- name: Check test coverage for new files
  run: |
    for f in $(git diff --name-only --diff-filter=A HEAD~1 -- '*.java' | grep -v Test); do
      test_file=$(echo $f | sed 's|main|test|' | sed 's|\.java|Test.java|')
      [ -f "$test_file" ] || { echo "Missing test for $f"; exit 1; }
    done
```

---

## Trap 4: Config Hallucination

**Symptom:** Agent writes `application.yml` with keys that don't exist in the framework version.

**Examples:**
- `spring.jpa.hibernate.ddl-auto: create-drop` (correct) vs `spring.jpa.auto-ddl: create` (fabricated)
- `server.servlet.context-path` (correct) vs `server.context-path` (deprecated in Spring Boot 3)
- `logging.level.root: DEBUG` (correct) vs `logging.root.level: DEBUG` (fabricated)

**Fix:** Include a reference config in your project:
```
# In AGENTS.md:
RULE: When modifying application.yml, read the existing file first.
Only use property keys that already exist OR are documented in the official Spring Boot reference.
Never invent YAML keys.
```

Better: include a `src/main/resources/application.yml` with ALL expected keys commented out, so the agent has a menu to choose from.

---

## Trap 5: Premature Abstraction

**Symptom:** Agent creates `UserDTOMapper`, `UserResponseDTO`, `UserRequestDTO`, `UserPatchDTO`, `UserCreateDTO` for a CRUD endpoint with 4 fields.

**Cause:** LLM has seen enterprise patterns and applies them regardless of scale.

**Fix:**
```
RULE: DTOs are only created when the API response differs from the entity by 3+ fields.
For simple CRUDs, the entity IS the DTO until proven otherwise.
Mapper classes are only created when mapping logic has conditionals — simple field copies don't need a mapper.
```

**Evidence:** npd-service-academico achieved the same functionality with 40% fewer classes when this rule was enforced.
