---
description: |
  Detailed guidance on when to use each test type in the test pyramid.
  Use when: deciding whether to write a unit, integration, or E2E test.
---

# Test Types Guide

## Unit Tests (70% of test suite)

**What they test:** A single class or method in isolation.
**Dependencies:** All mocked or stubbed. No database, no network, no file system.
**Speed:** < 50ms per test.

**Write a unit test when:**
- Testing business logic (calculations, validations, transformations)
- Testing pure functions (input → output, no side effects)
- Testing conditional logic (if/else, switch, strategy pattern)
- Testing error handling (exception types, error messages)

**Do NOT write a unit test when:**
- The value comes from database interaction (use integration)
- The behavior depends on HTTP request/response (use integration)
- You're testing framework behavior, not YOUR code

```java
// Good unit test: tests business logic
@Test
void shouldApplyStudentDiscount() {
    var calc = new FeeCalculator();
    var fee = calc.calculate(StudentType.SCHOLARSHIP, baseFee);
    assertEquals(BigDecimal.ZERO, fee);
}
```

## Integration Tests (20% of test suite)

**What they test:** Interaction between components — typically code + database or code + external API.
**Dependencies:** Real database (H2/testcontainers), real HTTP (MockMvc/WireMock).
**Speed:** < 5 seconds per test.

**Write an integration test when:**
- Testing DAO/Repository methods (CRUD, queries, joins)
- Testing API endpoints (request → response)
- Testing database constraints (unique, foreign key, cascading)
- Testing transaction boundaries (commit, rollback)

**Do NOT write an integration test when:**
- You can test the logic with a unit test (don't use H2 for arithmetic)
- The test requires a production database (use H2 with compatibility mode)

```java
// Good integration test: tests DAO + database interaction
@Test
void shouldPersistAndRetrieveStudent() {
    Student s = new Student("Maria", "12345");
    dao.save(s);
    Student found = dao.findByRa("12345");
    assertEquals("Maria", found.getName());
}
```

## E2E Tests (10% of test suite)

**What they test:** Complete user journey through the system.
**Dependencies:** Full application stack running (app server, database, auth).
**Speed:** < 60 seconds per test.

**Write an E2E test when:**
- Critical business flows (login, payment, registration)
- Flows that cross multiple services or layers
- Smoke tests for deployment verification

**Do NOT write an E2E test when:**
- A unit or integration test can verify the same behavior
- The flow is a simple CRUD with no business logic
- The test would require manual setup (external services, VPN, hardware)

## Characterization Tests (Legacy/Brownfield Only)

**What they test:** Current behavior of existing code, as-is, without judgment.
**Purpose:** Safety net for refactoring. Not TDD.

**Write a characterization test when:**
- Refactoring legacy code that has no tests
- You need to understand what the code actually does (vs what it should do)
- Migrating from one framework/version to another

```java
// Characterization: captures existing behavior
@Test
void characterization_legacyTaxCalc_returns15PercentForRegular() {
    // This IS the behavior — do not "fix" it without stakeholder approval
    assertEquals(0.15, legacyCalc.getTaxRate("REGULAR"), 0.001);
}
```

## Decision Matrix

| Question | Unit | Integration | E2E |
|----------|------|-------------|-----|
| Does it need a database? | No → Unit | Yes → Integration | — |
| Does it cross network boundaries? | No → Unit | Yes → Integration | — |
| Is it a complete user journey? | — | — | Yes → E2E |
| Can I test it without a framework? | Yes → Unit | — | — |
| Is it a critical business flow? | — | — | Yes → E2E |
