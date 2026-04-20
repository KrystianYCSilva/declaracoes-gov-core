---
description: |
  Common traps in context engineering for AI agents.
  Use when: debugging context-related failures like hallucination, lost instructions, or token overflow.
---

# Common Context Engineering Traps

## Trap 1: Context Overload (The Complexity Paradox)

**Symptom:** More governance files → worse LLM output. Agent ignores rules that are clearly written.

**Cause:** Context window has finite capacity. When governance consumes 60%+ of the window, the actual task gets squeezed. LLMs exhibit "lost in the middle" — instructions in the center of a large context are least likely to be followed.

**Fix:**
- Root context (AGENTS.md) ≤ 150 lines (~1,500 tokens)
- JIT references loaded only when the task matches
- Total eager-loaded context ≤ 5% of model context window
- Test: if a fresh agent can't follow your context in one session, it's too much

**Evidence:** 1,803 governance files (~181K tokens) → worse results than 1 AGENTS.md + code examples across 10 sessions.

---

## Trap 2: Stale Context Overrides Reality

**Symptom:** Agent follows instructions from AGENTS.md that no longer match the actual codebase. Creates files in directories that were renamed. Uses APIs that were deprecated.

**Cause:** Context files are written once and never updated. The codebase evolves but the governance doesn't.

**Fix:**
- Context files must be updated whenever the codebase changes significantly
- Use code as context: `"Read src/model/User.java as the reference"` is always fresh
- Avoid hardcoding file paths in governance — use patterns: `"Find entities with @Entity annotation"`
- Periodic audit: every N sprints, verify AGENTS.md against actual project structure

---

## Trap 3: JIT Becomes Eager

**Symptom:** Skill directory has 35 skills. Agent spends 2,000+ tokens discovering and loading skill metadata before doing any work.

**Cause:** Skill discovery scans all SKILL.md frontmatter to decide which skills are relevant. With 35+ skills, the discovery itself becomes expensive.

**Fix:**
- Keep skill count manageable (< 20 active skills)
- Use precise `description` triggers in SKILL.md frontmatter — vague descriptions cause false positive loading
- Archive skills that are rarely used (move to `.agents/skills/archive/`)
- Measure: if skill discovery takes >5% of the first turn's token budget, you have too many skills

---

## Trap 4: Context Without Enforcement

**Symptom:** AGENTS.md says "use SLF4J for logging" but agent uses System.out.println. Rules are advisory, not enforced.

**Cause:** Context is a suggestion. The agent can choose to follow it or not. Without enforcement (CI/CD gates), compliance ranges from 0-30%.

**Fix:**
- Every critical rule in AGENTS.md must have a corresponding CI check
- Context (AGENTS.md) = what to do. CI/CD = what MUST be done.
- Example: `"Use SLF4J"` in AGENTS.md + `grep -rn "System.out.println" src/main && exit 1` in CI
- The formula: Context (advisory) + Code (example) + CI (enforcement) = reliable compliance

---

## Trap 5: Multi-Agent Context Leakage

**Symptom:** Agent A's instructions affect Agent B's behavior. Agent B follows rules meant for a different task.

**Cause:** Shared context (AGENTS.md, session files) contains instructions for multiple agent roles. Each agent reads everything and follows whatever seems relevant.

**Fix:**
- Scope context by role: each agent gets only the context relevant to its task
- Use section headers in AGENTS.md: `## For Entity Tests` / `## For Service Tests`
- In multi-agent setups, each agent should receive a focused prompt, not the full governance
- Private scratchpads for each agent's working memory (don't share raw context)
