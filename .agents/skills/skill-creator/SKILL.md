---
name: skill-creator
description: |
  Create or refactor a skill package with a lean SKILL.md, progressive disclosure,
  and reusable scripts, references, or assets.
  Use when: creating, rewriting, or standardizing a skill under `.agents/skills`
  or another Codex-discoverable skill directory.
---

# Skill Creator

Use this skill to shape a maintainable skill package, not to write end-user documentation.

## How to Apply the Local Contract

- keep frontmatter strict: `name` and `description` only
- use the description as the trigger surface; include what the skill does and when to use it
- keep the body operational, with `## How to ...` sections
- move long or variant-specific detail into `references/`, `scripts/`, or `assets/`

## How to Keep Token Cost Low

- assume the model already knows general software basics
- keep only workflow, guardrails, and project-specific knowledge in `SKILL.md`
- prefer short examples and explicit load order over long prose
- never duplicate the same guidance in both `SKILL.md` and `references/`

## How to Decide What to Bundle

- add `scripts/` when deterministic execution or repetitive code matters
- add `references/` when the skill needs official docs, schemas, prompts, or policies
- add `assets/` only for files that are meant to be copied or used in outputs
- delete placeholder or stale files after refactoring

For deeper authoring patterns, read `references/workflows.md`, `references/skill-design-patterns.md`,
and `references/output-patterns.md`.

## How to Finish a Skill

1. define the recurring workflow
2. extract the non-obvious decisions and failure modes
3. write the smallest useful `SKILL.md`
4. add only the references the model will actually need
5. validate the structure and iterate from real usage

## How to Stay Accurate

- keep repository-specific rules inside the local skill, not in hidden assumptions
- use `references/sources.md` when you need the canonical Skill and Agent Skills sources
- prefer revision by real usage over speculative abstraction
