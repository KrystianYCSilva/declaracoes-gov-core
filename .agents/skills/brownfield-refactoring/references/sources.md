---
description: |
  Evidence base for this skill's recommendations.
  Use when: justifying a decision or explaining why a particular pattern was chosen.
---

# Sources and Evidence

## Validated Projects

| Project | Stack | Entities | Tests Before | Tests After | Coverage | Traps Hit |
|---------|-------|----------|-------------|-------------|----------|-----------|
| SGE | Java 8 / Hibernate 4.3 / ZK / DB2 | 10 | 104 | 573 | 70.0% | JAXB not needed, JUnit 4.10→4.13.2 |
| PDI | Java 8 / Hibernate 4.3 / ZK / DB2 | 37 | 4 | 428 | 70.9% | JAXB required, ASP schema, 3 parallel agents OK |
| SGCEX | Java 8 / Hibernate 4.3 / ZK / DB2 | 57 | 135 | 581 | 73.1% | DTOs with ZK imports, 3 agent failures, parallel overwrite |

## Key Findings

1. **Template replication works**: Infrastructure created once in SGE was replicated to PDI and SGCEX with minor adaptations
2. **Exclusion > forced tests**: 70% of testable code is more sustainable than 40% of all code
3. **Agent name fabrication**: 100% incidence rate when agent is not explicitly told to read source files first
4. **Parallel safety**: Agents on disjoint packages = safe. Agents on same files = destructive.
5. **Script vs agent for mechanical tasks**: PowerShell regex (5 seconds) > LLM agent (42 minutes with errors) for System.out→SLF4J

## Prior Approaches Tested and Discarded

| Approach | Files | Result |
|----------|-------|--------|
| Full governance templates | 173 templates per project | Token overflow, LLM lost context |
| Schema + AST framework | 500+ files | Too complex, nobody maintained it |
| Skill generator framework | 1,893 files | Context bloat |
| Dedicated scanner CLI | 150 tests, 7 commands | Not used in practice — grep + mvn test was faster |
| 4,000 JIT skills | 158+ skill files | ~200K tokens discovery overhead |

## The Formula

```
AGENTS.md (context the LLM reads) + Code (example the LLM copies) + CI/CD (gate the LLM can't bypass)
```

This skill IS the AGENTS.md layer. The project's existing code IS the example layer. JaCoCo + mvn test IS the CI/CD layer.
