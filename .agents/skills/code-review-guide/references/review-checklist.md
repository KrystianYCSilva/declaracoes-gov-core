---
description: |
  Review checklist organized by the 3-pass review structure.
  Use when: performing a structured code review, either manually or as an AI agent.
---

# Code Review Checklist

## Pass 1: Correctness

### Logic
- [ ] Code implements the stated requirement (check ticket/issue)
- [ ] Conditional logic is correct (no inverted conditions, missing else branches)
- [ ] Loop termination is guaranteed (no infinite loops)
- [ ] Return values are correct for all paths (including error paths)
- [ ] Null/empty handling is explicit (no implicit null assumptions)

### Data
- [ ] Data types are appropriate (no precision loss, no overflow risk)
- [ ] Database queries match the expected schema
- [ ] Input validation is present and correct
- [ ] Output format matches the API contract

### Integration
- [ ] API calls match the documented interface
- [ ] External service failures are handled (timeout, error response)
- [ ] Database transactions have correct boundaries (commit/rollback)

## Pass 2: Safety

### Security
- [ ] No hardcoded credentials, tokens, or secrets
- [ ] User input is sanitized before use in queries/commands
- [ ] Authentication/authorization checks are in place
- [ ] Sensitive data is not logged or exposed in error messages

### Reliability
- [ ] Resources are properly closed (files, connections, streams)
- [ ] Error handling is specific (not bare catch-all)
- [ ] Retry logic has backoff and limits (no infinite retry)
- [ ] Concurrent access is safe (no race conditions on shared state)

### Data Protection
- [ ] PII is handled according to policy (encryption, masking)
- [ ] Audit trail exists for sensitive operations
- [ ] No data leakage to logs, error responses, or external services

## Pass 3: Maintainability

### Readability
- [ ] Code is self-documenting (names describe intent)
- [ ] Complex logic has inline comments explaining WHY (not WHAT)
- [ ] Methods are ≤ 30 lines (split long methods)
- [ ] Nesting depth ≤ 3 levels (extract helper methods)

### Structure
- [ ] Single Responsibility: each class/method does one thing
- [ ] No code duplication (extract shared logic)
- [ ] Dependencies are injected, not hardcoded
- [ ] No dead code (unused methods, commented-out blocks)

### Testing
- [ ] New code has corresponding test files
- [ ] Tests cover happy path + at least 1 error path
- [ ] Tests are independent and repeatable
- [ ] Test names describe the behavior being tested
- [ ] No test-only code in production source

## Quick Reference: Severity by Category

| Category | Typical Severity |
|----------|-----------------|
| Logic errors | 🔴 Critical |
| Security issues | 🔴 Critical |
| Missing error handling | 🟡 Important |
| Missing tests | 🟡 Important |
| Code duplication | 🟡 Important |
| Naming improvements | 🔵 Suggestion |
| Performance micro-optimization | ⚪ Nitpick |
| Formatting | ⚪ Nitpick |
