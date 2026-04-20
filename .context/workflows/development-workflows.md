---
description: |
  Development and operational workflows for declaracoes-gov-core.
  Use when: onboarding, building, testing, publishing, or troubleshooting.
---

# Development Workflows — declaracoes-gov-core

## Onboarding

1. Clone repository.
2. Ensure Java 8 and Maven 3.x are installed.
3. Run `mvn -q verify` to validate the full reactor.
4. Read `AGENTS.md` and `.context/README.md`.
5. Check `MEMORY.md` for active work.

## Build Workflows

### Full Validation (CI Gate)
```bash
mvn -q verify
```
Compiles, runs tests, generates JaCoCo reports, enforces coverage gates.

### Single Module
```bash
cd declaracoes-gov-core-domain && mvn -q verify
```

### Single Test Class
```bash
mvn -q test -pl declaracoes-gov-core-domain -Dtest=CnpjTest
```

### Local Install (Skip Tests)
```bash
mvn -q install -DskipTests
```

### Skip JaCoCo (Local Only)
```bash
mvn verify -Djacoco.skip=true
```

## Adding a New Fiscal Identifier

1. Create value object in `core-domain` under `br.uem.npd.govcore.model`.
2. Add validator in `br.uem.npd.govcore.validator` (respect confidence tier).
3. Register in `GovValidationCatalog`.
4. Add unit tests in `src/test/java` mirroring the package.
5. Ensure JaCoCo gate passes.
6. Update `docs/05-MATRIZ-VALIDADORES.md` (human docs).
7. Update `knowledge/domain-concepts.md` (AI docs).
8. Update `MEMORY.md` with completion status.

## Adding a New Module Dependency

1. Check `standards/architectural-rules.md` AR-003.
2. Verify the dependency does not leak heavy libraries.
3. Update `_meta/tech-stack.md`.
4. Update `pom.xml` in the affected module and parent.
5. Run full reactor validation.

## Coverage Recovery

If `mvn -q verify` fails on JaCoCo:

1. Run `mvn jacoco:report` to see the HTML report.
2. Identify uncovered lines/branches.
3. Add targeted tests (not just filler).
4. Re-run `mvn -q verify`.

## Publication Order

1. Align versions in parent and all child POMs.
2. Run `mvn verify`.
3. Publish concrete JARs (`domain`, `format`, `crypto`, `xml`).
4. Publish `declaracoes-gov-core-bom` last.

## Troubleshooting

| Symptom | Cause | Fix |
|---------|-------|-----|
| `mvn verify` fails on crypto line coverage | Hardware-dependent tests skipped | Add more unit tests with mocked providers |
| Negative XML parser tests write to stderr | By design (XXE attack simulation) | Ignore; build passes if exit code is 0 |
| `test-jar` not found in xml tests | Crypto module not built | Build reactor with `mvn -q install` first |
| `Cnpj.of("00.000.000/0000-00")` throws | Known invalid pattern rejected | Use valid test data |
