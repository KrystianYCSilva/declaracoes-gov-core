---
name: agent-profile-spec
description: |
  Specification for Copilot agent profile files (.agent.md) including storage locations, frontmatter properties, and tool configuration.
  Use when: creating or debugging a custom Copilot agent profile.
---

# Agent Profile Specification (`.agent.md`)

> Load this file when creating, debugging, or explaining a custom agent profile.

---

## File Location & Naming

| Scope | Directory | Priority |
| ----- | --------- | -------- |
| System / User | `~/.copilot/agents/` | Highest |
| Repository | `.github/agents/` | Overrides org-level |
| Org / Enterprise | `agents/` in `.github-private` repo | Lowest |

Filename: only `.`, `-`, `_`, `a-z`, `A-Z`, `0-9`. Extension: `.agent.md`.

---

## YAML Frontmatter Properties

### `name` *(optional)*

Human-readable display name shown in the agent picker.
Defaults to filename without `.agent.md`.

### `description` *(required)*

What the agent does and when it should be used. Copilot reads this to decide
whether to delegate automatically. Be specific about scope.

### `tools` *(optional — omit for all tools)*

```yaml
tools: ["read", "edit", "search", "shell"]
```

| Tool | Description |
| ---- | ----------- |
| `read` | Read files |
| `edit` | Write / modify files |
| `search` | Codebase search |
| `shell` / `bash` | Execute shell commands |
| `browser` | Web browsing |
| `github` | GitHub API (built-in MCP server) |
| `SERVER/TOOL` | Any MCP server tool |

### `model` *(optional, IDE-only)*

Pins the agent to a specific model. Ignored in the CLI.

```yaml
model: claude-sonnet-4.5
```

### `target` *(optional)*

```yaml
target: github-copilot   # CLI + cloud only
target: vscode           # VS Code only
# omit = available everywhere
```

### `mcp-servers` *(optional)*

MCP servers available **only** to this agent:

```yaml
mcp-servers:
  my-db:
    type: stdio
    command: npx
    args: ["-y", "@my-org/db-mcp-server"]
    env:
      DB_URL: "${DB_URL}"
```

### `handoffs` *(optional)*

Transition buttons shown after the agent completes a task:

```yaml
handoffs:
  - label: "Run tests"
    agent: test-specialist
    prompt: "Run all tests and summarise failures"
  - label: "Open PR"
    agent: default
    prompt: "Open a pull request"
    send: true      # auto-submits without user confirmation
```

---

## Full Example — Testing Specialist

```markdown
---
name: test-specialist
description: >
  Focuses on test coverage and quality without modifying production code.
  Use when writing tests or reviewing test quality.
tools: ["read", "edit", "search"]
handoffs:
  - label: "Review production code"
    agent: default
    prompt: "Review the production code that was just tested"
---

You are a testing specialist focused on improving code quality.

## Responsibilities

- Analyse existing tests and identify coverage gaps
- Write unit, integration, and e2e tests following best practices
- Never modify production code unless explicitly asked
- Use the project's existing test runner (detect from package.json / Makefile)

## Output

- Match test file naming convention found in the project
- Write clear test descriptions that document behaviour, not implementation
- Include edge cases, error paths, and boundary conditions
```

---

## Full Example — Implementation Planner (read-only tools)

```markdown
---
name: implementation-planner
description: >
  Creates detailed implementation plans in Markdown. Use before writing code
  to break down a feature into ordered tasks with dependencies.
tools: ["read", "search", "edit"]
---

You are a technical planning specialist. Output is always a Markdown document.

Every plan must include:
1. Summary (2-3 sentences)
2. Technical context (language, dependencies, storage)
3. Phase breakdown with explicit task dependencies
4. Risk register
5. Definition of done per phase

Save plans to `specs/<feature-name>/plan.md`.
```

---

## Invoking a Custom Agent

```bash
/agent                                             # interactive picker
copilot --agent=test-specialist --prompt "..."     # non-interactive flag
```

Natural language — Copilot infers the agent from its description:

```text
Use the test-specialist agent to add tests for src/auth.ts
```

---

## Debugging Checklist

| Symptom | Likely cause | Fix |
| ------- | ------------ | --- |
| Agent not in `/agent` list | Wrong directory or file extension | Ensure `.github/agents/*.agent.md` |
| Agent ignores tool restrictions | Typo in tool name | Check tool names table above |
| Handoffs not shown | Wrong agent name or `send` type | Verify agent name matches filename |
| `model` property ignored | Running in CLI (not IDE) | Expected — CLI ignores `model` |
