---
name: copilot-cli-introduction
description: |
  Guide GitHub Copilot CLI usage, customization surfaces, and repository integration with JIT references.
  Use when: onboarding to Copilot CLI, explaining `/init`, custom agents, skills,
  instruction files, MCP, or locating the right official GitHub documentation.
---

# Copilot CLI Introduction

Use this skill as the entry layer for GitHub Copilot CLI, not as a monolithic manual.

## How to Navigate This Skill

- `references/slash-commands.md`: command catalog and session control
- `references/agent-profile-spec.md`: `.agent.md` structure and agent behavior
- `references/skill-spec.md`: `SKILL.md` structure and skill usage
- `references/custom-instructions.md`: `copilot-instructions.md`, path-specific instructions, and `AGENTS.md`
- `references/mcp-servers.md`: MCP setup and GitHub tooling
- `references/sources.md`: official GitHub and Agent Skills links

## How to Model Copilot CLI

- use `.github/copilot-instructions.md` for always-on repo guidance
- use the root `AGENTS.md` when the repository wants cross-agent conventions
- use custom agents for specialized toolsets or personas
- use skills for deep, on-demand procedures

## How to Answer Common Requests

- command or mode question -> `references/slash-commands.md`
- custom agent question -> `references/agent-profile-spec.md`
- skill authoring question -> `references/skill-spec.md`
- instruction or `/init` question -> `references/custom-instructions.md`
- MCP or GitHub server question -> `references/mcp-servers.md`

## How to Stay Accurate

- prefer `docs.github.com` over third-party summaries
- if a feature is version-sensitive, verify it against `references/sources.md`
- load only the reference file needed for the current ask
