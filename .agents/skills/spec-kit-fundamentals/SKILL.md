---
name: spec-kit-fundamentals
description: |
  Guide spec-driven development workflows with conditional backend selection and Spec Kit artifact discipline.
  Use when: creating specs, plans, and tasks under `backend=spec-kit-native`, or clarifying
  how Spec Kit separates artifact ownership from project governance.
activation: Auto
estimated_tokens: 890
---

# Spec Kit Fundamentals

Spec Kit is a spec-driven development framework that owns the spec/plan/tasks artifact flow.
Project governance (CI/CD gates, code review, release criteria) remains separate and is NOT
part of Spec Kit — it belongs to the repository's own CI/CD pipeline and AGENTS.md.

For the boundary between artifact flow and project governance, read `references/artifact-vs-governance.md`.

## How to Start a Spec-Kit Cycle

Before invoking Spec Kit:

1. confirm `backend=spec-kit-native`
2. confirm the card is ready for technical execution
3. if Spec Kit is not bootstrapped, use `specify init`
4. if tool availability is unclear, use `specify check`
5. stop and ask if the repository actually wants `flow-native` or `lightweight`

Do not use Spec Kit as a vague synonym for planning.
Use it as a concrete technical backend.

## How to Separate Public Surfaces from Prompt Stages

Treat these as different layers:

- public terminal surface: `specify init`, `specify check`
- runtime-specific prompt or agent surfaces: `/speckit.constitution`, `/speckit.specify`, `/speckit.plan`, `/speckit.tasks`, `/speckit.implement`
- repository-local overlays: `speckit.*` agents, helper prompts, or custom automation packs

Do not document `speckit.specify` or `speckit.plan` as plain terminal commands
unless the current runtime actually exposes them that way.

## How to Move Through Approved Artifacts

Use the upstream artifact flow:

1. approved spec artifact
2. approved plan artifact
3. approved task breakdown
4. implementation against the approved artifacts

Keep `.specify/` as runtime metadata and the feature directory as the active
technical tree. If one artifact is rejected, fix that artifact instead of skipping ahead.

## How to Separate Artifact Flow From Project Governance

Spec Kit owns:

- the bootstrap and tool verification surfaces
- the spec/plan/tasks artifact flow
- its template semantics
- its runtime metadata under `.specify/`

Project governance (external to Spec Kit) owns:

- CI/CD pipeline enforcement
- code review and approval gates
- release readiness criteria
- AGENTS.md context and conventions

Spec Kit generates artifacts. Governance validates and enforces them via CI/CD — not via prompts.

## How to Treat Local Overlays and Repairs

- Repository-local `speckit.*` helpers can accelerate the flow but do not
  redefine the upstream command contract.
- Manual edits to spec, plan, or task artifacts are repair or fallback actions;
  do not describe them as equivalent to a successful upstream step when the
  tooling is available.
- If the repository ships custom pack behavior, label it as project-local
  overlay, not baseline Spec Kit.

## How to Keep Spec Artifacts Useful

Specs should stay user- and behavior-focused.
Plans should stay technical and implementation-oriented.
Tasks should be executable and traceable back to the approved plan.

## How to Navigate This Skill

- `references/artifact-vs-governance.md`: boundary between Spec Kit artifact flow and project governance
- `references/sources.md`: artifact locations, skill routing table, and SDD methodology references
