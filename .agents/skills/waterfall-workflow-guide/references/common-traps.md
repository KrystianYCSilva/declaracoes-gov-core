---
description: |
  Common waterfall traps, especially with LLM-assisted development.
  Use when: reviewing waterfall process execution or debugging phase gate failures.
---

# Common Waterfall Traps

## Trap 1: Requirements Phase Never Ends

**Symptom:** Team spends months perfecting requirements. Requirements document grows to 200 pages. No code written.

**Cause:** Fear of missing something. Each review reveals new edge cases. Analysis paralysis.

**Fix:**
- Set a hard timebox for requirements phase (e.g., 2-4 weeks)
- Accept "good enough" requirements with change control for discovered gaps
- Distinguish MUST-HAVE (P0) from SHOULD-HAVE (P1) — freeze P0 only
- LLM can help: "Given these requirements, what's missing for a complete CRUD feature?"

---

## Trap 2: Design Divorced from Implementation

**Symptom:** Beautiful 50-page SDD. Implementation doesn't match because the design made assumptions that don't hold in practice.

**Fix:**
- Prototype critical components during design phase (spike)
- Design should reference specific libraries/frameworks with versions
- LLM can validate: "Given this design, write a skeleton implementation and identify gaps"
- Update SDD when implementation reveals design flaws

---

## Trap 3: Testing as Afterthought

**Symptom:** Implementation takes 80% of the schedule. Testing is squeezed into the remaining 20%. Defects found late are expensive.

**Fix:**
- Write test SPECIFICATIONS during the design phase (what to test, not how)
- Write test INFRASTRUCTURE during implementation (TDD)
- The testing phase validates acceptance criteria, not unit-level behavior
- LLM generates test cases from requirements during Phase 1, not Phase 4

---

## Trap 4: No Change Control

**Symptom:** Requirements are "frozen" but stakeholders keep asking for changes. Changes are implemented informally. Scope creeps silently.

**Fix:**
- Every change goes through the change impact form (see `requirements-engineering` skill)
- Changes after freeze require formal approval and impact assessment
- Track changes: what changed, why, who approved, what's affected
- LLM can help: "Given this change request, what tests, code, and documentation need updating?"

---

## Trap 5: LLM Skips Phases

**Symptom:** You ask the LLM to "implement the login feature." It writes code immediately without asking about requirements or design.

**Cause:** LLMs are biased toward action. They want to produce code, not documents.

**Fix:**
- Prompt by phase: "We are in Phase 1 (Requirements). Do NOT write code. Write the requirements specification for login."
- Gate check before each phase: "Phase 1 deliverables: [checklist]. Are all approved? → If not, stay in Phase 1"
- In AGENTS.md: `RULE: Always confirm the current phase before acting. Requirements → Design → Implementation → Testing.`
