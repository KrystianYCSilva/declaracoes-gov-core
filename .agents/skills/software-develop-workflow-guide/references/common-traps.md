---
description: |
  Common software development workflow traps.
  Use when: diagnosing process-level failures in software delivery.
---

# Common Workflow Traps

## Trap 1: All Planning, No Building

**Symptom:** Team spends weeks on requirements, design documents, architecture diagrams. No code shipped.

**Fix:**
- Timebox each phase. Design can't exceed 20% of total project time.
- Start with a vertical slice: 1 feature end-to-end proves the architecture works.
- "The best architecture is the one that ships."

---

## Trap 2: All Building, No Planning

**Symptom:** Team starts coding immediately. Three developers build the same feature differently. Integration fails.

**Fix:**
- Minimum planning: 30 minutes of "what are we building?" before coding
- For multi-person teams: shared AGENTS.md with conventions
- For AI agents: ALWAYS provide context (requirements + design) before asking for code

---

## Trap 3: Testing Phase at the End

**Symptom:** "We'll test it at the end." End of project: no time for testing. Ship with bugs.

**Fix:**
- Tests are written WITH the code (TDD) or IMMEDIATELY AFTER
- CI runs tests on every commit — no exceptions
- Coverage gate prevents shipping untested code
- Testing is not a phase — it's a continuous activity

---

## Trap 4: Process for Process's Sake

**Symptom:** 15 documents required before writing a single line of code. Ceremony over value. Developers hate the process.

**Fix:**
- Every process step must answer: "What would go wrong if we skipped this?"
- If the answer is "nothing" — skip it
- Lightweight artifacts (ADRs, checklists) > heavy documents (50-page SDDs)
- Adapt process to project size: solo project ≠ 50-person team

---

## Trap 5: No Definition of Done

**Symptom:** Features are "almost done" for weeks. Scope changes during implementation. Nothing is ever truly finished.

**Fix:**
- Define "Done" explicitly:
  - Code written ✅
  - Tests pass ✅
  - Coverage met ✅
  - Code reviewed ✅
  - Documentation updated ✅
  - Deployed to staging ✅
- A feature is either DONE (all criteria) or NOT DONE. No "80% done."
