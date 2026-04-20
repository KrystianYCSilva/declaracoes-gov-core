---
description: |
  Common traps in requirements engineering, especially with AI-assisted development.
  Use when: writing requirements that will be implemented by LLM agents.
---

# Common Requirements Traps

## Trap 1: Solution Masquerading as Requirement

**Symptom:** "The system shall use Redis for caching" instead of "The system shall respond within 200ms for cached queries."

**Cause:** Stakeholders describe solutions they've already imagined. LLMs amplify this by implementing literally what's written.

**Fix:** For every requirement, ask: "Could this be satisfied by a different implementation?" If yes, it's a requirement. If no, it's a constraint or design decision — label it as such.

---

## Trap 2: Untestable Language

**Symptom:** "The system should be fast", "The UI must be user-friendly", "Data should be secure."

**Cause:** Natural language ambiguity. LLMs interpret vague requirements by hallucinating specifics.

**Fix:** Apply the Observable Test:
- ❌ "The system should be fast" → not testable
- ✅ "API responses under 200ms at p95 with 100 concurrent users" → testable
- ❌ "Data should be secure" → not testable
- ✅ "All PII fields encrypted at rest with AES-256" → testable

---

## Trap 3: Missing Negative Requirements

**Symptom:** Agent builds happy path perfectly but system crashes on empty input, null values, or concurrent access.

**Cause:** Requirements describe what the system SHOULD do but not what it SHOULD NOT do or handle gracefully.

**Fix:** For every functional requirement, add:
- What happens with invalid input?
- What happens with empty/null data?
- What happens during concurrent access?
- What happens when an external dependency is unavailable?

---

## Trap 4: Scope Creep via Implicit Requirements

**Symptom:** "Create a user registration form" → agent adds email verification, password strength meter, CAPTCHA, social login, 2FA.

**Cause:** LLM training data includes full-featured tutorials. Without explicit scope boundaries, it builds everything it's seen.

**Fix:** Every requirement must have an explicit "Out of Scope" section:
```
IN SCOPE: Username, email, password fields. Email format validation. Password min 8 chars.
OUT OF SCOPE: Email verification, social login, 2FA, CAPTCHA, password recovery.
```

---

## Trap 5: Broken Traceability Chain

**Symptom:** 50 test files exist but nobody knows which requirement each test validates. Refactoring breaks tests and nobody knows if the behavior was required.

**Cause:** Requirements, code, and tests are created in separate contexts with no linking.

**Fix:** Use a traceability matrix (load `templates/traceability-matrix.md`):
- Each requirement has an ID
- Each test references a requirement ID in its doc comment
- CI can verify that every requirement has at least one test
