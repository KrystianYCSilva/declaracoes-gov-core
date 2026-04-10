---
name: skills-agents-md-and-prompts
description: |
  Reference for official Codex project surfaces — AGENTS.md, /init, skills discovery, and custom subagent files.
  Use when: clarifying what is official Codex behavior vs local repository convention.
---

# Codex: AGENTS, skills, prompts, and `/init`

## What is official

- Codex uses `AGENTS.md` as its project instruction surface.
- `/init` scaffolds an `AGENTS.md` file for the current directory.
- Skills are discovered from `.agents/skills/` and user-level skill directories.
- Custom subagents live in `.codex/agents/*.toml`.

## What this repository does

- The root `AGENTS.md` is the shared cross-agent index.
- `.codex/AGENTS.md` is the Codex-specific deep guide.
- `.codex/agents/` contains project-scoped Codex subagents.
- Spec Kit prompt assets remain repo-local workflow files; do not describe them as a native Codex slash-command extension API.

## `/init` rule for this repository

- If `/init` is used for Codex here, keep the root `AGENTS.md` as the index.
- Put the detailed Codex contract in `.codex/AGENTS.md`.
- Update existing files additively; do not overwrite other CLIs' guidance.

## Prompt surface

- Natural-language prompts are the primary interface.
- Skills and subagents are the official reusable surfaces.
- Prompt files are acceptable as repo-local workflow assets when their scope is explicit.
