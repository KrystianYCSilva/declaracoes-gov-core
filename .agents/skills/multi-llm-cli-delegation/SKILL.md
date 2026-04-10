---
name: multi-llm-cli-delegation
description: |
  Invoke external LLM CLIs from chat with explicit authorization, narrow scope, and post-run validation.
  Use when: delegating work to another terminal LLM, selecting the right CLI for a bounded task,
  or enforcing a safe multi-CLI orchestration flow.
---

# Multi-LLM CLI Delegation

Use this skill to orchestrate external CLIs safely. It does not replace repository governance or final human review.

## How to Open a Delegation

- require explicit user approval before invoking another CLI
- capture objective, target CLI or model, file scope, and autonomy level
- choose the smallest permission set that can still complete the task
- keep review-only requests read-only whenever the target CLI supports it

## How to Pick the Right CLI

- start with the cheapest model that can realistically succeed
- escalate only after a concrete validation failure
- use `references/cli-reference.md` for safe command shapes and routing hints
- use `references/sources.md` when a flag, model id, or capability needs revalidation

## How to Inject Context

- pass raw artifacts and exact output expectations
- load file contents into variables first; do not inline shell substitutions into prompt strings
- include only the context needed for the delegated subtask

## How to Validate Output

- run syntax, build, or lint checks as appropriate
- run targeted tests for the touched scope
- check for secrets, credential leakage, and repository-fit issues
- review the result locally before integrating or declaring success

## How to Recover

1. retry once with the exact failure
2. escalate model tier or CLI if justified
3. split the task into a smaller atomic unit

After the third failure, stop delegating that prompt shape and return control to the main agent or the user.

## How to Stay Safe

- external CLIs may draft code, tests, or analysis, but they do not own final integration
- keep repository instructions, release decisions, and closure in the main workflow
- prefer official docs over vendor blog posts when behavior is uncertain
- use `references/sources.md` for the authoritative vendor documentation index and per-CLI links
