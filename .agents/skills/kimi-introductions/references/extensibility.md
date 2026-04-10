---
name: extensibility
description: |
  Kimi extensibility via MCP servers and practical usage examples.
  Use when: configuring MCP or demonstrating Kimi integration patterns.
---

# Kimi extensibility

## MCP integration

### Official surface

- `kimi mcp` manages MCP configuration from the command line.
- MCP is the right extension surface for connecting Kimi to live
  external systems such as databases, APIs, and third-party services.

### When to use MCP vs other surfaces

- Use MCP for runtime tool access to external systems.
- Keep static policy and project guidance in `.kimi/AGENTS.md` or
  skills, not in MCP.
- Confirm server configuration details against the current CLI help
  and official docs, as the config format may evolve between versions.

## Usage examples

### Use a project custom agent

```bash
kimi --agent-file .kimi/agents/speckit-orchestrator.yaml \
     -p "Show the current Spec Kit stage" --print
```

### Run headless with the default agent

```bash
kimi -p "Summarize the repository layout" --print
```

### Continue a previous session

```bash
kimi --continue
```

### Select a built-in agent

```bash
kimi --agent okabe
```

### Combine headless mode with piping

```bash
kimi -p "List all TODO comments in src/" --print | tee todos.txt
```

## Guidance

- Start with simple invocations and layer in MCP or custom agents
  only when the task requires external tools or a restricted role.
- For repeatable workflows, define a custom YAML agent and invoke it
  with `--agent-file`.
- Use `--print` mode in scripts so Kimi output can be captured or
  piped without interactive prompts.
