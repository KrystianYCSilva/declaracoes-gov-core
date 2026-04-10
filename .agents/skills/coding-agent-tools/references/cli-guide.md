---
name: cli-guide
description: |
  Reference for comparing AI coding CLIs and IDE assistants by workflow fit, reviewability, and control surface.
  Use when: choosing between coding assistants, comparing terminal and IDE modes,
  or deciding which tool shape fits a delivery task.
---

# AI CLI Guide & Documentation

A practical selection guide for major coding-agent products.

## Selection by Working Surface

### 1. Terminal-first with strong project memory
- **Claude Code**: strongest when you want `CLAUDE.md`, native slash commands, subagents, and governed local execution.
- **Qwen Code**: strongest when you want modular extensions, skills, sub-agents, and configurable context files.
- **OpenCode**: strongest when portability across agent ecosystems matters more than vendor lock-in.

### 2. GitHub-native delivery and review loops
- **GitHub Copilot CLI**: best when pull requests, issues, reviews, and repository policy are already centered in GitHub.
- **Codex CLI**: strong when approval modes, sandboxing, and agentic coding need to stay explicit and automatable.

### 3. Editor-first or hybrid workflows
- **Cursor**: best when scoped project rules and interactive refactoring inside the editor dominate the workflow.
- **Kimi Code CLI**: useful when fast interactive terminal work and session continuity matter, but public extensibility docs are thinner.

### 4. Headless automation and repo scripting
- **Gemini CLI**: useful when large context, structured output, search grounding, or headless scripted usage are central.
- **OpenCode** and **Codex CLI**: good fits when the repo wants agent runs from terminal or automation wrappers.

## Selection by Constraint

### 1. Need maximum portability across tools
- Prefer `AGENTS.md` as the canonical repo memory.
- Favor **OpenCode**, **Copilot CLI**, and toolchains that explicitly read shared instruction files.

### 2. Need the strongest native rule system
- Prefer **Cursor** for project rules.
- Prefer **Copilot CLI** for path-specific instruction files inside `.github/instructions/`.

### 3. Need the richest native extension surface
- Prefer **Qwen Code** or **Claude Code**.
- Use **Gemini CLI** when custom commands, settings layers, and MCP matter more than portable markdown conventions.

## Review Lens

Before standardizing on any tool, answer these four questions:

1. where do persistent instructions actually live in that tool?
2. what is the narrowest safe write surface for that tool?
3. can the team reproduce behavior with versioned files instead of chat memory?
4. which parts are vendor-native and which parts are portable across tools?

