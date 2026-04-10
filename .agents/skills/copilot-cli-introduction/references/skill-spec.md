---
name: skill-spec
description: |
  Specification for Copilot skill files (SKILL.md), covering structure, JIT loading behavior, and the agentskills.io open standard.
  Use when: creating or explaining skills for GitHub Copilot CLI.
---

# Skill Specification (`SKILL.md`)

> Load this file when creating, debugging, or explaining a skill.
> Also covers the [agentskills.io](https://agentskills.io) open standard.

---

## What Is a Skill?

A skill is a **directory** containing a `SKILL.md` file plus optional scripts
and resources. When Copilot invokes a skill, it injects the `SKILL.md` body
into the agent's context along with the contents of all other files in the
skill directory. Scripts are made available for the agent to run.

Skills differ from custom instructions:

| | Custom instructions | Skills |
| --- | --- | --- |
| When loaded | Every request | On demand (JIT) |
| Best for | Always-on conventions | Deep task procedures |
| Format | Any `.md` file | `SKILL.md` with frontmatter |
| Scripts | No | Yes |

---

## Directory Layout

```text
.github/skills/MY-SKILL/     # or .claude/skills/ or .agents/skills/
├── SKILL.md                  # required, must be exactly this name
├── helper-script.sh          # optional — auto-discovered
├── helper-script.ps1         # optional — Windows variant
└── examples/                 # optional sub-resources
    └── example-input.json
```

**Naming rules**: directory name and skill `name` must be **lowercase with
hyphens** only (e.g., `github-actions-debugging`, not `GitHubActionsDebugging`).

---

## Supported Locations

| Scope | Path |
| ----- | ---- |
| Project (Copilot / Claude / Agents) | `.github/skills/`, `.claude/skills/`, `.agents/skills/` |
| User — all projects | `~/.copilot/skills/`, `~/.claude/skills/`, `~/.agents/skills/` |
| Custom location | Any directory added via `/skills add PATH` |

Organisation and enterprise-level skills are planned but not yet supported.

---

## `SKILL.md` Frontmatter Properties

### `name` *(required)*

Unique identifier for the skill. Must be **lowercase with hyphens only**.
Typically matches the directory name.

```yaml
name: github-actions-debugging
```

### `description` *(required)*

What the skill does and **when Copilot should use it**. This is the key field —
Copilot reads descriptions to decide whether to load the skill automatically.
Include explicit trigger phrases.

```yaml
description: >
  Guide for debugging failing GitHub Actions workflows.
  Use this when asked to debug failing CI/CD pipelines or GitHub Actions
  workflow failures.
```

### `license` *(optional)*

License that applies to the skill content.

```yaml
license: MIT
```

### `allowed-tools` *(optional)*

Pre-approves tools so Copilot can run them without asking each time.

```yaml
allowed-tools: shell
```

> **Security warning**: Only pre-approve `shell` or `bash` for scripts you
> have written and reviewed. Without this field, Copilot still asks for
> approval before running any terminal command.

---

## Minimal Example

```markdown
---
name: github-actions-debugging
description: >
  Guide for debugging failing GitHub Actions workflows.
  Use when asked to debug failing CI/CD pipelines.
---

To debug a failing GitHub Actions workflow:

1. Use `list_workflow_runs` to find recent runs for the PR
2. Use `summarize_job_log_failures` for an AI summary without flooding context
3. If more detail is needed, use `get_job_logs`
4. Reproduce the failure locally before committing a fix
```

---

## Skill with a Script

```text
.github/skills/svg-to-png/
├── SKILL.md
└── convert.sh
```

```markdown
---
name: svg-to-png
description: Converts SVG files to PNG. Use when asked to convert SVG images.
allowed-tools: shell
---

When converting SVG to PNG:

1. Find the SVG files the user wants to convert
2. Run `convert.sh INPUT.svg OUTPUT.png` from this skill's base directory
3. Confirm the output file was created successfully

The script requires `rsvg-convert` (install: `brew install librsvg` or
`apt install librsvg2-bin`).
```

```bash
#!/usr/bin/env bash
# convert.sh
set -euo pipefail
rsvg-convert -o "${2:-${1%.svg}.png}" "$1"
echo "Converted: $1 → ${2:-${1%.svg}.png}"
```

---

## Best Practices

1. **Write trigger phrases into `description`** — Copilot uses the description
   to decide whether to load the skill. "Use when..." clauses are effective.
2. **One skill, one domain** — keep skills focused; split large skills.
3. **Reference files by relative path** in the body — all files in the skill
   directory are available.
4. **Script portability** — provide both `.sh` and `.ps1` variants for
   cross-platform support.
5. **Security** — never hardcode credentials; use env vars or secret stores.
6. **Test your skill** — after creating, run `/skills reload` then ask Copilot
   the exact trigger question from the description.

---

## CLI Skill Management

```bash
/skills list          # see all skills and their descriptions
/skills info          # full details including file paths
/skills               # interactive enable/disable toggle
/skills add PATH      # add a new skills directory
/skills reload        # pick up new skills without restarting
/skills remove DIR    # remove a skill (non-plugin skills only)
```

---

## Community Skill Resources

- GitHub awesome-copilot collection: <https://github.com/github/awesome-copilot>
- Anthropic community skills: <https://github.com/anthropics/skills>
- Agent Skills open standard: <https://github.com/agentskills/agentskills>
