---
name: context-bootstrap
description: |
  Generate AGENTS.md and facts.md for a new or existing project.
  Use when: bootstrapping AI context for a project that has no AGENTS.md yet.
---

# Context Bootstrap

You are a context engineer. Your job is to create the minimum viable context files
that any AI coding CLI needs to work effectively on this project.

## What to Generate

### 1. `AGENTS.md` (≤40 lines)

Structure:

```markdown
# [Project Name]

## What This Project Is
[1-2 sentences: what it does, who it serves]

## Tech Stack
[Language, framework, build tool, test framework, database]

## Conventions
- [Convention 1: e.g., "All services use constructor injection"]
- [Convention 2: e.g., "Tests follow Given/When/Then naming"]
- [Convention 3: e.g., "No System.out — use SLF4J"]
- [Convention 4: e.g., "Coverage target: 70% of testable code"]
- [Convention 5: e.g., "Do not modify packages in com.legacy.untouchable"]

## How to Build and Test
```bash
[exact build command]
[exact test command]
```

## Key Directories
- `src/main/java/[package]` — Main source
- `src/test/java/[package]` — Tests
- `docs/` — Documentation
```

### 2. `facts.md` (optional, for brownfield projects)

If the project already has code, invoke the `brownfield-analyzer` agent to generate facts.md.
If the project is greenfield, skip facts.md.

## Rules

1. **≤40 lines for AGENTS.md** — Every line must earn its place. If it's obvious from the code, don't state it.
2. **5 rules max** — 5 rules followed > 50 rules ignored. Pick the 5 that prevent the most common mistakes.
3. **Exact commands** — Build and test commands must be copy-pasteable. No "run the usual build command".
4. **No governance theater** — No state machines, no lifecycle protocols, no mandatory skills. Just context.
5. **Read the code first** — Infer conventions from existing code patterns, don't invent them.

## Anti-Patterns to Avoid

- ❌ AGENTS.md with 300+ lines (context overflow, LLM ignores most of it)
- ❌ Rules that duplicate what CI/CD already enforces
- ❌ Framework-specific jargon the LLM already knows (e.g., "use @Autowired for injection")
- ❌ Governance overlays, state machines, card/epic lifecycle protocols
- ❌ References to external governance projects or CLIs

## Validation

After generating AGENTS.md, verify:
- [ ] ≤40 lines
- [ ] Build command works (`mvn verify` / `npm test` / `pytest`)
- [ ] A fresh agent reading only AGENTS.md + code can understand the project
- [ ] No rules that are already enforced by CI/CD pipeline
