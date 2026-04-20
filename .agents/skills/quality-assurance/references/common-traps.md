---
description: |
  Common QA traps when working with AI-assisted development.
  Use when: reviewing AI-generated code, setting up test strategies, or debugging quality failures.
---

# Common QA Traps

## Trap 1: Coverage Theater

**Symptom:** 80% line coverage but tests only assert `assertNotNull(result)` — no business logic validation.

**Cause:** LLM optimizes for coverage metric, not for defect detection. It writes tests that execute lines without verifying behavior.

**Fix:**
- Every test must have at least one `assertEquals` or `assertThrows` on a BUSINESS value
- Ban `assertNotNull` as sole assertion — it proves existence, not correctness
- Review test names: if the name doesn't describe a behavior, the test is likely theater

**CI enforcement:**
```xml
<!-- Mutation testing catches coverage theater -->
<plugin>
  <groupId>org.pitest</groupId>
  <artifactId>pitest-maven-plugin</artifactId>
  <version>1.15.0</version>
  <configuration>
    <mutationThreshold>60</mutationThreshold>
  </configuration>
</plugin>
```

---

## Trap 2: Test Isolation Failure

**Symptom:** Tests pass individually but fail when run together. Order-dependent test suite.

**Cause:** Shared mutable state — static fields, database rows not cleaned up, singleton instances.

**Fix:**
- Each test method must set up AND tear down its own state
- Use `@BeforeEach` / `@AfterEach` with rollback
- Never rely on test execution order
- H2 in-memory DB with `DROP ALL OBJECTS` between test classes

---

## Trap 3: Testing Implementation, Not Behavior

**Symptom:** Refactoring a method's internals breaks 15 tests, even though the public behavior didn't change.

**Cause:** Tests verify HOW something is done (mock interactions, internal method calls) instead of WHAT is produced.

**Fix:**
- Test public API, not private methods
- Mock only external dependencies (DB, network, file system)
- If a test breaks because you renamed a private method, that test is wrong

---

## Trap 4: Flaky Tests Accepted as Normal

**Symptom:** "Oh, that test fails sometimes, just re-run it."

**Cause:** Race conditions, network dependencies, time-sensitive assertions, random data.

**Fix:**
- Zero tolerance for flaky tests — fix or delete
- Use `@RepeatedTest(10)` to expose flakiness early
- Mock time-dependent code (`Clock.fixed()` in Java, `jest.useFakeTimers()` in JS)
- Never depend on external services in unit tests

---

## Trap 5: LLM-Generated Test Name Fabrication

**Symptom:** Test references `UserService.findActiveUsers()` but the actual method is `UserService.buscarUsuariosAtivos()`.

**Cause:** LLM generates tests from its training data patterns instead of reading the actual source code.

**Fix:**
- **RULE: Read the source file before writing any test** (same as brownfield-refactoring trap #4)
- Run `mvn test-compile` after generating tests — catches fabricated names immediately
- If compilation fails, DELETE the generated test and retry with explicit "read file X first"
