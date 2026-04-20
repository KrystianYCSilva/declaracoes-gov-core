<!--
SYNC IMPACT REPORT
Version change: [NEW] -> 1.0.0
List of modified principles:
  - Added: I. Declaration & Framework Agnostic
  - Added: II. Multi-Agent Shared Memory
  - Added: III. Test-Driven Integrity (90% Coverage)
  - Added: IV. Collaborative Agent Delegation
  - Added: V. Secure XML & Crypto Standards
Added sections:
  - Technical Constraints
  - Workflow Standards
Removed sections:
  - N/A (Initial Initialization)
Templates requiring updates:
  - ✅ updated: .specify/templates/plan-template.md
  - ✅ updated: .specify/templates/spec-template.md
  - ✅ updated: .specify/templates/tasks-template.md
Follow-up TODOs:
  - Ensure all agent-local-memory.md files are initialized in ./.gemini/memory/, ./.claude/memory/, etc.
-->

# declaracoes-gov-core Constitution

## Core Principles

### I. Declaration & Framework Agnostic
The core library MUST never include logic specific to a single declaration (e.g., eSocial, Reinf) or depend on high-level frameworks like Spring, Jakarta EE, or Quarkus. It must remain a pure Java 8 library to ensure maximum compatibility across the Brazilian fiscal ecosystem.

### II. Multi-Agent Shared Memory
The project utilizes a tiered memory architecture for multi-agent collaboration. The root `MEMORY.md` tracks cross-session status (Active/Completed). Each agent MUST maintain its own `memory/agent-local-memory.md` within its configuration directory (e.g., `.gemini/memory/`). Agents MUST synchronize these files at every workflow transition to prevent context drift and ensure seamless handoffs.

### III. Test-Driven Integrity (90% Coverage)
Validation is the only path to finality. JaCoCo coverage is strictly enforced at 90% (85% for the `crypto` module). Every code change MUST be accompanied by unit or integration tests. TDD is the preferred approach: write failing tests first, then implement.

### IV. Collaborative Agent Delegation
Efficiency is prioritized through the `multi-llm-cli-delegation` pattern. Agents should proactively delegate sub-tasks to other CLI agents when their specialized expertise or toolset (e.g., repository mapping, batch refactoring) offers better performance or context compression.

### V. Secure XML & Crypto Standards
All XML operations must be secure by default, preserving XXE protections. Signing MUST use RSA-SHA256 with inclusive canonicalization. Certificate and key access MUST be strictly isolated via the `CertificateProvider` abstraction. Real keys or certificates MUST NEVER be committed or logged.

## Technical Constraints

- **Baseline**: Strictly Java 8 (source/target 1.8). No Java 9+ features permitted.
- **Stack**: Maven multi-module, JUnit 4, Mockito, BouncyCastle, Jackson.
- **Domain**: Brazilian government fiscal identifiers (Cnpj, Cpf, Nis, etc.) are the primary immutable value objects.

## Workflow Standards

- **Spec-Driven**: All features follow the Spec Kit cycle: Constitution -> Spec -> Plan -> Tasks -> Implement.
- **Memory Sync**: Update `MEMORY.md` and local agent memory at every state transition.
- **Documentation**: Human-readable docs are in Portuguese (`docs/*.md`). AI-facing docs (including `GEMINI.md`) are in English. Implementation changes must sync both.

## Governance

- The Constitution takes absolute precedence over all other local practices or agent defaults.
- Amendments require a version bump:
  - **MAJOR**: Backward incompatible governance or principle redefinitions.
  - **MINOR**: New principles or materially expanded guidance.
  - **PATCH**: Clarifications and non-semantic refinements.
- Compliance is verified via `mvn verify` and manual peer (agent) review.

**Version**: 1.0.0 | **Ratified**: 2026-04-20 | **Last Amended**: 2026-04-20
