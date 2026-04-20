## Description
<!-- Brief description of the change -->

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Refactor
- [ ] Dependency upgrade
- [ ] Documentation update

## Checklist

### T0 Compliance (AR-001..AR-009)
- [ ] No declaration-specific payload DTOs or transport logic added (AR-001)
- [ ] No Spring, Jakarta EE, Lombok, or Bean Validation added (AR-002)
- [ ] Module boundaries respected; heavy deps stay in owning module (AR-003)
- [ ] No Java 9+ language features or APIs used (AR-004)
- [ ] Validator confidence model preserved (OFFICIAL/PROVISIONAL/STRUCTURAL) (AR-005)
- [ ] XML signing explicit; XXE protections preserved (AR-006)
- [ ] Certificate handling isolated; no real keys committed (AR-007)

### Testing & Coverage
- [ ] `mvn -q verify` passes locally
- [ ] New code has unit tests (`*Test.java` in matching package)
- [ ] JaCoCo line coverage ≥ 90% (domain/format/xml) or ≥ 85% (crypto)
- [ ] JaCoCo branch coverage ≥ 90% (all modules)

### Documentation & Context
- [ ] Javadoc/comments in Portuguese (if changed)
- [ ] AI docs (`.context/`, `AGENTS.md`) updated if behavior changed
- [ ] `MEMORY.md` updated if this is an active or completed task
- [ ] `docs/05-MATRIZ-VALIDADORES.md` updated if validators changed

### Review Notes
<!-- Anything the reviewer should know -->
