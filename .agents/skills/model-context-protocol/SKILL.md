---
name: model-context-protocol
description: |
  Connect AI agents to external data sources using the standardized Model Context Protocol (MCP).
  Use when: integrating agents with databases, file systems, APIs, or building custom MCP servers.
activation: Manual
estimated_tokens: 370
---

# Model Context Protocol

Use this skill when the system truly needs a protocol boundary to tools, resources, or prompts.

## How to Navigate This Skill

- `references/server-implementation.md`: server design and implementation patterns
- `references/sources.md`: official MCP specification and documentation links

## How to Model MCP Capabilities

- use resources for readable data
- use tools for executable actions
- use prompts for reusable interaction templates
- choose the smallest capability that solves the problem

## How to Build a Safe MCP Server

- keep stdout reserved for protocol traffic
- send logs somewhere else
- keep tool contracts narrow and typed
- gate destructive operations with explicit approval and validation

## How to Configure Clients Thoughtfully

- verify transport support, resource handling, and tool semantics in the target client
- do not assume every client exposes the full MCP surface
- prefer local files or existing integrations when they already solve the problem

## How to Stay Accurate

- use the official MCP references before asserting advanced protocol behavior
- keep payloads small, typed, and paginated when possible
- prefer protocol simplicity over clever server features
