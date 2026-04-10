---
name: cursor-agents-cli
description: |
  Reference for the Cursor Agent CLI, the cursor-agent AI-backend identifier, and MCP integration in Cursor projects.
  Use when: configuring cursor-agent, invoking the Cursor CLI, or setting up MCP servers in Cursor.
---

# Cursor Agent CLI

## What matters

- `cursor-agent` is the AI-backend identifier used with `specify init . --ai=cursor-agent --script <ps|sh>`.
- The Cursor IDE ships a `cursor` CLI for terminal automation; use `cursor --help` to confirm available flags for the installed version.
- Cursor Agent commands (slash commands) live in `.cursor/commands/*.md`.

## Agent mode vs ask mode vs edit mode

Cursor provides three primary interaction modes:

- **Agent mode** -- the agent reads files, plans changes, executes tools,
  and writes code autonomously. It can invoke shell commands, search the
  codebase, and iterate on its own plan. This is the default mode for
  multi-step tasks.
- **Ask mode** -- a read-only conversational mode. The agent answers
  questions about the codebase but does not modify files. Use it for
  exploration and code review.
- **Edit mode** -- the agent applies targeted edits to specific files
  the user selects. It does not execute tools or run commands beyond
  file modifications.

## Install

Cursor CLI (`cursor`) ships with the Cursor IDE.
After installing Cursor IDE, the `cursor` binary is available on PATH.

## Common CLI operations

```sh
cursor .                       # open current directory in Cursor
cursor --help                  # list available flags
cursor --version               # print installed version
```

## MCP integration

Configure MCP servers in `.cursor/mcp.json` (project-local) or `~/.cursor/mcp.json` (global):

```json
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["path/to/server.js"],
      "env": { "API_KEY": "${env:MY_API_KEY}" }
    }
  }
}
```

MCP servers extend the agent's tool surface with external capabilities
such as database queries, API calls, or custom code-generation backends.

## Typical workflow patterns

1. **Start agent mode** -- open the project in Cursor and begin an agent
   session. Describe the task in natural language.
2. **Use commands** -- invoke slash commands from `.cursor/commands/` to
   trigger predefined workflows (e.g., `/speckit` for orchestration).
3. **Compose with MCP** -- let the agent call MCP-backed tools during
   its plan execution to reach external systems without leaving the IDE.
4. **Switch to ask mode** -- when you need to review or understand code
   without making changes, switch to ask mode for safe exploration.

## Repository convention

- Keep reusable repo commands in `.cursor/commands/`.
- Keep the shared cross-agent rules in the root `AGENTS.md`.
- Cursor does not need a fake subagent model when commands or rules are the official surface.
