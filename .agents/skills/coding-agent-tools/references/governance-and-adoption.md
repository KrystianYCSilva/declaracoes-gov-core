---
name: governance-and-adoption
description: |
  Reference for rolling out AI coding tools under clear governance, review, and release controls.
  Use when: deciding how a team should adopt coding agents without weakening testing, review, or security posture.
---

# Governance and Adoption

## Gold-Standard Anchors

- official product documentation for each assistant or CLI
- Model Context Protocol documentation for tool and data access boundaries
- NIST Secure Software Development Framework for secure delivery controls
- Google SRE workbook material for release safety, gradual rollout, and operational guardrails

## Adoption Questions

1. which workflows stay human-first and which can be agent-assisted?
2. what validation remains mandatory before merge?
3. which tools can write code directly, and under what review model?
4. what is the rollback path if the agent workflow degrades quality?

## Practical Rule

Adopt the smallest tool change that improves throughput without making review, debugging, or incident response harder.
If the team cannot explain the tool's permission model and failure modes, adoption is not mature yet.
