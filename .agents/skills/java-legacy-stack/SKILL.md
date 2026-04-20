---
name: java-legacy-stack
description: |
  IBM DB2 + Hibernate + Spring Security LDAP patterns for legacy Java apps.
  Use when: debugging DB2 SQL errors, configuring Hibernate dialects, setting up LDAP auth, or troubleshooting legacy Java stack issues.
activation: Auto
estimated_tokens: 580
---

## How to triage DB2 errors fast

1. Identify SQLCODE and SQLSTATE from the exception
2. Capture the exact SQL and parameters (enable Hibernate SQL logging temporarily)
3. Confirm schema and privileges for the app user
4. Check driver version compatibility

Common errors:
- **SQLSTATE 28000** (auth failure): Verify credentials, check LDAP-backed DB2 accounts, check lock policies
- **SQLCODE -206 / SQLSTATE 42703** (column not found): Mapping vs schema mismatch. Check `@Column(name=)`, join aliases, schema search path. Qualify schema explicitly: `CES.TABLE_NAME`

## How to configure Hibernate for DB2

- Use official DB2 JDBC driver with correct URL format
- Set the right Hibernate dialect for your DB2 version
- Use `ddl-auto=validate` (or `none`) for existing schemas — NEVER `update` or `create` in production
- Eliminate N+1 with targeted fetch joins
- Use projections for list screens
- Size Hikari pool to expected concurrency

### H2 MODE=DB2 for testing
```xml
<property name="javax.persistence.jdbc.url" 
          value="jdbc:h2:mem:test;MODE=DB2;DATABASE_TO_LOWER=TRUE"/>
```

## How to implement LDAP-first auth

- Use bind authentication (user DN + password)
- Map LDAP groups to `ROLE_*` via `DefaultLdapAuthoritiesPopulator`
- Cache group lookups with short TTL to avoid LDAP overload
- Set connect/read timeouts (fail fast on wrong base DN)
- Rate-limit login attempts

```java
// Key configuration points
LdapAuthenticationProvider provider = new LdapAuthenticationProvider(
    new BindAuthenticator(contextSource),
    new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase)
);
```

## How to secure ZK pages with Spring Security

- Enforce authorization on server: URL rules + service-layer `@Secured`
- Optionally hide UI elements by role, but **never rely on hide-only**
- Keep `SessionScope` principal minimal: username, roles, org/unit
- CSRF token handling: ZK CE handles this via desktop ID

Load `references/common-traps.md` for DB2/Hibernate/LDAP-specific pitfalls.
