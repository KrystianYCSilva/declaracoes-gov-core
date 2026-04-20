---
name: spec-kit-fundamentals
description: |
  Guide spec-driven development workflows with conditional backend selection and Spec Kit artifact discipline.
  Use when: creating specs, plans, and tasks under `backend=spec-kit-native`.
---

# Spec Kit Fundamentals

This skill applies only when the active backend is `spec-kit-native`.
Spec Kit owns the spec-driven execution artifacts and bootstrap.

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

Keep `.specify/` as upstream runtime metadata
and `specs/` as the active technical tree when that contract is installed.
If one artifact is rejected, fix that artifact instead of skipping ahead.

## How to Separate Upstream From Local Governance

Upstream Spec Kit owns:

- the upstream bootstrap and tool verification surfaces
- the spec/plan/tasks artifact flow
- its template semantics
- its runtime metadata under `.specify/`

Repository governance owns:

- bootstrap routing into the current backend
- macro lifecycle state
- source-of-truth precedence

Never let Spec Kit update memory, context governance, or closure state by itself.

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

- `references/sources.md`: project-local artifact locations, runtime agent routing table, and SDD methodology references
