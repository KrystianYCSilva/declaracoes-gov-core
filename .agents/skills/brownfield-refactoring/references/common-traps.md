---
description: |
  Five validated traps from real brownfield refactoring that cause agent failures.
  Use when: an agent creates tests that fail to compile, produces NoClassDefFoundError, or enters repair loops.
---

# Common Traps (Validated in 3 Projects)

## Trap 1: Java 25 Removes javax.xml.bind

**Symptom**: `mvn compile` fails with "package javax.xml.bind does not exist"

**Cause**: Java 11+ removed javax.xml.bind from the JDK. Hibernate's jpamodelgen annotation processor needs it at compile time.

**Fix**: Add to pom.xml:
```xml
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.9</version>
</dependency>
<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>
```

**Note**: Not all projects need this. SGE compiled without it, PDI required it. Check after adding JaCoCo.

## Trap 2: H2 Schema Must Match @Table Annotations

**Symptom**: Entity tests fail with "Schema X not found" or tables created in wrong schema.

**Cause**: Entities use `@Table(schema="ASP")` or `@Table(schema="GPE")`, but H2 doesn't create schemas automatically.

**Detection**:
```bash
grep -rn "@Table" src/java --include="*.java" | grep "schema"
```

**Fix**: Add INIT to H2 URL in hibernate-test.cfg.xml:
```
jdbc:h2:mem:test;MODE=DB2;INIT=CREATE SCHEMA IF NOT EXISTS ASP
```

Replace ASP with the schema found in @Table annotations. If multiple schemas exist:
```
INIT=CREATE SCHEMA IF NOT EXISTS ASP\;CREATE SCHEMA IF NOT EXISTS GPE
```

## Trap 3: NoClassDefFoundError ≠ "Class Doesn't Exist"

**Symptom**: Test compiles but fails at runtime with `NoClassDefFoundError` or `ExceptionInInitializerError`.

**Cause**: The class EXISTS in source and compiles in the WAR, but fails to LOAD in test context because a transitive dependency is missing. Most common: DTOs that import ZK UI components (Textbox, Longbox, Intbox).

**Detection**: Before testing any class, check its imports:
```bash
grep "^import" src/java/path/to/Class.java
```

If you see `org.zkoss.*`, `javax.servlet.*`, `javax.naming.*`, or `com.ibm.db2.*` → DO NOT TEST. Add to JaCoCo exclusions.

**This is not a compilation error. It's a classloading error. The distinction matters.**

## Trap 4: LLMs Fabricate Class and Method Names

**Symptom**: Test code references methods like `getNome()` when the actual method is `getDsNome()`, or imports a class from the wrong package.

**Cause**: LLMs generate test code from pattern recognition rather than source reading. They guess names based on conventions instead of reading the actual source file.

**Incidence**: ~100% when the agent is told "create tests for all entities" without explicit instruction to read source first.

**Fix**: ALWAYS include in agent prompts:
```
"Read every source file in [directory] before writing any test code. 
List the exact class name, package, constructor parameters, and all 
public method signatures. Then create tests using EXACTLY those names."
```

**This single instruction fixed the problem that caused 3 consecutive agent failures in SGCEX.**

## Trap 5: Parallel Agents Overwrite Each Other

**Symptom**: After running multiple agents, `git status` shows fewer changes than expected, or one agent's work has disappeared.

**Cause**: Multiple agents editing files in the same directory. Agent B's write overwrites Agent A's changes to the same file.

**Observed**: In SGCEX, 3 parallel cleanup agents ran on overlapping files. The controllers agent (42 min, 69 tool calls) overwrote work from the utils agent and DAO agent.

**Fix**: Partition by directory. Each agent gets exclusive ownership of specific files:
```
Agent 1: src/test/java/pkg/entidades/     (entity tests only)
Agent 2: src/test/java/pkg/DAO/           (DAO tests only)  
Agent 3: src/test/java/pkg/utilitarios/   (utility tests only)
```

**Additional rule**: If an agent runs >20 minutes or >40 tool calls, it's likely in a repair loop. Abort and redo with more explicit instructions.
