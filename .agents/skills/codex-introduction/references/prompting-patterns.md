---
name: prompting-patterns
description: |
  Reference for Codex prompting patterns, distinguishing end-user CLI prompts from harness-level developer prompts.
  Use when: crafting effective Codex CLI prompts or tuning agent harness and tool-use behavior.
---

# Prompting patterns

## First distinction: end-user prompt vs harness prompt

The official Codex Prompting Guide is mainly about building or tuning a Codex
harness, not just about what a normal CLI user should type day to day.

Use that guide for:

- developer prompts;
- system/developer instruction layers;
- agent harness design;
- tool-use and autonomy tuning.

Use normal CLI prompts for:

- asking Codex to inspect, explain, edit, test, or refactor something in the
  current repository.

## Good day-to-day CLI prompts

Prefer prompts with:

- one clear goal;
- scope boundaries;
- explicit files or directories when known;
- constraints and non-goals;
- a request for a plan first when the task is risky.

Good:

```text
Read src/specify_cli and explain the init flow. Do not edit anything yet.
```

Good:

```text
Add documentation for the release pipeline in .architecture. Reuse the existing
style and do not modify runtime scripts.
```

Good:

```text
Review my working tree for regressions and missing tests. Focus on findings,
not summaries.
```

## Prompt file guidance

If you want reusable prompts in a repository, keep them:

- narrow;
- task-shaped;
- tied to concrete workflow outcomes;
- explicit about scripts, files, and expected outputs.

That is the pattern used here by `.codex/prompts/speckit.*.md`.

## Guidance from the official Codex Prompting Guide

Key takeaways from the official guide:

- strong Codex setups emphasize autonomy, persistence, codebase exploration,
  tool use, and quality guardrails;
- reuse fast search/read/edit tools and parallelize independent reads;
- bias toward concrete progress instead of stopping on avoidable clarifications;
- preserve codebase conventions and behavior safety;
- keep edits coherent and avoid noisy micro-edits.

Important nuance:

- the prompting guide warns harness builders not to over-instruct the model to
  emit preambles or upfront status chatter if that causes the rollout to stop
  early;
- that is guidance for prompt/harness design, not a reason to avoid asking
  Codex for a plan when you explicitly want one in an interactive CLI session.
