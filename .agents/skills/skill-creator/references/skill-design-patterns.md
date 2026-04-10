---
name: skill-design-patterns
description: |
  Design principles and anti-patterns for creating effective skills that respect token economy.
  Use when: designing a new skill, evaluating skill quality, or refactoring bloated skills.
---

# Skill Design Patterns

## Token Economy

The context window is a shared resource. Skills compete with system prompt, conversation history,
other skills, and the user's actual request for context space.

Default assumption: the model is already very smart. Only add context it does not already have.
Challenge each paragraph: "Does this justify its token cost?"

Prefer concise examples over verbose explanations.

## Progressive Disclosure

Structure skills in layers:

1. **SKILL.md** — lean operational surface (loaded always)
   - Task-oriented `## How to ...` sections
   - Triggers for loading deeper references
   - No setup diaries, changelogs, or meta documentation

2. **references/** — deep detail (loaded JIT)
   - Patterns, templates, checklists, glossaries
   - Loaded only when the task requires them
   - Each file should be self-contained

3. **scripts/** — automation (loaded on demand)
   - Only when the skill involves executable automation

Rule of thumb: if a section is needed less than 50% of the time, move it to `references/`.

## Degrees of Freedom

Match constraint level to risk:

| Risk Level | Constraint | Example |
|------------|-----------|---------|
| Safety-critical | Strict output format, explicit forbidden actions | Security review skill |
| Domain-specific | Required sections, flexible detail | Card creation skill |
| Creative | Loose guidance, examples as inspiration | Brainstorming skill |

Too tight = the model fights the constraint and wastes tokens on compliance.
Too loose = the model drifts and outputs vary unpredictably.

## Common Anti-Patterns

| Anti-Pattern | Problem | Fix |
|-------------|---------|-----|
| Kitchen sink | Everything inline in SKILL.md | Move detail to references/ |
| Tutorial style | Explains what the model already knows | Delete; keep only project-specific knowledge |
| Copy-paste governance | Restates rules from repository governance files or protocols | Point to canonical source |
| Version diary | Changelog or history inside the skill | Remove; git tracks history |
| Defensive padding | Repeats the same rule 3 ways "just to be safe" | State once, clearly |
| Orphan references | references/ files that nothing in SKILL.md triggers | Add trigger or delete |

## Quality Checklist

Before shipping a skill:

1. Every `## ` heading starts with "How to" (contract-enforced)
2. Frontmatter has `name`, `description` with `Use when:`
3. No section exceeds ~100 lines without being split to references/
4. No duplication of content from other skills or canonical files
5. At least one concrete example or pattern per section
6. References are substantive (not stubs with only frontmatter)
7. The skill reads well if you only read SKILL.md (references enhance, not complete)
