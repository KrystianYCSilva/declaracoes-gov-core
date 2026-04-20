---
description: |
  Common documentation traps with LLM agents.
  Use when: reviewing documentation quality or debugging doc maintenance failures.
---

# Common Documentation Traps

## Trap 1: LLM-Generated Documentation Sounds Right But Says Nothing

**Symptom:** README says "This project provides a robust, scalable, enterprise-grade solution for managing student data." — no specifics, no commands, no examples.

**Cause:** LLMs generate fluent, confident prose. Without specific constraints, they produce marketing copy, not technical documentation.

**Fix:**
- Every doc section must have at least one CONCRETE element: a command, a code snippet, or a specific number
- Test: if you remove all adjectives (robust, scalable, enterprise-grade) and the sentence still works, the adjectives were filler
- README must have copyable commands: `mvn clean install` not "run the build tool"

---

## Trap 2: Documentation Diverges from Code

**Symptom:** README says "Run `npm start`" but the project uses `yarn dev`. API docs show v1 endpoints but v2 is deployed.

**Cause:** Documentation is written once and never updated. Code evolves independently.

**Fix:**
- Include doc updates in the Definition of Done
- CI check: verify README commands actually work (script that extracts and runs shell commands)
- API docs auto-generated from annotations (Swagger/OpenAPI) stay in sync automatically
- AGENTS.md rule: "When modifying a feature, update README and CHANGELOG"

---

## Trap 3: Over-Documentation (Everything is Documented, Nothing is Useful)

**Symptom:** 500 pages of documentation. Nobody reads it. New developers still ask the same questions.

**Cause:** Documentation was written for completeness, not for usefulness. Every class has Javadoc, but the "how to run the project" is buried on page 347.

**Fix:**
- README answers 5 questions (WHAT, WHY, HOW, TEST, MORE) — nothing else
- Javadoc only on public APIs and non-obvious methods
- Remove generated docs that nobody reads (default Javadoc for getters/setters)
- Measure: if a doc hasn't been viewed/updated in 6 months, consider deleting it

---

## Trap 4: Comments as Code Smell Cover

**Symptom:** Code has 50% comments explaining what each line does. The code itself is unreadable.

**Cause:** Instead of refactoring complex code, comments are added to explain it.

**Fix:**
- If code needs a comment to explain WHAT it does → refactor the code
- Comments explain WHY (business context), code explains WHAT (implementation)
- Long method with 10 comments → extract methods with descriptive names
- Exception: regex, bitwise operations, and performance hacks always need comments

---

## Trap 5: No Changelog Discipline

**Symptom:** Changelog is empty or contains entries like "Bug fixes and improvements." Users can't tell what changed between versions.

**Fix:**
- Every PR must include a changelog entry (enforce via PR template)
- Use Keep a Changelog format (Added, Changed, Fixed, Removed)
- Link to issue/PR numbers for traceability
- NEVER: "Various bug fixes" — list each fix specifically
