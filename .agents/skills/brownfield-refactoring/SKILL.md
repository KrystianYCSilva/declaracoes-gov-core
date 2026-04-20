---
name: brownfield-refactoring
description: |
  Guide brownfield refactoring of legacy Java/Hibernate projects with TDD, JaCoCo coverage gates, and H2 test infrastructure.
  Use when: refactoring legacy Java code, adding tests to untested projects, setting up JaCoCo coverage, migrating from Ant to Maven, or creating H2-backed integration tests for DB2/Oracle projects.
activation: Auto
estimated_tokens: 1170
---

# Brownfield Refactoring

This skill captures operational patterns validated across 3 real legacy projects (1,582 tests, 70%+ coverage each).
It is not theory — every rule here comes from a failure that was fixed.

## How to Set Up Test Infrastructure

The infrastructure is always the same. Do this BEFORE writing any test.

1. Add to pom.xml: H2 2.2.224, JUnit 5.12.2, Mockito 4.11.0, JaCoCo 0.8.12, Surefire 3.2.5
2. Create `src/test/resources/hibernate-test.cfg.xml` with H2 in DB2-compat mode
3. Add `initForTest(String configFile)` method to the project's HibernateUtil
4. Create `HibernateTestSupport` base class with auto-rollback per test
5. Run `mvn test-compile` — must compile clean before proceeding

Load `templates/pom-additions.xml` for exact dependency and plugin XML.
Load `templates/hibernate-test-cfg.xml` for the H2 configuration template.
Load `templates/test-support.java` for HibernateUtil and HibernateTestSupport code.

## How to Decide What to Exclude from Coverage

Exclude code that CANNOT run without external infrastructure. Never force fragile tests.

Read the source file. If it imports any of these, EXCLUDE from JaCoCo:
- `org.zkoss.*` → needs ZK container
- `javax.servlet.*` → needs servlet container
- `javax.naming.*` → needs JNDI
- `com.ibm.db2.*` → needs DB2 driver
- LDAP, SMTP, or session-dependent classes

Load `references/exclusion-patterns.md` for the full exclusion pattern list and JaCoCo XML syntax.
Load `references/empirical-evidence.md` for quantified proof of what works vs what fails across 10 sessions and 1,582 tests.

## How to Write Tests That Work

The golden rule: **read every source file before writing a single line of test code.**

LLMs consistently fabricate class names, method names, and field names. This causes NoClassDefFoundError and compilation failures. The fix is explicit:

1. List all files in the target package: `find src/java/pkg/ -name "*.java"`
2. Read each file. Note exact class name, package, constructor, getters/setters
3. Check imports — if a class imports ZK/Servlet/JNDI, skip it
4. Only then write the test, using EXACTLY the names you found
5. Run `mvn test-compile` before running tests

Load `references/common-traps.md` for the 5 validated traps and their fixes.
Load `references/zone-classification.md` for the 🟢🟡🔴 zone system to classify code before refactoring.

## How to Sequence the Refactoring

Always follow this order. Each phase must pass `mvn test` before proceeding to the next.

```
Phase 0: Infrastructure (pom.xml + hibernate-test.cfg + test support)  → mvn test-compile
Phase 1: Entity tests (persist/retrieve for each entity)               → mvn test
Phase 2: DAO tests (CRUD via H2 for testable DAOs)                     → mvn test
Phase 3: Service + Utility tests (pure logic, mock externals)          → mvn test
Phase 4: Coverage gate (add tests OR add exclusions until ≥70%)        → mvn verify
```

Never "refactor everything at once." One layer at a time.

## How to Use Parallel Agents Safely

Parallel agents work ONLY when each agent operates on completely different files.

```
✅ Safe:  Agent A → entity tests | Agent B → DAO tests | Agent C → utility tests
❌ Unsafe: Agent A → cleanup controllers | Agent B → cleanup controllers
```

Never let two agents edit the same package. They will overwrite each other's work.

## How to Handle the Coverage Gap

If after Phase 3 coverage is below 70%:

1. Check JaCoCo report — identify packages with most missed lines
2. For untestable packages (ZK imports, native SQL): add to JaCoCo exclusions
3. For testable packages: add targeted getter/setter tests, constructor tests, or additional method tests
4. Repeat until ≥70%

The goal is 70% of TESTABLE code, not 70% of ALL code. Honest exclusions > fragile tests.

## How to Validate Agent Work

After ANY agent creates or modifies test files:

1. `mvn test-compile` — catches fabricated class names immediately
2. `mvn test` — catches runtime failures (NoClassDefFoundError, wrong assertions)
3. If either fails: delete agent's files and redo with more explicit instructions

Never trust agent output without validation. Enforcement > trust.
