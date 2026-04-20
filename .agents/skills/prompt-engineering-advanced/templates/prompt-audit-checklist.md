---
description: |
  5-point quality gate checklist for evaluating prompts before use.
  Use when: reviewing or improving a prompt for AI agent tasks.
---

# Prompt Audit Checklist

Score each criterion 0-2 (0 = missing, 1 = partial, 2 = complete). Target: ≥ 8/10.

## 1. Clarity (Is the task unambiguous?)

- [ ] Uses imperative mood ("Create X", not "Could you create X?")
- [ ] Specifies the EXACT output expected (file, format, structure)
- [ ] No jargon or acronyms without definition
- **Score: ___/2**

## 2. Constraints (Are boundaries explicit?)

- [ ] Language/framework version specified
- [ ] What NOT to do is stated (negative constraints)
- [ ] Scope boundaries clear (in-scope vs out-of-scope)
- **Score: ___/2**

## 3. Context (Does the LLM have what it needs?)

- [ ] Relevant files referenced ("Read src/model/User.java first")
- [ ] Existing patterns pointed to ("Follow the pattern in EntityTest.java")
- [ ] ≤ 7 instructions (cognitive load managed)
- **Score: ___/2**

## 4. Examples (Is there at least one?)

- [ ] Input/output example provided
- [ ] OR existing code file referenced as template
- [ ] Format/structure shown, not just described
- **Score: ___/2**

## 5. Verification (Can success be measured?)

- [ ] Success criteria defined ("tests pass", "coverage ≥ 70%", "compiles clean")
- [ ] Validation command specified (`mvn test`, `npm test`, `pytest`)
- [ ] Failure recovery stated ("if compilation fails, delete and retry")
- **Score: ___/2**

## Total: ___/10

| Score | Verdict | Action |
|-------|---------|--------|
| 9-10 | ✅ Ship it | Use as-is |
| 7-8 | ⚠️ Good enough | Minor refinement |
| 5-6 | ❌ Risky | Add missing constraints and examples |
| 0-4 | 🚫 Don't use | Rewrite from scratch |
