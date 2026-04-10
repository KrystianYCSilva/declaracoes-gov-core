#!/usr/bin/env bash
# create-agent.sh — Scaffold a new Copilot custom agent profile
#
# Usage:
#   ./create-agent.sh <agent-name> [agents-dir]
#
# Examples:
#   ./create-agent.sh test-specialist
#   ./create-agent.sh implementation-planner .github/agents
#   ./create-agent.sh my-reviewer ~/.copilot/agents

set -euo pipefail

AGENT_NAME="${1:-}"
AGENTS_DIR="${2:-.github/agents}"

if [[ -z "$AGENT_NAME" ]]; then
  echo "Usage: $0 <agent-name> [agents-dir]" >&2
  echo "  agent-name  letters, numbers, ., -, _ only" >&2
  echo "  agents-dir  default: .github/agents" >&2
  exit 1
fi

# Validate naming convention (Copilot: letters, numbers, . - _)
if [[ ! "$AGENT_NAME" =~ ^[a-zA-Z0-9._-]+$ ]]; then
  echo "Error: agent name may only contain letters, numbers, '.', '-', '_'." >&2
  exit 1
fi

AGENT_FILE="$AGENTS_DIR/${AGENT_NAME}.agent.md"

if [[ -f "$AGENT_FILE" ]]; then
  echo "Error: '$AGENT_FILE' already exists." >&2
  exit 1
fi

mkdir -p "$AGENTS_DIR"

cat > "$AGENT_FILE" << AGENT_EOF
---
name: ${AGENT_NAME}
description: >
  [One paragraph describing what this agent does and its specific domain.
  Be concrete — Copilot reads this to decide whether to auto-delegate.
  Example: "Focuses on test coverage. Use when writing or reviewing tests.
  Does NOT modify production code."]
tools: ["read", "edit", "search"]
# tools: ["read", "search"]          # read-only variant
# tools: ["read", "edit", "search", "shell"]  # with shell access
# model: claude-sonnet-4.5            # IDE-only; ignored in CLI
# target: github-copilot              # "vscode" | "github-copilot" | omit for both
---

You are a [ROLE] specialist focused on [DOMAIN].

## Responsibilities

- [Primary responsibility — be specific]
- [Secondary responsibility]
- [What you explicitly do NOT do]

## Approach

1. [First step you always take — e.g., read existing code first]
2. [Second step]
3. [Validation or confirmation step]

## Output Standards

- [File naming conventions]
- [Structure or formatting requirements]
- [Quality bar — what "done" looks like]
AGENT_EOF

echo "✓ Created agent: $AGENT_FILE"
echo ""
echo "Next steps:"
echo "  1. Edit $AGENT_FILE"
echo "     - Sharpen the 'description' (it drives auto-delegation)"
echo "     - Adjust 'tools' to the minimum needed"
echo "     - Replace placeholder prompt with real expertise"
echo "  2. Commit the file — it is live when merged to default branch"
echo "  3. Use it:"
echo "     /agent ${AGENT_NAME}"
echo "     Use the ${AGENT_NAME} agent to ..."
echo "     copilot --agent=${AGENT_NAME} --prompt \"...\""
