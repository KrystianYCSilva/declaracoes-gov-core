---
description: |
  Requirement-to-test traceability matrix template.
  Use when: tracking which requirements are covered by which tests.
---

# Traceability Matrix

## How to Use

1. List all requirements with unique IDs
2. For each requirement, link to the test(s) that verify it
3. Status: ✅ Covered | ⚠️ Partial | ❌ Not covered | 🚫 Out of scope

## Template

| Req ID | Requirement | Priority | Test ID(s) | Status | Notes |
|--------|-------------|----------|------------|--------|-------|
| REQ-001 | User can register with email and password | P0 | USER-REG-001, USER-REG-002 | ✅ | Happy path + validation |
| REQ-002 | Password must be at least 8 characters | P1 | USER-REG-003 | ✅ | |
| REQ-003 | Duplicate email rejected with clear message | P1 | USER-REG-004 | ✅ | |
| REQ-004 | Email verification sent on registration | P2 | — | 🚫 | Deferred to v2 |
| REQ-005 | API responds within 200ms at p95 | P1 | PERF-001 | ⚠️ | Only tested locally |

## Coverage Summary

| Status | Count | Percentage |
|--------|-------|------------|
| ✅ Covered | 0 | 0% |
| ⚠️ Partial | 0 | 0% |
| ❌ Not covered | 0 | 0% |
| 🚫 Out of scope | 0 | 0% |

## Linking Tests to Requirements (Code Convention)

### Java
```java
/**
 * Requirement: REQ-001
 * Acceptance: User can register with valid email and password
 */
@Test
@DisplayName("REQ-001: Given valid email and password, when register, then user is created")
void req001_givenValidInput_whenRegister_thenUserCreated() { ... }
```

### JavaScript
```javascript
// Requirement: REQ-001
test('REQ-001: registers user with valid email and password', async () => { ... });
```

### Python
```python
def test_req001_register_with_valid_email_and_password():
    """Requirement: REQ-001 - User can register with email and password"""
    ...
```
