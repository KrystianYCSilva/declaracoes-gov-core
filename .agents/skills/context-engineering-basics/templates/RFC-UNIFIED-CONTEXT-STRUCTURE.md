---
name: RFC-UNIFIED-CONTEXT-STRUCTURE
description: |
  RFC template for the unified .context/ directory structure standard for AI-assisted development.
  Use when: proposing or reviewing context architecture standards.
---

# RFC: Unified .context/ Structure for AI-Assisted Development

> **Version:** 1.0.0
> **Date:** 2026-01-16
> **Status:** APPROVED
> **Based on:** RFC-001, RFC-002, RFC-CONTEXT-ENGINEERING

---

## 1. Executive Summary

This RFC defines the standardized `.context/` directory layout for AI-assisted development projects. Two conformance levels are supported:

- **Basic Level:** Internal or low-criticality projects.
- **Full Level:** Client-facing or high-criticality projects.

---

## 2. Directory Structure

### 2.1. Basic Level (Minimum Required)

```
project-root/
├── AGENTS.md                    # Bootstrap (kernel)
├── MEMORY.md                    # Persistent state
│
└── .context/                    # AI context
    ├── README.md               # Navigation hub
    ├── _meta/
    │   └── tech-stack.md       # Technical stack
    └── standards/
        └── architectural-rules.md  # T0 rules
```

### 2.2. Full Level (Critical Projects)

```
project-root/
├── AGENTS.md                    # Bootstrap GitHub Copilot
├── CLAUDE.md                    # Bootstrap Claude
├── .cursorrules                 # Bootstrap Cursor IDE
├── MEMORY.md                    # Long-term persistent state
│
└── .context/
    ├── README.md               # Navigation hub
    ├── ai-assistant-guide.md   # Full AI protocol
    │
    ├── _meta/                  # T2: Context & Decisions
    │   ├── project-overview.md
    │   ├── tech-stack.md
    │   └── key-decisions.md    # Consolidated ADRs
    │
    ├── standards/              # T0-T1: Rules & Standards
    │   ├── architectural-rules.md  # T0: ABSOLUTE
    │   ├── code-quality.md         # T1: SOLID, Clean Code
    │   └── testing-strategy.md     # T1: TDD, coverage
    │
    ├── patterns/               # T1: Blueprints
    │   └── architectural-overview.md
    │
    ├── examples/               # T3: Code examples
    │   └── clean-architecture-structure.md
    │
    ├── workflows/              # Development workflows
    │   └── development-workflows.md
    │
    └── troubleshooting/        # Common issues
        └── common-issues.md
```

---

## 3. Tier System (Precedence Hierarchy)

The Tier System is MANDATORY at both levels:

| Tier | Kind | Authority | Overrides | Directory |
|------|------|-----------|-----------|-----------|
| **T0** | Enforcement | ABSOLUTE | All | `standards/architectural-rules.md` |
| **T1** | Standards | NORMATIVE | T2, T3 | `standards/`, `patterns/` |
| **T2** | Context | INFORMATIVE | T3 | `_meta/` |
| **T3** | Examples | ILLUSTRATIVE | None | `examples/` |

### Conflict Resolution Logic

```
IF T0 conflicts with any tier -> T0 WINS
IF T1 conflicts with T2 or T3 -> T1 WINS
IF T2 conflicts with T3        -> T2 WINS
ALWAYS cite the specific rule (ID) in the response
```

---

## 4. File Contents

### 4.1. AGENTS.md (Bootstrap - REQUIRED)

```markdown
# AI Development Kernel

**CRITICAL**: Before generating code, load context from `/.context/`

## Tier System
| Tier | File | Authority |
|------|------|-----------|
| T0 | `/.context/standards/architectural-rules.md` | ABSOLUTE |
| T1 | `/.context/standards/code-quality.md` | NORMATIVE |
| T2 | `/.context/_meta/` | INFORMATIVE |

## Quick Rules (T0)
[List 3-5 critical T0 rules here]

## State
Read `MEMORY.md` for current state.
```

### 4.2. MEMORY.md (State - REQUIRED)

```markdown
# MEMORY.md - AI Persistent State

## Last Updated
[DATE]

## Current State
**Project:** [NAME]
**Status:** [STATUS]

## Recent Actions
[Latest actions performed]

## Next Steps
[Upcoming steps]
```

### 4.3. .context/README.md (Hub - REQUIRED)

```markdown
# .context/ - AI Context Hub

## Quick Start
1. Read this file
2. Load `standards/architectural-rules.md` (T0)
3. Check `_meta/tech-stack.md` for project specifics

## Structure
- `_meta/` -> Project context (T2)
- `standards/` -> Rules and patterns (T0-T1)
- `patterns/` -> Blueprints (T1)
- `examples/` -> Code samples (T3)
```

### 4.4. standards/architectural-rules.md (T0 - REQUIRED)

```markdown
# Architectural Rules - T0 (Enforcement)

> **Tier**: T0 - ABSOLUTE. ALWAYS follow these rules.

## [CATEGORY]-[ID]: Rule Name

**Rule**: Clear description.

// CORRECT
[correct code]

// FORBIDDEN
[forbidden code]
```

### 4.5. ai-assistant-guide.md (Full Level)

Required contents:
- Bootstrap Sequence (3 steps)
- Request Classification (type -> files table)
- Tier System with resolution logic
- Definition of Done
- Research Methodology
- Available Agents

---

## 5. Multi-AI Bootstrap (Full Level)

### 5.1. CLAUDE.md

```markdown
# Claude AI - Context Engineering Setup

**CRITICAL**: Load `/.context/ai-assistant-guide.md` first.

## Essential Pointers
| Tier | File |
|------|------|
| T0 | `/.context/standards/architectural-rules.md` |
| T1 | `/.context/standards/code-quality.md` |
| T2 | `/.context/_meta/key-decisions.md` |
```

### 5.2. .cursorrules

```markdown
# Cursor IDE Rules

Load `/.context/ai-assistant-guide.md` before generating code.

## Mandatory Rules (T0)
[T0 rule list]

## Testing
- Line coverage >= 90%
- Branch coverage >= 80%
```

---

## 6. Definition of Done (Full Level)

| Metric | Minimum |
|--------|---------|
| **Line Coverage** | >= 90% |
| **Branch Coverage** | >= 80% |
| **1:1 Convention** | 1 test class : 1 impl class |
| **Documentation** | Up to date |

---

## 7. Conformance Checklist

### Basic Level

- [ ] AGENTS.md created
- [ ] MEMORY.md created
- [ ] .context/README.md created
- [ ] .context/_meta/tech-stack.md created
- [ ] .context/standards/architectural-rules.md created (minimum 5 T0 rules)

### Full Level (additional)

- [ ] CLAUDE.md created
- [ ] .cursorrules created
- [ ] .context/ai-assistant-guide.md created
- [ ] .context/_meta/project-overview.md created
- [ ] .context/_meta/key-decisions.md created
- [ ] .context/standards/code-quality.md created
- [ ] .context/standards/testing-strategy.md created
- [ ] .context/patterns/ populated
- [ ] .context/examples/ populated
- [ ] .context/workflows/ populated
- [ ] .context/troubleshooting/ populated
- [ ] Research methodology documented
- [ ] Definition of Done defined

---

## 8. References

- RFC-001: AI-Assisted Software Development Lifecycle
- RFC-002: Standardized Documentation Structure
- RFC-CONTEXT-ENGINEERING: Context Engineering for AIs

---

**Version:** 1.0.0
**Last Updated:** 2026-01-16
