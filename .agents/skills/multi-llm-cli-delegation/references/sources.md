---
name: sources
description: |
  Official vendor documentation for terminal LLM CLIs used in delegation workflows. Use when: choosing or invoking a CLI agent for a bounded task.
Last verified: 2025-07-09
---
# Sources

Official vendor documentation for terminal-executable LLM CLIs used in delegation workflows.
For operational invocation patterns, read `cli-reference.md`.

Links last revalidated: 2026-04-05.

## Primary Delegation CLIs

- GitHub Copilot CLI command reference:
  `https://docs.github.com/en/copilot/reference/copilot-cli-reference/cli-command-reference`
- GitHub Copilot CLI customization and feature comparison:
  `https://docs.github.com/en/copilot/concepts/agents/copilot-cli/comparing-cli-features`
- Claude Code CLI overview:
  `https://docs.anthropic.com/en/docs/claude-code/overview`
- Claude Code alternate docs:
  `https://code.claude.com/docs/en/overview`
- Claude Code slash commands:
  `https://code.claude.com/docs/en/slash-commands`
- Claude Code headless / non-interactive usage:
  `https://docs.anthropic.com/en/docs/claude-code/how-to-guides`
- Gemini CLI official docs:
  `https://google-gemini.github.io/gemini-cli/docs/cli/commands.html`
- Gemini CLI GitHub repository:
  `https://github.com/google-gemini/gemini-cli`
- OpenAI Codex CLI:
  `https://developers.openai.com/codex`
- OpenAI Codex slash commands:
  `https://developers.openai.com/codex/cli/slash-commands`
- OpenAI Codex sandbox and approval modes:
  `https://developers.openai.com/codex/guides/getting-started`
- OpenCode rules and init:
  `https://opencode.ai/docs/rules/`
- OpenCode commands:
  `https://opencode.ai/docs/commands/`
- Qwen Code terminal usage:
  `https://qwenlm.github.io/qwen-code-docs/en/users/features/commands/`
- Qwen Code GitHub repository:
  `https://github.com/QwenLM/qwen-code`
- Cursor rules and context:
  `https://docs.cursor.com/context/rules`
- Cursor CLI reference:
  `https://docs.cursor.com/en/cli`
- Kimi Code CLI getting started:
  `https://www.kimi.com/code/docs/en/kimi-cli/guides/getting-started.html`
- Kimi Code CLI sessions:
  `https://www.kimi.com/code/docs/en/kimi-cli/guides/sessions.html`

## IDE-Integrated (Not Primary Headless Delegation)

- Windsurf Command/Cascade:
  `https://docs.windsurf.com/windsurf/cascade/commands`
  IDE command workflow; not a standalone headless delegation CLI.

## Safety and Authorization References

- OWASP AI security guidelines:
  `https://owasp.org/www-project-machine-learning-security-top-10/`
- GitHub Copilot trust and safety documentation:
  `https://docs.github.com/en/copilot/responsible-use-of-github-copilot-features`

## Usage Notes

- Prefer official vendor docs over third-party blogs for safe invocation patterns.
- CLI flags and model catalogs change frequently; always validate before scripting governance flows.
- When vendor docs conflict with the installed CLI's `--help`, treat `--help` as the runtime truth for the installed version.
- When documenting safe usage for this repository, translate vendor guidance through the repository's governance files.
