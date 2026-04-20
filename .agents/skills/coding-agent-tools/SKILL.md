---
name: coding-agent-tools
description: |
  Compare AI coding tools and recommend a workflow based on reviewability, control, and delivery risk.
  Use when: choosing between IDE and CLI agents, comparing coding assistants,
  or shaping an AI-assisted development process for a team or repository.
activation: Auto
estimated_tokens: 520
---

# Coding Agent Tools

Use this skill to compare tool shapes and adoption strategy, not to re-explain every product from scratch.

## How to Navigate This Skill

- `references/cli-guide.md`: high-level comparison of major coding agents
- `references/cli-nuances.md`: product-specific operational differences
- `references/modern-stack.md`: ecosystem and architectural fit
- `references/governance-and-adoption.md`: rollout, policy, and team-level controls
- `references/warnings.md`: common failure modes and guardrails
- `references/workflow-scripts.md`: automation patterns and repeatable workflows
- `references/glossary.md`: key terms in the coding agent tool ecosystem
- `references/sources.md`: official vendor documentation index

## How to Compare Tools

- start from task shape: inline editing, terminal automation, or governed orchestration
- evaluate reviewability, permission model, context control, and reproducibility
- prefer tools the team can validate and debug, not just tools with the most features

## How to Recommend Adoption

- start with a narrow workflow and explicit review gates
- add autonomy only after the team can observe and recover from failures
- use governance and tooling together; one does not replace the other

## How to Stay Accurate

- separate tool facts from preference or opinion
- prefer the official sources in `references/sources.md` when product behavior is uncertain
- load deeper references only for the tool or workflow currently under discussion
- use `references/common-traps.md` for the 5 most common coding agent failures (over-governance, lock-in, no review gate, tool-to-control-tools, parallel conflicts)
- use `templates/ci-gate-template.yml.md` for a GitHub Actions quality gate template for AI-assisted workflows
