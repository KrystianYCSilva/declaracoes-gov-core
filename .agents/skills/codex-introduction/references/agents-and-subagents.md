---
name: agents-and-subagents
description: |
  Reference for Codex built-in agents (default, worker, explorer) and the difference between agents, skills, and AGENTS.md.
  Use when: selecting a Codex agent, creating custom subagents, or clarifying Codex agent concepts.
---

# Agents and subagents

## Mental model

In Codex, "agent" usually means a spawned worker thread with its own context and
configuration layer.

This is not the same thing as:

- `AGENTS.md`
- a skill
- a repo prompt file

## Built-in agents

The official subagents doc lists three built-in agents:

- `default`
  - general-purpose fallback agent
- `worker`
  - execution-focused agent for implementation and fixes
- `explorer`
  - read-heavy codebase exploration agent

Use them like this conceptually:

- `default` for balanced work
- `worker` for bounded production tasks
- `explorer` for codebase discovery and architecture tracing

## Custom agents

Official Codex docs say custom agents live in:

- `~/.codex/agents/` for personal agents
- `.codex/agents/` for project-scoped agents

Each custom agent is one standalone TOML file.

Required fields:

- `name`
- `description`
- `developer_instructions`

Optional inherited fields include:

- `nickname_candidates`
- `model`
- `model_reasoning_effort`
- `sandbox_mode`
- `mcp_servers`
- `skills.config`

## Minimal custom agent example

```toml
name = "reviewer"
description = "Focused code-review agent for finding regressions and missing tests."
developer_instructions = """
Prioritize bugs, behavioral regressions, and testing gaps.
Keep summaries brief and findings concrete.
"""
model = "gpt-5.4"
model_reasoning_effort = "medium"
```

Recommended location in this repository:

```text
.codex/agents/reviewer.toml
```

## When to use a custom agent

Use a custom agent when you want a reusable delegated role, for example:

- architecture explorer;
- strict reviewer;
- migration worker;
- test writer;
- docs synthesizer.

Do not create a custom agent if a normal prompt or a skill is enough.

## Global subagent settings

The official docs describe `[agents]` config for settings such as:

- `agents.max_threads`
- `agents.max_depth`
- `agents.job_max_runtime_seconds`

Practical reading:

- `max_threads` caps concurrent open agent threads;
- `max_depth` limits recursive fan-out;
- higher values increase cost, latency, and unpredictability.

## Good delegation pattern

Delegate when:

- the task is bounded;
- the output is clearly defined;
- the write scope is isolated;
- the parent can continue useful work in parallel.

Avoid delegation when:

- the result is needed immediately for the next blocking step;
- the task is tightly coupled to the current reasoning thread;
- multiple agents would edit the same artifact at once.
