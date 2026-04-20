---
description: |
  3-zone classification system for brownfield code before refactoring.
  Use when: deciding which code to refactor, which to test first, and which to leave untouched.
  Based on empirical boundary classification from legacy Java refactoring projects.
---

# Refactoring Zone Classification

Before touching any code, classify every package/module into 3 zones:

## 🟢 Safe Zone — Refactor Freely

**Criteria:**
- Has existing tests (any amount)
- Low coupling (few external dependencies)
- No credentials or security-sensitive code
- Clear business logic

**Action:** Refactor, add tests, improve — standard TDD workflow.

## 🟡 Caution Zone — Tests First, Then Refactor

**Criteria:**
- NO existing tests
- Isolated (few or no other packages depend on it)
- Contains business logic that's testable
- No credentials

**Action:**
1. Write characterization tests FIRST (capture current behavior)
2. Verify characterization tests pass
3. Only THEN refactor
4. Verify characterization tests still pass after refactoring

## 🔴 Forbidden Zone — Do NOT Modify Without Approval

**Criteria:**
- NO tests AND high coupling (many packages depend on it)
- Contains credentials, security config, or auth logic
- Touches production database connections
- ZK/Servlet/JNDI-dependent code that can't be tested without containers

**Action:**
- Do NOT modify without explicit stakeholder approval
- If modification is required: write characterization tests first, get approval, then proceed
- Exclude from JaCoCo coverage (don't force fragile tests)

## How to Classify (Commands)

```bash
# Find packages with tests (🟢 candidates)
find src/test -name "*Test.java" | sed 's|src/test|src/main|' | sed 's|Test.java|.java|' | sort > tested.txt
find src/main -name "*.java" | sort > all.txt
comm -23 all.txt tested.txt > untested.txt

# Find high-coupling packages (🔴 candidates)
for pkg in $(find src/main -type d); do
  imports=$(grep -rn "import " $pkg --include="*.java" | grep -v "java\." | grep -v "javax\." | wc -l)
  echo "$imports $pkg"
done | sort -rn | head -20

# Find credential/security files (🔴 mandatory)
grep -rn "password\|credential\|secret\|token\|apikey" src/main --include="*.java" -l

# Find ZK-dependent files (🔴 or exclude)
grep -rn "import org.zkoss" src/main --include="*.java" -l
```

## Zone Assignment Template

| Package | Tests? | Coupling | Sensitive? | Zone | Action |
|---------|--------|----------|-----------|------|--------|
| model/ | No | Low | No | 🟡 | Tests first |
| dao/ | No | Medium | No | 🟡 | Tests first |
| service/ | Yes (3) | Medium | No | 🟢 | Refactor |
| controller/ | No | High (ZK) | No | 🔴 | Exclude |
| auth/ | No | High | Yes | 🔴 | Don't touch |
| util/ | No | Low | No | 🟡 | Tests first |
