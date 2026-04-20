---
description: |
  Common traps when adopting AI coding agent tools in teams and projects.
  Use when: evaluating, deploying, or troubleshooting AI coding assistants.
---

# Common Coding Agent Traps

## Trap 1: Tool Over-Governance

**Symptom:** Team creates 1,800+ files of governance (templates, schemas, rules) to control the AI agent. Agent performs WORSE with more rules.

**Cause:** Context overflow. The governance itself consumes the context window, leaving less room for the actual task. Agent follows governance rules inconsistently because it can't hold all of them in context.

**Fix:**
- Maximum 150 lines in AGENTS.md (the one file ALL tools eagerly load)
- Use JIT references: deep guidance lives in skill files, loaded only when needed
- Test with a fresh agent: if it can't follow your governance in a zero-context session, the governance is too complex
- The formula: simple root context + JIT deep context + CI enforcement

**Evidence:** Across 10 sessions, 1,803 governance files (~181K tokens) produced WORSE results than 1 AGENTS.md file + code examples.

---

## Trap 2: Tool-Specific Lock-In

**Symptom:** Governance works in Cursor but not in Copilot CLI. Team can't switch tools without rewriting all rules.

**Cause:** Governance uses tool-specific features (`.cursorrules`, `claude.md`, custom prompts) instead of universal formats.

**Fix:**
- Use AGENTS.md as the primary governance file — ALL major CLIs read it
- Use `.agents/skills/` for deep context — most CLIs support skill discovery
- Tool-specific files are OK for tool-specific features (MCP servers, keybindings) but not for project rules
- Test governance with at least 2 different tools before committing to a format

---

## Trap 3: Autonomous Agent Without Review Gate

**Symptom:** Agent commits 500 lines of code that compiles but doesn't match the project's patterns. Manual cleanup takes longer than writing from scratch.

**Cause:** Agent was given autonomy without a validation gate. It optimized for task completion, not for code quality.

**Fix:**
- Always have a CI gate: `mvn verify` / `npm test` / `pytest` must pass
- Coverage gate prevents agents from "completing" tasks without tests
- Use branch protection: agent works on a feature branch, human reviews before merge
- Never give agents direct push to main/master

---

## Trap 4: Building Tools to Control Tools

**Symptom:** Team builds a CLI (scanner, validator, drift detector) to govern the AI agent. The CLI itself becomes a maintenance burden. Nobody uses it because `grep` + `mvn test` is faster.

**Cause:** The governance tool is a solution looking for a problem. The real need is knowledge transfer (so agents know what to do), not runtime control (watching agents while they work).

**Fix:**
- Skills > CLIs: a skill file is read by the agent at task start (preventive). A CLI runs after the agent works (reactive).
- `AGENTS.md` + code examples + CI gate covers 90% of governance needs
- Build a CLI only when: (a) you've validated the pattern manually 3+ times, AND (b) the pattern is mechanical (regex, file moves, format conversion)

**Evidence:** A dedicated scanner CLI had 150 tests and 7 commands but was never used during 3 successful refactoring sessions. `grep` + `mvn test` did everything the CLI was supposed to do.

---

## Trap 5: Parallel Agents Without Isolation

**Symptom:** 3 agents working on the same codebase. Last one to commit overwrites the others' work. Git conflicts everywhere.

**Cause:** Agents share the same working directory and branch. Each agent reads the initial state and writes its changes, but can't see what other agents changed.

**Fix:**
- Each agent works on a separate branch (or git worktree)
- Agents operate on DISJOINT file sets (different packages/modules)
- Merge after all agents complete, resolving conflicts manually
- Never let two agents edit the same file
