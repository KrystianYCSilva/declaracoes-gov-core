---
name: slash-commands
description: |
  Complete reference for GitHub Copilot CLI slash commands including /init, /new, /clear, /resume, and task management commands.
  Use when: explaining or invoking Copilot CLI slash commands.
---

# Slash Commands — Complete Reference

> Load this file when the user asks for details about a specific slash command
> or a full command listing.

---

## Session & Navigation

### `/init`

Generates `.github/copilot-instructions.md` tailored to the current repository,
or suppresses the initialisation suggestion if the file already exists.
Run once per project after cloning or when the tech stack changes.

### `/new` / `/clear`

Abandons the current conversation (without reverting file changes) and starts
a fresh session in the same working directory.

### `/resume [SESSION-ID]`

Resumes a previous interactive session. Without an argument shows a picker.

```bash
/resume                   # interactive picker
/resume abc123            # resume by session ID
copilot --continue        # resume most recent session from terminal
```

### `/rename [NAME]`

Renames the current session. Without a name, auto-generates one from the
conversation content.

### `/rewind` / `/undo`

Rolls back the last turn and reverts any file changes made during that turn.

### `/compact`

Summarises conversation history to free up context window tokens.
Also runs automatically at 95% usage.

### `/context`

Visual breakdown of current token usage by source.

### `/usage`

Shows session statistics: premium requests used, session duration, lines of
code edited, per-model token breakdown.

### `/share`

Exports the session to a Markdown file, HTML file, or GitHub Gist.

### `/copy`

Copies the last Copilot response to the system clipboard.

---

## Agent & Delegation

### `/agent [AGENT-NAME]`

Opens the agent picker or directly invokes a named agent.

```bash
/agent                          # interactive picker
/agent test-specialist          # invoke by name
```

### `/delegate`

Hands off the current session to the Copilot cloud agent on GitHub.com,
which will open a pull request with the completed work.

### `/fleet`

Enables fleet mode: runs multiple subagent tasks in parallel, each in its own
context window.

### `/tasks`

Lists background tasks and their current status.

---

## Skills

```bash
/skills            # interactive toggle on/off
/skills list       # list all available skills
/skills info       # details + location of each skill
/skills add PATH   # add an extra skills directory
/skills reload     # pick up skills added during current session
/skills remove DIR # remove a skill directory
```

---

## Code & IDE

```bash
/plan              # draft implementation plan before coding
/diff              # review changes made in the current directory
/pr                # operate on pull requests for the current branch
/review            # AI code review of current changes
/ide               # connect to an IDE workspace
/lsp               # manage language server configuration
/terminal-setup    # configure Shift+Enter multiline input
```

---

## Models, MCP & Plugins

```bash
/model             # select AI model (default: Claude Sonnet 4.5)
/mcp               # list, add, or remove MCP servers
/mcp add           # interactive MCP server setup
/plugin            # manage plugins and plugin marketplaces
```

---

## Permissions

```bash
/allow-all              # approve all tools for this session (alias: --yolo)
/add-dir PATH           # add a trusted directory
/list-dirs              # show all trusted directories
/cwd [PATH]             # show or change working directory
/reset-allowed-tools    # clear all per-session tool approvals
```

---

## Help, Updates & Feedback

```bash
/help              # full in-session help reference
/changelog [summarize]  # show changelog; add 'summarize' for AI summary
/version           # version info + update check
/update            # download and install latest CLI version
/feedback          # submit survey, bug report, or feature request
/experimental      # toggle experimental features (autopilot, etc.)
/theme [NAME]      # set colour mode
/instructions      # view and toggle custom instruction files
```

---

## Auth & User

```bash
/login             # authenticate via browser or GH_TOKEN / GITHUB_TOKEN
/logout            # clear GitHub authentication
/user              # manage GitHub user list
```

---

## All Commands — Quick Index

```text
/add-dir  /agent  /allow-all  /changelog  /clear  /compact  /context
/copy  /cwd  /delegate  /diff  /experimental  /feedback  /fleet  /help
/ide  /init  /instructions  /list-dirs  /login  /logout  /lsp  /mcp
/model  /new  /plan  /plugin  /pr  /rename  /reset-allowed-tools  /resume
/review  /rewind  /share  /skills  /tasks  /terminal-setup  /theme  /undo
/update  /usage  /user  /version
```
