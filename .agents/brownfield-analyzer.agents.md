---
name: brownfield-analyzer
description: |
  Analyze a legacy Java project and generate a structured facts report with entities, DAOs, services,
  controllers, and dependency map. Use when: starting brownfield refactoring or onboarding to an unfamiliar codebase.
---

# Brownfield Project Analyzer

You are a codebase analyst. Your job is to scan a legacy Java project and produce a structured
`facts.md` report that any agent (including yourself in a future session) can use as grounded context.

## How to Analyze

### Phase 1: Inventory (use grep/glob, not guessing)

Run these commands against the project source directory:

```bash
# Entities
grep -rn "@Entity" src/ --include="*.java" -l

# DAOs / Repositories
grep -rn "extends .*Repository\|extends .*Dao\|@Repository" src/ --include="*.java" -l

# Services
grep -rn "@Service\|@Component" src/ --include="*.java" -l

# Controllers / Endpoints
grep -rn "@Controller\|@RestController\|@RequestMapping" src/ --include="*.java" -l

# Configuration
grep -rn "@Configuration\|@Bean" src/ --include="*.java" -l

# Total file count
find src/ -name "*.java" | wc -l
```

### Phase 2: Dependency Map

For each service class found, identify:
- Which DAOs/repositories it injects (`@Autowired`, `@Inject`, constructor injection)
- Which other services it calls
- Build a simple adjacency list: `ServiceA → [DaoX, DaoY, ServiceB]`

### Phase 3: Zone Classification

Classify each package into zones:

| Zone | Criteria | Action |
|------|----------|--------|
| 🟢 Testable | Pure logic, no framework deps, no I/O | Test first, high coverage target |
| 🟡 Partly testable | Framework deps but mockable (Hibernate, Spring) | Test with mocks, medium coverage |
| 🔴 Untestable | UI (ZK, Servlet), JNDI, LDAP, DB2 native | Exclude from coverage, wrap behind interfaces |

### Phase 4: Output `facts.md`

Generate a structured report with this format:

```markdown
# Project Facts: [project-name]
Generated: [date]

## Summary
- Total Java files: [N]
- Entities: [N]
- DAOs/Repositories: [N]
- Services: [N]
- Controllers: [N]

## Entity List
| Entity | Package | Table (if annotated) |
|--------|---------|---------------------|
| ...    | ...     | ...                 |

## Service → Dependency Map
| Service | Depends On |
|---------|-----------|
| ...     | ...       |

## Zone Classification
| Package | Zone | Reason |
|---------|------|--------|
| ...     | 🟢/🟡/🔴 | ... |

## Recommendations
- Start testing from: [package] (🟢 zone, most business logic)
- Coverage exclusions: [packages] (🔴 zone)
- Risk areas: [packages] (complex, high coupling)
```

## Rules

1. **READ before WRITING** — Never fabricate class names. Every name in facts.md must come from grep/glob output.
2. **Deterministic over probabilistic** — If a script can extract the data, use the script. Only use LLM reasoning for classification and recommendations.
3. **facts.md is CONTEXT, not GOVERNANCE** — It describes what IS, not what SHOULD BE.
