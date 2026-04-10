# AGENTS

This file is the shared multi-CLI entrypoint for the repository.
CLI-specific primary instruction files live in the native tool directories:

- Codex: `.codex/AGENTS.md`
- Claude: `.claude/CLAUDE.md`
- Cursor: `.cursor/rules/00-project-bootstrap.mdc`
- Gemini: `.gemini/GEMINI.md`
- Qwen: `.qwen/QWEN.md`
- Kimi: `.kimi/AGENTS.md`
- OpenCode: `.opencode/AGENTS.md`
- Junie: `.junie/AGENTS.md`
- Vibe: `.vibe/AGENTS.md`
- CodeBuddy: `.codebuddy/CODEBUDDY.md`
- Copilot: `.github/copilot-instructions.md`

All LLM-facing files in this repository must stay in English:
- main instruction files
- skills
- commands
- agents and subagents
- prompt assets

## AI Context Bootstrap

Before generating code, tests, or AI-facing documentation, load the AI context in this order:
1. `.context/README.md`
2. `.context/ai-assistant-guide.md`
3. `.context/standards/architectural-rules.md`

Then load only the task-specific files needed for the current request.

## AI Context Tier System

| Tier | Purpose | Authority | Files |
|------|---------|-----------|-------|
| `T0` | Enforcement | Absolute | `.context/standards/architectural-rules.md` |
| `T1` | Standards and patterns | Normative | `.context/standards/code-quality.md`, `.context/standards/testing-strategy.md`, `.context/patterns/` |
| `T2` | Project context | Informative | `.context/_meta/` |
| `T3` | Examples | Illustrative | `.context/examples/` |

Conflict resolution:
- If `T0` conflicts with any other tier, `T0` wins.
- If `T1` conflicts with `T2` or `T3`, `T1` wins.
- If `T2` conflicts with `T3`, `T2` wins.
- If `.context/`, `docs/`, and `src/` disagree, confirm the current truth in `src/`, then update `docs/`, then update `.context/`.

## AI Context Index

Bootstrap and navigation:
- `.context/README.md`
- `.context/ai-assistant-guide.md`

Project metadata:
- `.context/_meta/project-overview.md`
- `.context/_meta/tech-stack.md`
- `.context/_meta/key-decisions.md`
- `.context/_meta/codebase-map.md`

Standards:
- `.context/standards/architectural-rules.md`
- `.context/standards/code-quality.md`
- `.context/standards/testing-strategy.md`

Patterns:
- `.context/patterns/architecture-patterns.md`
- `.context/patterns/testing-and-tdd.md`

Workflows:
- `.context/workflows/development-workflow.md`
- `.context/workflows/testing-and-validation-workflow.md`
- `.context/workflows/context-sync-workflow.md`

Troubleshooting:
- `.context/troubleshooting/common-issues.md`

## AI Context Routing

- For certificate or signature changes: load `.context/standards/architectural-rules.md`, `.context/_meta/key-decisions.md`, and `.context/workflows/development-workflow.md`.
- For validator changes (CNPJ/CPF): load `.context/_meta/codebase-map.md`, `.context/patterns/architecture-patterns.md`, `.context/standards/testing-strategy.md`.
- For JSON utilities: load `.context/_meta/tech-stack.md`, `.context/patterns/architecture-patterns.md`.
- For testing work: load `.context/standards/testing-strategy.md`, `.context/patterns/testing-and-tdd.md`, and `.context/workflows/testing-and-validation-workflow.md`.
- For review or QA: load `.context/workflows/review-qa-and-release-workflow.md` and `.context/troubleshooting/common-issues.md`.

## Context Synchronization Rule

- `src/` is the live implementation.
- `docs/` is the human-facing record and must remain in Portuguese.
- `.context/` is the AI-facing compressed record and must remain in English.
- Any change to behavior, architecture, tests, profiles, or operational setup must keep `src/`, `docs/`, and `.context/` synchronized.

## Project

- Name: `declaracoes-gov-core`
- Baseline release: `1.0.0`
- Type: `Java Library (JAR)`
- Domain: Core shared functionality for Brazilian tax declarations - certificates, XML signature, document validators, JSON utilities

## Stack

- Java 8+ (source/target compatibility)
- Maven 3.9+
- JAXB (XML binding)
- Apache XML Security 3.x (XML digital signature)
- Jackson 2.x (JSON processing - optional/provided)
- Apache HttpClient 5.x (HTTP - optional/provided)
- Caffeine 3.x (caching - optional)
- JUnit 5 + Mockito (testing)
- JaCoCo (coverage)

## Architecture Rules

- Keep library agnostic - no Spring/Jakarta EE dependencies in core.
- Keep business logic in domain services (certificate, signature, validation).
- Keep models immutable where possible.
- All public APIs must be thread-safe.
- Maintain Java 8 source compatibility - no var, no new Optional methods.
- Minimize external dependencies - mark optional deps as `<optional>true</optional>`.
- Provide SPI interfaces for extensibility.

## Security & Certificates

- Certificate types supported: A1 (.p12/.pfx), A3 (token/smartcard via PKCS#11)
- ICP-Brasil chain validation required.
- XML Signature: RSA-SHA256, SHA-256, C14N canonicalization, Enveloped transform.
- Never commit real certificates or keys.
- Test keystores must use dummy credentials only.

## Quality Gate

- `mvn verify` must stay green.
- Minimum coverage:
  - `80%` line
  - `75%` branch
- Zero warnings from Checkstyle and PMD.

## Useful Commands

- Compile: `mvn -q -DskipTests compile`
- Test: `mvn -q test`
- Validate: `mvn -q verify`
- Package: `mvn -q -DskipTests package`
- Checkstyle: `mvn -q checkstyle:check`
- PMD: `mvn -q pmd:check`

## Shared References

- `.context/_meta/project-overview.md`
- `.context/_meta/tech-stack.md`
- `.context/_meta/key-decisions.md`
- `.context/_meta/codebase-map.md`
- `.kimi/plan/KIMI-PLAN.md`

## Retired Items

(none yet - new project)
