---
description: |
  Per-feature TDD cycle checklist. Copy for each feature.
  Use when: implementing a feature using TDD discipline.
---

# TDD Cycle Checklist

## Feature: ____________________

### Phase 1: Understand (before writing any code)
- [ ] Requirement is clear and testable
- [ ] Acceptance criteria defined (Given/When/Then)
- [ ] Edge cases identified (null, empty, max, concurrent)
- [ ] Dependencies identified (what needs mocking?)

### Phase 2: Test Infrastructure
- [ ] Test class created in correct package under `src/test/`
- [ ] Test dependencies available (JUnit, Mockito, H2, etc.)
- [ ] `mvn test-compile` passes with empty test class

### Phase 3: Red-Green-Refactor (repeat per behavior)

#### Behavior 1: ____________________
- [ ] 🔴 RED: Test written and FAILS (`mvn test` shows failure)
- [ ] 🟢 GREEN: Minimum implementation makes test PASS
- [ ] 🔧 REFACTOR: Code cleaned up, test still passes
- [ ] ✅ Committed

#### Behavior 2: ____________________
- [ ] 🔴 RED: Test written and FAILS
- [ ] 🟢 GREEN: Minimum implementation makes test PASS
- [ ] 🔧 REFACTOR: Code cleaned up, test still passes
- [ ] ✅ Committed

#### Behavior 3: ____________________
- [ ] 🔴 RED: Test written and FAILS
- [ ] 🟢 GREEN: Minimum implementation makes test PASS
- [ ] 🔧 REFACTOR: Code cleaned up, test still passes
- [ ] ✅ Committed

### Phase 4: Edge Cases
- [ ] Null input test
- [ ] Empty collection test
- [ ] Boundary value test (min/max)
- [ ] Error path test (exception/invalid state)

### Phase 5: Verification
- [ ] All tests pass: `mvn test`
- [ ] Coverage meets threshold: `mvn verify`
- [ ] No `TODO` or `FIXME` left in code
- [ ] Code reviewed (or self-reviewed against checklist)
