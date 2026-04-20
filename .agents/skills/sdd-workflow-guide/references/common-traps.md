---
description: |
  Common SDD traps and review anti-patterns.
  Use when: reviewing SDDs or debugging design document failures.
---

# Common SDD Traps

## Trap 1: SDD as Fiction (Design ≠ Implementation)

**Symptom:** SDD describes a clean architecture. Implementation is a completely different structure. SDD was written, approved, and forgotten.

**Fix:**
- SDD must be updated when implementation deviates significantly
- Include SDD path in AGENTS.md so agents reference it during implementation
- ADRs are permanent records — they document what was decided, not what should be

---

## Trap 2: No Alternatives Section

**Symptom:** SDD proposes ONE approach with no alternatives. Tunnel vision. No evidence that other approaches were considered.

**Fix:**
- Minimum 2 alternatives considered (even if briefly dismissed)
- Each alternative needs: what it is, why it was rejected
- If you can't think of alternatives, the design is too obvious for an SDD or you haven't explored enough

---

## Trap 3: Missing Non-Goals

**Symptom:** Scope creep during implementation because nobody defined what's OUT of scope. Agent adds features the SDD never mentioned.

**Fix:**
- Non-Goals section is as important as Goals
- Every Non-Goal prevents scope creep: "This design does NOT include caching"
- If it's tempting to add, it should be explicitly listed as a Non-Goal

---

## Trap 4: Over-Designed SDD

**Symptom:** 30-page SDD for a 200-line feature. Class diagrams, sequence diagrams, state machines, deployment diagrams for a CRUD endpoint.

**Fix:**
- SDD complexity should match the decision complexity
- CRUD feature: 1-page ADR is enough
- New service with 5 integrations: full SDD is warranted
- Rule: if the SDD takes longer to write than the implementation, it's over-designed

---

## Trap 5: LLM Generates Impressive But Hollow SDDs

**Symptom:** Agent produces a beautifully formatted SDD with all sections filled in. But the content is generic — could describe any system. No project-specific decisions.

**Fix:**
- Every section must reference PROJECT-SPECIFIC constraints, entities, or decisions
- If you replace the project name and the SDD still makes sense, it's too generic
- Check: does the Data Model section describe YOUR entities? Does the API section describe YOUR endpoints?
