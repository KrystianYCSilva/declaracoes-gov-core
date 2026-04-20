---
description: |
  Quick inventory commands for brownfield Java projects.
  Use when: starting a new brownfield refactoring to understand the codebase before writing any tests.
---

# Project Inventory Commands

Run these before starting any refactoring. They provide the same inventory a scanner CLI would.

## Entities
```bash
grep -rn "@Entity" src/java --include="*.java" -l | wc -l
grep -rn "@Entity" src/java --include="*.java" -l
```

## Schemas (for H2 config)
```bash
grep -rn "@Table" src/java --include="*.java" | grep "schema" | sed 's/.*schema.*"\([^"]*\)".*/\1/' | sort -u
```

## DAOs
```bash
find src/java -name "*DAO.java" -o -name "*Dao.java" -o -name "*Repository.java" | wc -l
```

## Controllers
```bash
find src/java -name "*Controller.java" -o -name "*Controlador.java" | wc -l
```

## ZK-dependent classes (candidates for JaCoCo exclusion)
```bash
grep -rn "import org.zkoss" src/java --include="*.java" -l | wc -l
grep -rn "import org.zkoss" src/java --include="*.java" -l
```

## Code smells
```bash
echo "System.out.println:"; grep -rn "System.out.println" src/java --include="*.java" | wc -l
echo "Messagebox.show:"; grep -rn "Messagebox.show" src/java --include="*.java" | wc -l
echo "e.printStackTrace:"; grep -rn "\.printStackTrace()" src/java --include="*.java" | wc -l
```

## Existing tests
```bash
find src/test -name "*Test.java" -o -name "*Tests.java" 2>/dev/null | wc -l
```

## Build system
```bash
echo "Maven:"; test -f pom.xml && echo "YES" || echo "NO"
echo "Ant:"; test -f build.xml && echo "YES (remove it)" || echo "NO"
echo "Gradle:"; test -f build.gradle && echo "YES" || echo "NO"
```
