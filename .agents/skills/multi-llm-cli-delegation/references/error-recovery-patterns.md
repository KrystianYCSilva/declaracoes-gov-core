---
description: |
  Error recovery and fallback patterns for multi-LLM delegation.
  Use when: an LLM CLI fails and you need to decide whether to retry, escalate, or fall back.
---

# Error Recovery Patterns

## Decision Tree

```
Task failed?
├── Compilation error?
│   ├── Agent fabricated names → DELETE output, retry with "READ source files first"
│   └── Missing dependency → Add to pom.xml/package.json, retry
├── Test failure?
│   ├── Wrong assertion → Fix assertion (agent often inverts expected/actual)
│   └── Runtime error → Check for missing test infrastructure (H2, mock config)
├── Timeout (agent ran > 20 min)?
│   ├── Agent in repair loop → ABORT, simplify the task, retry with smaller scope
│   └── Legitimate complexity → Split into sub-tasks, delegate each separately
├── Context overflow?
│   ├── Too much governance loaded → Reduce AGENTS.md to < 150 lines
│   └── Codebase too large for one prompt → Use phased approach (one package at a time)
└── Quality issue (code works but is bad)?
    ├── No tests → Reject, re-prompt with "tests are mandatory"
    ├── Wrong patterns → Provide explicit example file to follow
    └── Over-engineered → Add "KISS: no abstractions until 3+ concrete implementations"
```

## Model Tier Escalation

When a task fails with the current model, consider escalating:

| Tier | Models (examples) | Best For | Token Cost |
|------|-------------------|----------|------------|
| Fast | GPT-4o-mini, Haiku, Gemini Flash | Simple edits, search, file ops | $ |
| Standard | GPT-4o, Sonnet, Gemini Pro | Code generation, refactoring, testing | $$ |
| Premium | o1, Opus, Gemini Ultra | Complex reasoning, architecture, debugging | $$$$ |

**Escalation rules:**
1. Start at Standard tier for code tasks
2. Escalate to Premium only if Standard fails 2x on the same task
3. Never use Premium for mechanical tasks (regex, file moves, format conversion)
4. Fast tier is fine for: grep, file reading, simple edits, test running

## Retry Strategy

| Failure Type | Max Retries | Between Retries |
|-------------|-------------|-----------------|
| Compilation error | 2 | Add "read the actual source file first" |
| Test failure | 2 | Provide the error output as context |
| Timeout | 1 | Split task into smaller pieces |
| Quality issue | 1 | Add explicit example + constraints |
| Context overflow | 0 | Restructure approach (no retry will fix this) |

## Fallback: Script > Agent

For mechanical, deterministic tasks, use a script instead of an LLM:

| Task | Agent Time | Script Time | Winner |
|------|-----------|-------------|--------|
| System.out → SLF4J | 42 min, 69 tool calls, errors | 5 seconds, regex | Script |
| Find all entities | 3 min, read every file | 1 second, grep | Script |
| Add import to all files | 8 min, some files missed | 2 seconds, sed | Script |
| Write business logic tests | — | Can't do this | Agent |
| Refactor complex method | — | Can't do this | Agent |

**Rule:** If a task can be expressed as a regex or find/replace, use a script. If it requires understanding code semantics, use an agent.
