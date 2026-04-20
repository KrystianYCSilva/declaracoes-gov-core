# CLI Directory Mapping for Skill Distribution

> Source: CLI directory mapping validated across 7 stable CLIs

## Stable CLIs — Directory Structure

| CLI | Skills Dir | Commands Dir | Variable Syntax |
|-----|-----------|-------------|-----------------|
| **GitHub Copilot** | `.github/skills/` | `.github/commands/` | `$ARGUMENTS` |
| **Claude Code** | `.claude/skills/` | `.claude/commands/` | `$ARGUMENTS` |
| **Gemini CLI** | `.gemini/skills/` | `.gemini/commands/` | `{{args}}` |
| **Codex CLI** | `.codex/skills/` | `.codex/prompts/` | `$ARGUMENTS` |
| **Cursor Agent** | `.cursor/skills/` | `.cursor/commands/` | `$ARGUMENTS` |
| **Qwen Code** | `.qwen/skills/` | `.qwen/commands/` | `{{args}}` |
| **OpenCode** | `.opencode/skills/` | `.opencode/command/` | `$ARGUMENTS` |

## Context Files per CLI

| CLI | Primary Context File | Global Location |
|-----|---------------------|-----------------|
| GitHub Copilot | `AGENTS.md` / `.github/copilot-instructions.md` | `~/.github/copilot-instructions.md` |
| Claude Code | `CLAUDE.md` | `~/.claude/CLAUDE.md` |
| Gemini CLI | `GEMINI.md` | `~/.gemini/GEMINI.md` |
| Codex CLI | `AGENTS.md` | N/A |
| Cursor Agent | `.cursor/rules/` | `~/.cursor/rules/` |
| Qwen Code | `QWEN.md` | `~/.qwen/QWEN.md` |
| OpenCode | `AGENTS.md` | N/A |

## How to distribute a skill to multiple CLIs

1. Write the canonical skill in `~/.agents/skills/<name>/SKILL.md`
2. For each target CLI, copy to its `skills_dir`:
   ```
   cp -r ~/.agents/skills/<name> ./<cli-skills-dir>/<name>
   ```
3. If the CLI uses `{{args}}` syntax (Gemini, Qwen), replace `$ARGUMENTS` in templates
4. Skills are JIT-loaded by all CLIs — no need to copy unless the project needs offline access

## Key Insight

All CLIs converged on the same pattern: `SKILL.md` + `references/` + `templates/`.
The only differences are directory paths and variable syntax. A skill written once
works everywhere with minimal adaptation.
