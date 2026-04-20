---
description: |
  GitHub Actions CI gate template for AI-assisted development.
  Use when: setting up CI/CD pipeline that validates AI agent output.
---

# CI Gate for AI Agent Workflow

## GitHub Actions Template

```yaml
name: AI Agent Quality Gate

on:
  pull_request:
    branches: [main, develop]

jobs:
  quality-gate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      # --- Java Project ---
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: '17'  # or '8' for legacy
          distribution: 'temurin'

      - name: Build and Test
        run: mvn verify --batch-mode --no-transfer-progress

      - name: Check Coverage Gate
        run: |
          # JaCoCo generates report in target/site/jacoco/jacoco.xml
          LINE_COVERAGE=$(grep -oP 'LINE.*?COVEREDRATIO.*?value="\K[^"]+' target/site/jacoco/jacoco.xml | head -1)
          echo "Line coverage: $LINE_COVERAGE"
          # Fail if below threshold (0.70 for brownfield, 0.80 for greenfield)
          python3 -c "assert float('${LINE_COVERAGE}') >= 0.70, f'Coverage {LINE_COVERAGE} below 70%'"

      # --- Node.js Project (alternative) ---
      # - name: Install and Test
      #   run: npm ci && npm test -- --coverage
      #
      # - name: Check Coverage
      #   run: |
      #     COVERAGE=$(npx jest --coverage --coverageReporters=json-summary | jq '.total.lines.pct')
      #     python3 -c "assert float('${COVERAGE}') >= 80.0, f'Coverage ${COVERAGE}% below 80%'"

      # --- Universal Checks ---
      - name: Check for Debug Artifacts
        run: |
          # Fail if agent left debug code
          ! grep -rn "System.out.println" src/main --include="*.java" || \
            { echo "ERROR: System.out.println found — use SLF4J"; exit 1; }
          ! grep -rn "console.log" src/ --include="*.ts" --include="*.js" || \
            { echo "ERROR: console.log found — use proper logger"; exit 1; }

      - name: Check for Test File Parity
        run: |
          # Every new source file should have a corresponding test
          for f in $(git diff --name-only --diff-filter=A origin/main -- '*.java' | grep 'src/main' | grep -v 'Test'); do
            test_file=$(echo "$f" | sed 's|src/main|src/test|' | sed 's|\.java|Test.java|')
            if [ ! -f "$test_file" ]; then
              echo "WARNING: No test for new file $f (expected $test_file)"
            fi
          done
```

## Branch Protection Rules (GitHub Settings)

```
Main branch protection:
  ✅ Require pull request before merging
  ✅ Require approvals: 1
  ✅ Require status checks to pass: "quality-gate"
  ✅ Require branches to be up to date
  ❌ Allow force pushes (never for AI agents)
```

## Workflow: Agent → Branch → CI → Review → Merge

```
1. Agent creates feature branch: git checkout -b feat/agent-task-001
2. Agent writes code + tests on the branch
3. Agent pushes and creates PR
4. CI runs quality-gate job automatically
5. If CI fails → agent fixes or human intervenes
6. If CI passes → human reviews diff
7. Human approves → merge to main
```
