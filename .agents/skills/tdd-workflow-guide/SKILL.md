---
name: tdd-workflow-guide
description: |
  Guide Test-Driven Development workflow for AI agents with red-green-refactor cycle, test pyramid, and test-first discipline.
  Use when: writing code test-first, implementing TDD in a project, choosing test types, or enforcing test-before-code discipline with LLM agents.
activation: Auto
estimated_tokens: 1130
---

# TDD Workflow Guide

This skill enforces the test-first discipline that prevents the most common AI agent failure: writing code without tests, then "adding tests later" (which never happens properly).

## The Red-Green-Refactor Cycle

Every code change follows this cycle. No exceptions.

```
1. RED:      Write a failing test that describes the behavior you want
2. GREEN:    Write the MINIMUM code to make the test pass
3. REFACTOR: Clean up the code while keeping tests green
4. REPEAT:   Next behavior → next failing test → next implementation
```

**The critical rule:** NEVER write production code without a failing test first.

## How to Write the Failing Test First

```java
// Step 1: RED — write the test BEFORE the implementation exists
@Test
@DisplayName("Given valid student, when calculate fee, then returns base fee minus discount")
void givenValidStudent_whenCalculateFee_thenReturnsDiscountedFee() {
    // Given
    Student student = new Student("John", StudentType.REGULAR);
    FeeCalculator calc = new FeeCalculator();

    // When
    BigDecimal fee = calc.calculateFee(student);

    // Then
    assertEquals(new BigDecimal("800.00"), fee);
}
// This test MUST fail (RED) because FeeCalculator.calculateFee() doesn't exist yet

// Step 2: GREEN — implement the minimum to pass
public BigDecimal calculateFee(Student student) {
    return new BigDecimal("800.00"); // minimum implementation
}

// Step 3: REFACTOR — add real logic, test stays green
public BigDecimal calculateFee(Student student) {
    BigDecimal baseFee = new BigDecimal("1000.00");
    BigDecimal discount = getDiscount(student.getType());
    return baseFee.subtract(discount);
}
```

## The Test Pyramid

Choose the right test type for each layer. More tests at the bottom, fewer at the top.

```
         /  E2E  \          ← Few: critical user journeys only
        / Integr.  \        ← Some: DB, API, external services
       /   Unit     \       ← Many: business logic, pure functions
      ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
```

| Type | Tests | Speed | What | When |
|------|-------|-------|------|------|
| Unit | 70% | Fast (<1s) | Single class/method, no external deps | Every method with logic |
| Integration | 20% | Medium (<10s) | DB, API, file system interactions | Every DAO, every API call |
| E2E | 10% | Slow (<60s) | Full user journey through the system | Critical paths only |

Load `references/test-types.md` for detailed guidance on when to use each test type.

## How to Apply TDD with LLM Agents

LLM agents naturally resist TDD — they want to write the implementation first. Force the workflow:

```
Prompt pattern:
"Write ONLY the test for [behavior]. Do NOT write the implementation.
The test must fail when run (RED phase).
After I confirm the test fails, I will ask you to implement."
```

Load `references/common-traps.md` for the 5 most common TDD failures with LLM agents.

## How to Handle Legacy Code (Characterization Tests)

When adding tests to existing code that already works:

1. Write a test that captures the CURRENT behavior (even if "wrong")
2. Run it — it MUST pass (you're documenting existing behavior, not desired behavior)
3. This is a **characterization test**, not a TDD test
4. Now you can refactor safely: if the characterization test breaks, you changed behavior

```java
@Test
@DisplayName("Characterization: legacy calculateTax returns 0 for null input")
void characterization_calculateTax_returnsZeroForNull() {
    // This captures existing behavior — may or may not be "correct"
    assertEquals(BigDecimal.ZERO, taxService.calculateTax(null));
}
```

## Coverage Targets

| Context | Line | Branch | Enforcement |
|---------|------|--------|-------------|
| Greenfield (new code) | 80% | 70% | JaCoCo/Jest gate in CI |
| Brownfield (legacy) | 70% | 60% | JaCoCo gate, exclude untestable |
| Library/SDK | 90% | 80% | Strict gate, no exclusions |
| Hotfix/Patch | 100% of changed lines | — | Manual review |

Load `templates/tdd-cycle-checklist.md` for a per-feature TDD checklist.
