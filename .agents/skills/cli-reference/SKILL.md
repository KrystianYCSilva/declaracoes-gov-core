---
name: cli-reference
description: |
  Quick-reference for AI coding CLIs: context files, skill paths, agent dirs, and official docs.
  Use when: configuring a CLI for a project, checking where skills/agents go, or finding official documentation.
activation: Auto
estimated_tokens: 940
---

## Context Files per CLI

| CLI | Primary Context File | Global Location | /init Generates |
|-----|---------------------|-----------------|-----------------|
| **GitHub Copilot** | `.github/copilot-instructions.md` | `~/.github/copilot-instructions.md` | `AGENTS.md` |
| **Claude Code** | `CLAUDE.md` (hierarchical, walks up to git root) | `~/.claude/CLAUDE.md` | `CLAUDE.md` |
| **Codex CLI** | `AGENTS.md` | `~/.codex/config.toml` | `AGENTS.md` |
| **Gemini CLI** | `GEMINI.md` (global → workspace → JIT) | `~/.gemini/GEMINI.md` | `GEMINI.md` |
| **Qwen Code** | `QWEN.md` (configurable via `context.fileName`) | `~/.qwen/QWEN.md` | `QWEN.md` |
| **Cursor** | `.cursor/rules/` | `~/.cursor/rules/` | No `/init` |
| **Kimi Code** | `AGENTS.md` | N/A | `AGENTS.md` |
| **OpenCode** | `AGENTS.md` > `CLAUDE.md` (precedence) | `~/.config/opencode/` | `AGENTS.md` |
| **Junie** | `.junie/AGENTS.md` | N/A | `JUNIE.md` |
| **Mistral Vibe** | `AGENTS.md` | `~/.vibe/` | No `/init` |
| **CodeBuddy** | `CODEBUDDY.md` | N/A | `CODEBUDDY.md` |

## Skill Directories per CLI

| CLI | Skills Dir | Commands Dir | Variable Syntax |
|-----|-----------|-------------|-----------------|
| **GitHub Copilot** | `.github/skills/` | `.github/commands/` | `$ARGUMENTS` |
| **Claude Code** | `.claude/skills/` | `.claude/commands/` | `$ARGUMENTS` |
| **Codex CLI** | `.codex/skills/` | `.codex/prompts/` | `$ARGUMENTS` |
| **Gemini CLI** | `.gemini/skills/` | `.gemini/commands/` | `{{args}}` |
| **Qwen Code** | `.qwen/skills/` | `.qwen/commands/` | `{{args}}` |
| **Cursor** | `.cursor/skills/` | `.cursor/commands/` | `$ARGUMENTS` |
| **OpenCode** | `.opencode/skills/` | `.opencode/command/` | `$ARGUMENTS` |

**Global skills**: All CLIs also search `~/.agents/skills/` (JIT loaded).

## Agent Directories per CLI

| CLI | Agent Location | Format |
|-----|---------------|--------|
| **Claude Code** | `.claude/agents/`, `~/.claude/agents/` | Markdown + YAML frontmatter |
| **Codex CLI** | `.codex/agents/` | TOML |
| **Copilot** | `.github/agents/` | `.agent.md` (Markdown + YAML) |
| **Gemini CLI** | `.gemini/agents/`, `~/.gemini/agents/` | Markdown + YAML |
| **Qwen Code** | `.qwen/agents/`, `~/.qwen/agents/` | Markdown or JSON |
| **Cursor** | Via `.cursor/rules/` | Rules-based |
| **Vibe** | `~/.vibe/agents/` | Markdown + YAML |

## Key Behavioral Differences

- **Claude Code**: Subagents CANNOT recurse (no chaining). Worktree isolation available.
- **Codex CLI**: Max 6 concurrent subagents. Sandbox via Seatbelt/bubblewrap.
- **Copilot CLI**: `/fleet` for parallel subagent execution. Explore/Task/Review/Plan agents built-in.
- **Kimi Code**: Agent Swarm — up to 100 parallel subagents, 300 tool-call steps.
- **Qwen Code**: Most modular — extensions bundle commands + skills + agents + MCP.

## Official Documentation

| CLI | Docs URL |
|-----|---------|
| Claude Code | https://code.claude.com/docs/ |
| Copilot CLI | https://docs.github.com/en/copilot/how-tos/copilot-cli |
| Codex CLI | https://github.com/openai/codex |
| Gemini CLI | https://geminicli.com/docs/ |
| Qwen Code | https://qwenlm.github.io/qwen-code-docs/ |
| Cursor | https://docs.cursor.com/ |
| Kimi Code | https://www.kimi.com/code/docs/ |
| OpenCode | https://opencode.ai/docs/ |
| Junie | https://junie.jetbrains.com/docs/ |
| Vibe | https://docs.mistral.ai/mistral-vibe/ |

Load `references/common-traps.md` for CLI configuration pitfalls.
