---
description: |
  Given-When-Then test case template for structured test writing.
  Use when: writing test cases that need to be readable, traceable, and maintainable.
---

# Test Case Template

## Structure (Given-When-Then)

```
Test ID:      [MODULE]-[FEATURE]-[SCENARIO]-[NUMBER]
Priority:     P0 (critical) | P1 (high) | P2 (medium) | P3 (low)
Type:         Unit | Integration | E2E | Characterization
Requirement:  [Link to requirement or user story]

Given:  [Initial state / preconditions]
When:   [Action performed]
Then:   [Expected outcome / assertions]
```

## Example: Java Unit Test

```java
/**
 * Test ID: USER-AUTH-LOGIN-001
 * Priority: P0
 * Type: Unit
 * Requirement: US-042 - User can log in with valid credentials
 */
@Test
@DisplayName("Given valid credentials, when login, then returns authenticated user")
void givenValidCredentials_whenLogin_thenReturnsAuthenticatedUser() {
    // Given
    String username = "john.doe";
    String password = "valid-password";
    when(userDAO.findByUsername(username)).thenReturn(Optional.of(validUser));
    when(passwordEncoder.matches(password, validUser.getPasswordHash())).thenReturn(true);

    // When
    AuthResult result = authService.login(username, password);

    // Then
    assertTrue(result.isAuthenticated());
    assertEquals("john.doe", result.getUser().getUsername());
    assertNotNull(result.getToken());
}
```

## Example: Characterization Test (Brownfield)

```java
/**
 * Test ID: LEGACY-CALC-BEHAVIOR-001
 * Priority: P1
 * Type: Characterization
 * Requirement: Preserve existing behavior during refactoring
 */
@Test
@DisplayName("Characterization: calculateFee returns 0 for exempt students")
void characterization_calculateFee_returnsZeroForExemptStudents() {
    // Given: existing behavior captured as-is (may or may not be "correct")
    Student exempt = createStudent(StudentType.EXEMPT);

    // When
    BigDecimal fee = feeCalculator.calculateFee(exempt);

    // Then: this IS the current behavior — do not change without stakeholder approval
    assertEquals(BigDecimal.ZERO, fee);
}
```

## Test Naming Convention

```
[given]_[when]_[then]
```

Examples:
- `givenEmptyCart_whenAddItem_thenCartHasOneItem`
- `givenExpiredToken_whenAuthenticate_thenThrowsUnauthorized`
- `givenNullInput_whenValidate_thenReturnsFalse`

## Coverage Assertions Per Test Type

| Type | Minimum Assertions | What to Assert |
|------|-------------------|----------------|
| Unit | 1 assertEquals + 1 boundary | Return value, state change, exception |
| Integration | 1 persistence + 1 retrieval | Data roundtrip, constraint violations |
| Characterization | 1 exact match | Current behavior snapshot |
| E2E | HTTP status + body field | Response code, key fields, error messages |
