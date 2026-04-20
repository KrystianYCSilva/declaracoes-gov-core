---
description: |
  Structured template for writing review comments.
  Use when: writing PR review comments that need to be clear and actionable.
---

# Review Comment Template

## Format

```markdown
**[SEVERITY]** `file:line` — Short description

**What:** Describe the specific issue in the code.
**Why:** Explain the risk or impact if not addressed.
**Suggest:** Provide a concrete alternative or fix.
```

## Examples

### 🔴 Critical
```markdown
**[MUST FIX]** `src/UserDAO.java:34` — SQL injection vulnerability

**What:** User input `username` is concatenated directly into SQL query.
**Why:** Allows attackers to execute arbitrary SQL, potentially dumping or deleting the database.
**Suggest:** Use PreparedStatement with parameterized query:
​```java
String sql = "SELECT * FROM users WHERE username = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, username);
​```
```

### 🟡 Important
```markdown
**[SHOULD FIX]** `src/OrderService.java:78` — Missing null check on `customer.getAddress()`

**What:** `customer.getAddress()` can return null for customers without registered address.
**Why:** NullPointerException in production when processing orders for addressless customers.
**Suggest:** Add null check or use Optional:
​```java
String city = Optional.ofNullable(customer.getAddress())
    .map(Address::getCity)
    .orElse("Unknown");
​```
```

### 🔵 Suggestion
```markdown
**[CONSIDER]** `src/ReportGenerator.java:120` — Complex conditional could be a named method

**What:** 4-line boolean expression with 5 conditions is hard to read.
**Why:** Next developer will need to re-derive the business rule to understand this.
**Suggest:** Extract to `isEligibleForDiscount(student)` with a descriptive name.
```

### ⚪ Nitpick
```markdown
**[NIT]** `src/Utils.java:15` — Variable `d` could be more descriptive

**What:** `double d = calculateDistance(a, b)` — `d` doesn't convey meaning.
**Suggest:** `double distanceKm = calculateDistance(origin, destination)`
```

## Review Summary Template

At the end of a full review, provide a summary:

```markdown
## Review Summary

**Files reviewed:** 12
**Findings:** 2 🔴 Critical, 3 🟡 Important, 5 🔵 Suggestion, 2 ⚪ Nitpick

### Blockers (must fix before merge)
1. SQL injection in UserDAO.java:34
2. Unhandled NPE in OrderService.java:78

### Key Observations
- Test coverage for new code: 85% ✅
- Error handling pattern: inconsistent — some methods throw, others return null
- Recommendation: standardize on Optional returns for query methods

### Verdict: ❌ Changes Requested
Fix the 2 critical issues. The important issues should be addressed but won't block merge.
```
