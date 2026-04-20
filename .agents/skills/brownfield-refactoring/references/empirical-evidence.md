---
description: |
  Empirical evidence and failed approaches from 10 sessions of LLM-assisted development.
  Use when: evaluating AI governance tools, choosing between simple vs complex approaches,
  or justifying why less governance produces better results.
---

# What Works vs What Failed (Empirical Evidence)

This is a meta-reference — it documents WHAT WAS TRIED and WHY IT FAILED so you don't repeat the same mistakes.

## The Formula (Validated)

```
AGENTS.md (context) + Code (example) + CI/CD (enforcement) = quality code
```

- AGENTS.md (35-150 lines) → LLM reads every session, knows the conventions
- One working vertical slice → LLM copies the pattern, doesn't invent
- `mvn verify` / `npm test` with coverage gate → 100% enforcement, not advisory

## Empirical Results

| Approach | Context Size | LLM Compliance | Result |
|----------|-------------|-----------------|--------|
| 1 AGENTS.md (35 lines) + 1 example | ~500 tokens | 95.8% | ✅ Fresh agent, 160 tests |
| 1 AGENTS.md (150 lines) + templates | ~1,500 tokens | 70-80% | ✅ 3 projects, 1,582 tests |
| 173 governance files | ~7,400 tokens | 0-30% | ❌ LLM ignores rules |
| 20-state machine + 6 quality gates | ~3,000 tokens | <20% | ❌ LLM can't maintain transitions |
| Constitution + kernel + memory files | ~5,000 tokens | <30% | ❌ Overhead > benefit |
| 4,000+ JIT skills | ~200K discovery | N/A | ❌ Token discovery overhead |

## What Doesn't Work (Tested and Failed)

1. **More governance files = worse output** — 1,803 files (~181K tokens) produced worse results than 1 file
2. **State machines for LLMs** — 20-state machine is impossible for LLMs to maintain across turns
3. **CLI tools for governance** — A dedicated scanner CLI (150 tests, 7 commands) was never used in actual work
4. **Constitutional rules** — T0 rules are only followed when they're also enforced by CI
5. **Parallel agents on same files** — last agent overwrites all prior work
6. **Delegation without "read first"** — 100% name fabrication rate when agents create tests without reading source
7. **Script-based fixes > agent fixes** — System.out→SLF4J: script = 5 seconds, agent = 42 minutes + errors

## Why CI/CD > Rules

| Mechanism | Compliance Rate | Why |
|-----------|----------------|-----|
| Rule in AGENTS.md | 0-30% | Advisory — agent can ignore |
| Rule + example code | 60-80% | Better — agent copies pattern |
| CI gate (`mvn test` fails) | 100% | Mandatory — can't merge without passing |
| JaCoCo coverage threshold | 100% | Build fails if coverage drops |

## Tools Substitution Table

| Governance Tool | Simpler Substitute |
|----------------|-------------------|
| Full governance CLI (173 templates) | 1 AGENTS.md (150 lines) |
| Dedicated scanner CLI | `grep -rn "@Entity" src/` |
| facts.md generator | `find src/ -name "*.java" \| wc -l` |
| Boundary classifier | AGENTS.md section: "Don't touch these packages" |
| State machine validator | `mvn test` (pass = valid state, fail = invalid) |
| Token counter CLI | SKILL.md < 100 lines ≈ 1,500 tokens (rule of thumb) |

## Project Outcomes

| Project | Entities | Tests Before | Tests After | Coverage | Approach Used |
|---------|----------|-------------|-------------|----------|---------------|
| SGE | 10 | 104 | 573 | 70.0% | AGENTS.md + templates + TDD |
| PDI | 37 | 4 | 428 | 70.9% | Same (replicated from SGE) |
| SGCEX | 57 | 135 | 581 | 73.1% | Same (replicated from SGE) |
| npd-service | 8 | 0 | 160 | 95.8% | 35-line AGENTS.md + 1 example |
