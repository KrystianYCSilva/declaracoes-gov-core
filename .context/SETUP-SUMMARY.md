# Setup Summary: declaracoes-gov-core

## Completed Setup

Date: April 2026
Status: ✅ COMPLETE

## Files Created

### Root Level
- `AGENTS.md` - Multi-CLI entrypoint
- `.gitignore` - Updated with secrets exclusions

### AI Context (`.context/`)
- `README.md` - Navigation hub
- `ai-assistant-guide.md` - Operating guide

#### Meta (`_meta/`)
- `project-overview.md` - Project summary
- `tech-stack.md` - Technology stack
- `key-decisions.md` - Architectural decisions (ADRs)
- `codebase-map.md` - Package structure

#### Standards (`standards/`)
- `architectural-rules.md` - T0 enforcement rules
- `code-quality.md` - Style guidelines
- `testing-strategy.md` - Testing requirements

#### Patterns (`patterns/`)
- `architecture-patterns.md` - Design patterns

#### Workflows (`workflows/`)
- `development-workflow.md` - Development process

### Agent Configurations

| Agent | File | Status |
|-------|------|--------|
| Kimi | `.kimi/AGENTS.md` | ✅ Created |
| Gemini | `.gemini/GEMINI.md` | ✅ Created |
| Claude | `.claude/CLAUDE.md` | ✅ Created |
| Qwen | `.qwen/QWEN.md` | ✅ Created |
| CodeBuddy | `.codebuddy/CODEBUDDY.md` | ✅ Created |
| Codex | `.codex/AGENTS.md` | ✅ Created |
| OpenCode | `.opencode/AGENTS.md` | ✅ Created |
| Junie | `.junie/AGENTS.md` | ✅ Created |
| Vibe | `.vibe/AGENTS.md` | ✅ Created |
| Cursor | `.cursor/rules/00-project-bootstrap.mdc` | ✅ Created |
| Copilot | `.github/copilot-instructions.md` | ✅ Created |

## Key Adaptations for Gov-Core

### 1. Library Focus
Unlike NPD services (WAR applications), gov-core is a library (JAR):
- Emphasized framework agnosticism
- No Spring/Jakarta dependencies
- SPI pattern for extensibility

### 2. Certificate Domain
- A1 (file) and A3 (hardware token) certificate support
- ICP-Brasil validation
- mTLS connection factory

### 3. XML Signature
- eSocial/EFD-Reinf compliance
- RSA-SHA256, SHA-256, C14N, Enveloped
- Apache XML Security library

### 4. Document Validators
- CNPJ with alphanumeric support (2026+)
- CPF validation
- Inscrição Estadual state-specific rules

### 5. Java 8 Compatibility
- Explicit type declarations (no var)
- No Java 9+ Optional methods
- Source/target compatibility

## Tier System

| Tier | Files | Authority |
|------|-------|-----------|
| T0 | `standards/architectural-rules.md` | Absolute |
| T1 | `standards/`, `patterns/` | Normative |
| T2 | `_meta/` | Informative |
| T3 | `examples/` | Illustrative |

## Quality Gates

- `mvn verify` must pass
- 80% line coverage
- 75% branch coverage
- Checkstyle clean
- PMD clean

## Next Steps for Other Projects

1. Copy this structure to next project
2. Adapt content to specific domain
3. Update project name and descriptions
4. Adjust technology stack if needed
5. Keep T0 rules consistent across all projects

## Maintenance

Update this context when:
- New public APIs added
- Architecture decisions changed
- Technology stack updated
- New patterns introduced
- Build process modified
