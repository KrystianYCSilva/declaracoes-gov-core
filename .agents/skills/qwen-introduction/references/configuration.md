---
name: configuration
description: |
  Reference for Qwen Code configuration covering settings areas, authentication, model providers, MCP, and environment variables.
  Use when: configuring Qwen Code or explaining the /settings command and available configuration options.
---

# Qwen Code - Configuration Reference

## Settings

**Access**: `/settings`

### Key Settings Areas

| Setting | Purpose |
|---------|---------|
| Behavior | Response style, verbosity, auto-approval rules |
| Tools | Enable/disable specific tool access |
| Output | Format preferences, code block styling |
| Context | Context window size, memory limits |
| Model Provider | Select/rotate model providers |
| MCP Servers | Configure external integrations |
| Ignored Files | Files/directories to skip |
| Trusted Folders | Grant explicit permissions |

## Authentication

**Recommended**: Qwen OAuth (Free)

**Login flow**:
1. Start: `qwen`
2. Select authentication
3. Choose **Qwen OAuth (Free)**
4. Follow browser auth flow
5. Authorize access

## Model Providers

**Command**: `/settings model-provider`

**Options**:
- Qwen models (recommended, free tier available)
- Third-party providers (if configured)

## Ignoring Files

### Method 1: `.qwenignore` file

Create `.qwenignore` in project root (like `.gitignore`):

```gitignore
# Build artifacts
dist/
build/
*.log

# Sensitive files
.env
*.key
secrets/

# Dependencies
node_modules/
vendor/
```

### Method 2: Settings command
```
/settings ignored-files
```

## Trusted Folders

Grant explicit permissions to specific folders:
```
/settings trusted-folders
```

Add folders you trust to allow full access (edit, execute, etc.).

## Themes

**Command**: `/theme dark|light|solarized`

Customize terminal appearance.

## Environment Variables

| Variable | Purpose |
|----------|---------|
| `QWEN_API_KEY` | API key (if not using OAuth) |
| `QWEN_MODEL` | Override default model |
| `QWEN_CONFIG` | Custom config file path |
| `QWEN_DEBUG` | Enable debug mode (`qwen --debug` also works) |
| `HTTP_PROXY` / `HTTPS_PROXY` | Proxy configuration |

## MCP (Model Context Protocol)

Connect external data sources:

**Supported integrations**:
- Google Drive
- Figma
- Slack
- Jira
- GitHub
- Custom MCP servers

**Configure**: `/settings mcp`

## File Locations

```
~/.qwen/
├── skills/           # Personal Skills
├── agents/           # Personal Agents
└── settings.json     # User settings

.qwen/
├── skills/           # Project Skills
├── agents/           # Project Agents
├── context.md        # Project context (from /init)
└── settings.json     # Project settings (overrides user)
```

## Project vs User Settings

- **User settings** (`~/.qwen/settings.json`): Apply to all projects
- **Project settings** (`.qwen/settings.json`): Override user settings for specific project
- **Precedence**: Project > User > Defaults
