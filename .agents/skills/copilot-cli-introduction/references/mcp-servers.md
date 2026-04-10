---
name: mcp-servers
description: |
  Reference for configuring MCP servers with GitHub Copilot CLI, including config file locations and agent-scoped server setup.
  Use when: adding external tools to Copilot CLI or configuring the GitHub MCP server.
---

# MCP Servers — Configuration Reference

> Load this file when asked about MCP servers, adding tools to Copilot,
> or configuring the GitHub MCP server.

---

## What Is MCP?

The Model Context Protocol (MCP) is an open standard that lets AI agents
talk to external tools and services through a unified interface.
Copilot CLI ships with the **GitHub MCP server** pre-configured.

---

## Config File Location

| Location | Scope |
| -------- | ----- |
| `~/.copilot/mcp-config.json` | Default user-level |
| `$COPILOT_HOME/mcp-config.json` | Custom location via env var |

Agent-specific MCP servers can also be declared in `.agent.md` frontmatter
under `mcp-servers:` (those servers are only available to that agent).

---

## Config File Format

```json
{
  "mcpServers": {
    "SERVER-NAME": {
      "type": "stdio",
      "command": "EXECUTABLE",
      "args": ["ARG1", "ARG2"],
      "env": {
        "ENV_VAR": "value"
      }
    }
  }
}
```

### Server types

| Type | Description |
| ---- | ----------- |
| `stdio` | Subprocess communicates via stdin/stdout (most common) |
| `sse` | HTTP Server-Sent Events endpoint |

### SSE server example

```json
{
  "mcpServers": {
    "my-remote-server": {
      "type": "sse",
      "url": "https://my-server.example.com/mcp",
      "headers": {
        "Authorization": "Bearer ${MY_API_KEY}"
      }
    }
  }
}
```

---

## Managing Servers from the CLI

```bash
/mcp                    # list all configured servers and their status
/mcp add                # interactive setup wizard (fill form → Ctrl+S)
/mcp remove SERVER-NAME # remove a server
```

After adding a server, its tools are immediately available to the model
and appear in agent `tools:` lists by `SERVER-NAME/TOOL-NAME`.

---

## Pre-configured: GitHub MCP Server

The GitHub MCP server is included automatically. Key tools it provides:

| Tool | What it does |
| ---- | ------------ |
| `github/list_issues` | List issues in a repository |
| `github/create_issue` | Create a new issue |
| `github/create_pull_request` | Open a pull request |
| `github/list_workflow_runs` | List GitHub Actions runs |
| `github/get_job_logs` | Get logs for a specific job |
| `github/summarize_job_log_failures` | AI summary of failed job logs |
| `github/search_code` | Search code across GitHub |
| `github/get_file_contents` | Read a file from any repo |

Use `GH_TOKEN` or `GITHUB_TOKEN` for authentication if you need private repos
or higher API rate limits.

---

## Common MCP Servers

### Browser / Web Automation

```json
"playwright": {
  "type": "stdio",
  "command": "npx",
  "args": ["-y", "@playwright/mcp@latest"]
}
```

Install: `npm install -g @playwright/mcp` (optional — `npx` installs on demand)

### Filesystem (outside working directory)

```json
"filesystem": {
  "type": "stdio",
  "command": "npx",
  "args": ["-y", "@modelcontextprotocol/server-filesystem", "/allowed/path"]
}
```

### PostgreSQL

```json
"postgres": {
  "type": "stdio",
  "command": "npx",
  "args": ["-y", "@modelcontextprotocol/server-postgres"],
  "env": {
    "DATABASE_URL": "postgresql://user:pass@localhost:5432/mydb"
  }
}
```

### Slack

```json
"slack": {
  "type": "stdio",
  "command": "npx",
  "args": ["-y", "@modelcontextprotocol/server-slack"],
  "env": {
    "SLACK_BOT_TOKEN": "${SLACK_BOT_TOKEN}",
    "SLACK_TEAM_ID": "${SLACK_TEAM_ID}"
  }
}
```

### Fetch / HTTP

```json
"fetch": {
  "type": "stdio",
  "command": "uvx",
  "args": ["mcp-server-fetch"]
}
```

### SQLite

```json
"sqlite": {
  "type": "stdio",
  "command": "uvx",
  "args": ["mcp-server-sqlite", "--db-path", "./data/mydb.sqlite"]
}
```

### Memory / Knowledge Graph

```json
"memory": {
  "type": "stdio",
  "command": "npx",
  "args": ["-y", "@modelcontextprotocol/server-memory"]
}
```

---

## Using MCP Tools in Agent Profiles

Reference MCP tools by `SERVER-NAME/TOOL-NAME` in the `tools` list:

```yaml
tools: ["read", "edit", "github/create_pull_request", "playwright/browser_action"]
```

Or configure an agent-local server directly in the agent profile:

```yaml
mcp-servers:
  my-api:
    type: stdio
    command: node
    args: ["./tools/my-mcp-server.js"]
    env:
      API_KEY: "${MY_API_KEY}"
```

---

## Security Checklist

- Use environment variable references (`${VAR_NAME}`) — never hardcode secrets
- Keep `mcp-config.json` out of version control (add to `.gitignore`)
- Only install MCP servers from trusted sources
- Review what each server can access before adding it
- For agent-embedded servers, use `tools:` to restrict scope
