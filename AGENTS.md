# AGENTS

This file is the shared multi-CLI entrypoint for `declaracoes-gov-core`.

Primary tool entrypoints live in:

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

All AI-facing files in this module must stay in English.

## AI Context Bootstrap

Load in this order:

1. `.context/README.md`
2. `.context/_meta/project-overview.md`
3. `.context/_meta/tech-stack.md`
4. `.context/standards/architectural-rules.md`
5. `.context/ai-assistant-guide.md` when you need routing or done criteria.

## AI Context Tier System

| Tier | Purpose | Authority | Files |
| --- | --- | --- | --- |
| `T0` | Enforcement | Absolute | `.context/standards/architectural-rules.md` |
| `T1` | Standards and patterns | Normative | `.context/standards/`, `.context/patterns/` |
| `T2` | Project context and workflows | Informative | `.context/_meta/`, `.context/workflows/` |
| `T3` | Examples | Illustrative | Not used in this module today |

Conflict resolution:

- If AI docs, Portuguese human docs, and child-module code disagree, confirm the truth in `declaracoes-gov-core-*/src` and the relevant `pom.xml`, then sync human docs, then AI docs.
- Never describe provisional validator behavior as official.

## AI Context Index

Bootstrap and navigation:

- `.context/README.md`
- `.context/ai-assistant-guide.md`

Project metadata:

- `.context/_meta/project-overview.md`
- `.context/_meta/tech-stack.md`
- `.context/_meta/codebase-map.md`
- `.context/_meta/key-decisions.md`

Standards:

- `.context/standards/architectural-rules.md`
- `.context/standards/code-quality.md`
- `.context/standards/testing-strategy.md`

Patterns:

- `.context/patterns/architecture-patterns.md`

Workflows:

- `.context/workflows/development-workflow.md`

## AI Context Routing

- For validator or value-object changes: load `.context/_meta/codebase-map.md`, `.context/_meta/key-decisions.md`, and `.context/standards/testing-strategy.md`.
- For format, parser, or `GovJsonFactory` changes: load `.context/_meta/tech-stack.md`, `.context/_meta/codebase-map.md`, and `.context/patterns/architecture-patterns.md`.
- For XML signing or DOM utility changes: load `.context/standards/architectural-rules.md`, `.context/_meta/codebase-map.md`, and `.context/_meta/key-decisions.md`.
- For certificate, PKCS11, PKCS12, or `SSLContext` changes: load `.context/standards/architectural-rules.md`, `.context/_meta/tech-stack.md`, and `.context/_meta/key-decisions.md`.
- For documentation or synchronization work: load `.context/ai-assistant-guide.md` and `.context/workflows/development-workflow.md`.

## Context Synchronization Rule

- The live implementation is the root `pom.xml`, the child-module `pom.xml` files, and `declaracoes-gov-core-*/src`.
- Human docs in the module root and `docs/` remain in Portuguese.
- AI docs (`AGENTS.md`, tool shims, `.context/`) remain in English.
- Any change to behavior, public API, validation policy, build gates, or module boundaries must keep those sources synchronized.

## Project

- Name: `declaracoes-gov-core`
- Type: Maven multi-module core library (root packaging `pom`)
- Baseline release: `1.0.0`
- Modules: `declaracoes-gov-core-domain`, `declaracoes-gov-core-format`, `declaracoes-gov-core-xml`, `declaracoes-gov-core-crypto`, `declaracoes-gov-core-bom`
- Domain: shared Brazilian fiscal domain, formatting, XML, and certificate utilities reused by declaration-specific modules

## Stack

- Java 8 source/target
- Maven reactor build
- Jackson 2.16.1 (optional in `format`)
- Apache Santuario `xmlsec` 3.0.3 (XML module dependency)
- JUnit 4.13.2, Mockito 4.11.0, BouncyCastle 1.70 (test scope)

## Architecture Rules

- Keep the core declaration-agnostic and framework-agnostic.
- Keep transport concerns (SOAP, REST, OAuth2, HTTP clients) out of this module.
- Preserve the published validator confidence model: `OFFICIAL`, `PROVISIONAL`, and `STRUCTURAL`.
- Keep `Cnpj` dual-mode support (numeric and alphanumeric) and keep `Cpf` / `Nis` provisional algorithms opt-in only.
- Never commit real certificates, private keys, or token configuration secrets.

## Quality Gate

- `mvn -q verify` must stay green.
- JaCoCo defaults come from the parent POM: `90%` line / `90%` branch, with a documented `declaracoes-gov-core-crypto` line exception at `85%`.

## Useful Commands

- `mvn -q test`
- `mvn -q verify`
- `mvn -q -DskipTests compile`
