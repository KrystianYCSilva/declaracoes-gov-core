---
name: custom-instructions
description: |
  Reference for Copilot custom instruction file surfaces and repository conventions for always-on guidance.
  Use when: configuring .github/copilot-instructions.md or explaining instruction file layering.
---

# Copilot custom instructions

Custom instructions are natural language directives that are automatically
injected into every Copilot interaction. They let teams encode coding
standards, architecture rules, security constraints, and style preferences
so that Copilot follows them without the user repeating the context each time.

## Three instruction surfaces

### 1. `.github/copilot-instructions.md`

The repository-wide instruction file. Every Copilot request in this
repository receives its contents. Use it for broad rules that apply to
the entire codebase (language standards, framework conventions, forbidden
patterns).

### 2. `.github/instructions/*.instructions.md`

Path-specific instruction files. Each file can carry `applyTo` frontmatter
that limits its scope to matching file paths:

```yaml
---
applyTo: "src/main/java/**/*.java"
---
Keep controllers thin. Use constructor injection only.
```

When the user is editing a file that matches the glob, the instruction file
is merged into the prompt automatically. Files without `applyTo` apply
globally, similar to `copilot-instructions.md`.

### 3. `AGENTS.md`

The shared multi-CLI entrypoint recognized by Copilot and other coding
agents. Copilot reads `AGENTS.md` at the repository root as an additional
instruction surface. Use it for cross-agent rules that should be visible
to every CLI tool in the repository.

## Example of well-structured instructions

```markdown
## Architecture
- Keep business logic in `domain.service` classes.
- Controllers must not call repositories directly.

## Security
- Never log authentication tokens.
- Validate all user input at the controller boundary.

## Style
- Use constructor injection; do not use field injection.
```

Keep instructions declarative, concise, and non-contradictory.

## Merging and conflict avoidance

Copilot merges all matching instruction files into a single context window.
To avoid conflicts:

- Do not repeat the same rule in multiple files; place it in the
  most specific applicable surface.
- Use `copilot-instructions.md` for global rules and `*.instructions.md`
  for path-scoped overrides.
- If `AGENTS.md` already states a rule, do not duplicate it in
  `copilot-instructions.md` unless the Copilot-specific wording must differ.
- Review merged instructions periodically to prune stale or redundant entries.
