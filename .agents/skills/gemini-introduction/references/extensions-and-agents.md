---
name: extensions-and-agents
description: |
  Reference for Gemini CLI extension surfaces including GEMINI.md, custom commands, extensions, hooks, skills, and MCP.
  Use when: extending Gemini CLI or explaining its available customization and integration surfaces.
---

# Gemini CLI extension surfaces

## Official surfaces

- `GEMINI.md` for project memory (supports `@path/to/file` includes)
- `.gemini/commands/` for custom commands (TOML format)
- extensions for packaged functionality
- hooks for lifecycle automation
- skills for reusable guidance
- MCP for external systems

## Extensions

Extensions are packaged functionality bundles that add capabilities to
the Gemini CLI beyond its built-in tool set. They can provide new tools,
data sources, or specialized processing pipelines. Extensions are
managed through the CLI and can be enabled or disabled per project.

## MCP server configuration

Configure MCP servers in `.gemini/settings.json` under the `mcpServers` key:

```json
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["path/to/server.js"],
      "env": { "API_KEY": "value" }
    }
  }
}
```

MCP servers expose external tools (databases, APIs, code runners) to
the Gemini agent. The agent discovers available tools at session start
and can call them during task execution.

## Hooks

Hooks provide lifecycle automation for Gemini CLI sessions. They let
you run scripts or commands at specific points in the agent lifecycle,
such as before or after a tool invocation, at session start, or on
session end. Use hooks for tasks like linting after file writes,
refreshing caches, or logging agent actions.

## Skills surface

Gemini CLI supports the Agent Skills open standard. Skills are
discovered in `.agents/skills/` and provide on-demand domain knowledge
through a `SKILL.md` entry point with `references/` for detailed
material. Skills are loaded JIT when the agent determines the current
task matches the skill description.

## Custom command format (`.gemini/commands/<name>.toml`)

```toml
description = "What this command does"

prompt = """
Command body here.

Reference context with @filename.
Use the command for: specific task description.
"""
```

The command is invoked by name in Gemini CLI. `description` is required.

## Important boundary

Current public Gemini CLI docs describe commands, extensions, hooks, skills,
MCP, and `GEMINI.md`. They do not describe a native project-local subagent
surface equivalent to Qwen or Claude agents.

## Repository convention

- `.gemini/agents/` exists only as a compatibility layer for this multi-CLI repository.
- Do not describe `.gemini/agents/` as an official Gemini auto-discovery mechanism.
- Use `.gemini/commands/speckit.orchestrator.toml` for the real Gemini-native Spec Kit entrypoint.
