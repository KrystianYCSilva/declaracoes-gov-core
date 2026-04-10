---
name: cli-usage
description: |
  Kimi CLI commands, operational modes, and tool surfaces.
  Use when: explaining or invoking Kimi CLI commands and modes.
---

# Kimi CLI usage

## CLI Commands

### Invocation forms

- `kimi` -- start an interactive session.
- `kimi -p "<prompt>" --print` -- run headless, print result to stdout.
- `kimi --continue` -- resume the last session.
- `kimi --agent default|okabe` -- select a built-in agent.
- `kimi --agent-file <file>` -- load a custom YAML agent definition.
- `kimi mcp` -- manage MCP server configuration.

### Guidance

- Prefer `kimi --help` for exact flags and defaults in the installed version.
- Public docs and help output are the canonical sources for Kimi operator behavior.
- Do not over-claim undocumented slash commands or bootstrap flows.

## Operational Modes

### Interactive mode

The default mode. Kimi starts a REPL where the user types prompts and
the agent responds, reads files, runs commands, and writes code with
approval gates before destructive actions.

### Headless / print mode

Activated with `-p "<prompt>" --print`. The agent processes the prompt,
prints the result to stdout, and exits. Use this mode for automation,
CI pipelines, and deterministic piping into other tools.

### Approval-friendly default mode

The standard safety posture. The agent asks for user confirmation before
executing shell commands or writing files. This is the recommended mode
for normal repository work where oversight is important.

### Higher-autonomy mode (`--yolo`)

Removes most approval gates, letting the agent execute commands and
write files without confirmation. Use only in controlled environments
such as disposable containers or sandboxed CI runners where the risk
of unreviewed changes is acceptable.

### Continue mode

Activated with `--continue`. Resumes the last interactive session,
restoring prior context and conversation history. Useful for picking
up a multi-step task after a break.

## Tool Surfaces

Kimi exposes a tool-enabled coding-agent surface. The available tools
include:

- **File access** -- read, write, and search files in the project tree.
- **Shell access** -- execute shell commands in the project directory.
- **Web access** -- fetch URLs and retrieve web content.
- **Background tasks** -- run long-lived processes in the background.

The exact tool inventory can vary by agent definition and Kimi version.
Custom agents defined in YAML can restrict the tool surface to a subset
relevant to their role.

### Guidance

- Use the smallest tool surface that solves the task.
- Keep custom agents narrow when restricting tools matters.
- Verify tool names and availability against current CLI help and
  the current Kimi build.
