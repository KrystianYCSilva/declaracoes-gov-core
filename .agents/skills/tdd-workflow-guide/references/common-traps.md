---
description: |
  Common TDD traps when working with LLM agents.
  Use when: debugging TDD workflow failures or reviewing agent-generated tests.
---

# Common TDD Traps

## Trap 1: Green Without Red

**Symptom:** Agent writes test + implementation simultaneously. Test was never red. You don't know if the test actually validates the behavior.

**Cause:** LLMs optimize for "working code" — writing a failing test feels wasteful to them.

**Fix:**
- Split the prompt: "Write ONLY the test. Do NOT implement."
- Verify the test fails: `mvn test` must show RED before proceeding
- Only then: "Now implement the minimum to make the test pass."
- If agent bundles test + implementation, reject and re-prompt

---

## Trap 2: Testing the Implementation, Not the Behavior

**Symptom:** Tests break when you refactor internals even though public behavior didn't change. Test verifies `verify(mockDao).save(entity)` instead of verifying the business outcome.

**Cause:** LLMs generate tests that mirror the implementation structure, including internal method calls.

**Fix:**
- Test the WHAT, not the HOW
- ❌ `verify(dao, times(1)).save(any())` — tests implementation
- ✅ `assertEquals(expectedResult, service.calculate(input))` — tests behavior
- Mock only external boundaries (DB, network, file system), not internal collaborators

---

## Trap 3: One Giant Test Instead of Many Small Tests

**Symptom:** One test method with 200 lines testing 15 different behaviors. If it fails, you don't know which behavior broke.

**Cause:** LLM generates a "comprehensive test" to cover everything at once.

**Fix:**
- One test = one behavior = one assertion (ideally)
- Test name must describe the specific behavior: `givenExpiredToken_whenAuth_thenThrows`
- If a test has more than 3 assertions, split it into separate test methods
- Each test must be independently runnable (no order dependency)

---

## Trap 4: Tautological Tests

**Symptom:** Tests pass but prove nothing. `assertEquals(result, result)`, `assertNotNull(new Object())`, `assertTrue(true)`.

**Cause:** Agent was asked for X% coverage and generated tests that execute lines without verifying behavior.

**Fix:**
- Every test must assert a BUSINESS value against an EXPECTED value
- Ban patterns: `assertNotNull(new X())`, `assertTrue(list.size() >= 0)`, `assertEquals(x, x)`
- Mutation testing catches these: change production code → if no test breaks → test is useless
- Review: if removing the test wouldn't reduce confidence in the code, delete it

---

## Trap 5: Skipping Refactor Phase

**Symptom:** Code works (green) but is messy — duplicated logic, magic numbers, poor names. Agent moves to next feature instead of cleaning up.

**Cause:** LLM treats "test passes" as "done." The refactor phase requires aesthetic judgment that LLMs deprioritize.

**Fix:**
- After GREEN, explicitly prompt: "Now refactor the implementation. Extract duplicates, name constants, simplify conditionals. Tests must stay green."
- Check for: magic numbers → named constants, duplicated code → extracted methods, long methods → split
- The refactor phase is where code quality happens — never skip it
