---
name: agents
description: |
  Reference for Kimi CLI built-in agents and custom agent configuration via YAML agent files.
  Use when: selecting a Kimi agent, creating custom agents, or explaining the --agent-file format.
---

# Kimi CLI agents and subagents

## What is officially confirmed

- `kimi --agent default|okabe` selects a built-in agent.
- `kimi --agent-file <file>` loads a custom agent specification.
- Custom agent files are YAML-based.
- The public CLI help confirms `--agent-file`; public docs and help are thinner than Qwen or Claude, so stay conservative.

## Custom agent YAML structure

```yaml
version: 1
agent:
  name: my-agent-name
  extend: default
  system_prompt_path: ./my-agent.system.md
```

- `extend: default` inherits the default agent's tool set.
- `system_prompt_path` is relative to the YAML file location.

## Repository convention

- `.kimi/AGENTS.md` is the repository-specific Kimi guidance file.
- `.kimi/agents/speckit-orchestrator.yaml` is the project custom agent.
- `.kimi/agents/speckit-orchestrator.system.md` contains the system prompt.
- Keep the root `AGENTS.md` as the shared cross-agent index.

## Guidance

- Treat Kimi custom agents as a real surface only where the current CLI help and official docs support it.
- Do not over-claim undocumented lifecycle commands or auto-discovery behavior.
