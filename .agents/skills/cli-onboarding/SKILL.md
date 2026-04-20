---
name: cli-onboarding
description: |
  Guide onboarding to any AI coding CLI with setup, customization, and best practices.
  Use when: setting up a new CLI for a project, migrating between CLIs, or optimizing CLI configuration.
activation: Manual
estimated_tokens: 1480
---

# CLI Onboarding Guide

This skill helps you configure and optimize any AI coding CLI for a project.
For quick reference tables (paths, dirs, syntax), see the `cli-reference` skill.
This skill focuses on **how to set up and use each CLI effectively**.

## How to Onboard a New CLI to a Project

### Step 1: Initialize Context

Run `/init` (if supported) to generate the CLI's context file, then **manually review and edit** it.

| CLI | Command | Generated File | Max Recommended Size |
|-----|---------|---------------|---------------------|
| Copilot | `/init` | `AGENTS.md` | 40 lines |
| Claude | `/init` | `CLAUDE.md` | 40 lines |
| Codex | `/init` | `AGENTS.md` | 40 lines |
| Gemini | `/init` | `GEMINI.md` | 40 lines |
| Qwen | `/init` | `QWEN.md` | 40 lines |
| Cursor | N/A | `.cursor/rules/*.mdc` | 40 lines per rule |

### Step 2: Configure Skills

Skills are loaded from two locations:
1. **Global**: `~/.agents/skills/` (shared across all CLIs)
2. **Project-local**: `.{cli}/skills/` (CLI-specific, per project)

To distribute global skills to a specific CLI's native format:
```bash
python ~/.agents/scripts/compile_skills.py --clis [cli-name]
```

### Step 3: Set Up CI/CD Gates

The most effective governance is external to the LLM:
```yaml
# Minimum viable CI gate
- run: mvn verify       # or: npm test / pytest
- run: check coverage threshold
```

## Per-CLI Setup Guides

### GitHub Copilot CLI
- **Context**: `AGENTS.md` (root) + `.github/copilot-instructions.md`
- **Config**: `~/.copilot/config.json` — set `skillDirectories`, `model`, `trusted_folders`
- **Skills**: `.github/skills/` (project) or `~/.agents/skills/` (global via config)
- **Agents**: `.github/agents/` — Markdown with YAML frontmatter
- **Built-in agents**: explore (fast/cheap), task (verbose output), general-purpose (full capability), code-review
- **Key feature**: `/fleet` for parallel subagent execution
- **Best for**: Complex multi-step tasks, code review, planning

### Claude Code
- **Context**: `CLAUDE.md` (hierarchical — walks up to git root)
- **Config**: `~/.claude/settings.json` — permissions, model, allowed tools
- **Skills**: `.claude/skills/` with `$ARGUMENTS` syntax
- **Agents**: `.claude/agents/` — cannot recurse (no subagent chaining)
- **Key feature**: Worktree isolation for safe parallel work
- **Best for**: Deep reasoning, refactoring, architecture decisions

### Codex CLI
- **Context**: `AGENTS.md` — same format as Copilot
- **Config**: `~/.codex/config.toml` — model, reasoning effort, approval mode
- **Skills**: `.codex/skills/` with `$ARGUMENTS` syntax
- **Rules**: `~/.codex/rules/default.rules` — shell command allowlists
- **Key feature**: Sandbox via Seatbelt/bubblewrap, max 6 concurrent subagents
- **Best for**: Autonomous execution with safety guardrails

### Gemini CLI
- **Context**: `GEMINI.md` (cascading: global -> workspace -> JIT)
- **Config**: `~/.gemini/settings.json`
- **Skills**: `.gemini/skills/` with `{{args}}` syntax (different from most CLIs!)
- **Key feature**: 1M+ token context window, multi-modal (images, audio)
- **Caveat**: Context degradation after ~1500 lines of output; use `/compact` aggressively
- **Best for**: Large file analysis, documentation, broad codebase understanding

### Qwen Code
- **Context**: `QWEN.md` (configurable filename via `context.fileName`)
- **Config**: `~/.qwen/settings.json`
- **Skills**: `.qwen/skills/` with `{{args}}` syntax
- **Key feature**: Most modular — extensions bundle commands + skills + agents + MCP
- **Caveat**: Tends toward academic/theoretical output; steer toward code with explicit instructions
- **Best for**: Documentation generation, research, analysis

### Cursor
- **Context**: `.cursor/rules/*.mdc` — YAML frontmatter with activation modes
- **Skills**: `.cursor/skills/` with `$ARGUMENTS` syntax
- **Commands**: `.cursor/commands/` — custom slash commands
- **Key feature**: IDE-integrated, visual diff, auto-apply
- **Caveat**: No `/init` command; configure manually
- **Best for**: IDE-based development, quick edits, visual code review

### Kimi Code
- **Context**: `AGENTS.md`
- **Config**: `~/.kimi/config.toml` — thinking mode, max steps, reserved context
- **Key feature**: Agent Swarm — up to 100 parallel subagents, 300 tool-call steps
- **Caveat**: Streaming timeouts on long sessions; rate limiting (HTTP 429)
- **Best for**: Parallel analysis, broad codebase scanning

### OpenCode
- **Context**: `AGENTS.md` > `CLAUDE.md` (reads both, AGENTS.md takes precedence)
- **Skills**: `.opencode/skills/` with `$ARGUMENTS` syntax
- **Key feature**: Native CLAUDE.md fallback — easiest migration from Claude Code
- **Best for**: Teams transitioning from Claude Code

### Other CLIs (Junie, Vibe, CodeBuddy)
- **Junie**: `.junie/AGENTS.md` — JetBrains native, checkpoint/rollback support
- **Vibe**: `AGENTS.md` + `~/.vibe/config.toml` — Mistral models, local LLM support
- **CodeBuddy**: `CODEBUDDY.md` — team coordination with Agent Teams pattern

## Key Principles (Validated Empirically)

1. **40 lines max** for any context file — beyond that, LLMs start ignoring content
2. **CI/CD > Prompts** — 5 lines of YAML enforce more than 358 lines of instructions
3. **Code IS context** — a fresh agent reading existing code achieves 95.8% compliance
4. **5 rules followed > 50 rules ignored** — pick the 5 that prevent the most damage
5. **Compile, don't duplicate** — use `compile_skills.py` to distribute skills across CLIs

Load `references/migration-guide.md` for CLI-to-CLI migration patterns.
