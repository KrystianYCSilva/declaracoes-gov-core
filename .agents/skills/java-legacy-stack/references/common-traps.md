# Java Legacy Stack Common Traps

## Trap 1: N+1 Queries with Hibernate + DB2
- **What happens**: List screen fires 1 query for the list + N queries for each related entity
- **Fix**: Use `JOIN FETCH` in JPQL or `@EntityGraph`. For list screens, use projections (DTOs).
- **Detection**: Enable `hibernate.show_sql=true` temporarily, count queries per page load

## Trap 2: DB2 Schema Qualification
- **What happens**: `SQLCODE -204` (object not found) because default schema ≠ app schema
- **Fix**: Always qualify: `@Table(name = "TABLE_NAME", schema = "CES")`
- **In H2 tests**: Use `SET SCHEMA CES` in init script or `DATABASE_TO_LOWER=TRUE`

## Trap 3: LDAP Timeout Not Set
- **What happens**: Application hangs when LDAP server is unreachable
- **Fix**: Set `com.sun.jndi.ldap.connect.timeout=5000` and `com.sun.jndi.ldap.read.timeout=10000`
- **Rule**: Fail fast. A hung login page is worse than a failed login.

## Trap 4: ddl-auto=update on Production DB2
- **What happens**: Hibernate tries to ALTER TABLE on DB2 → permission denied or schema corruption
- **Fix**: ALWAYS use `validate` or `none` in production. Schema changes via DBA-approved DDL scripts.

## Trap 5: H2 MODE=DB2 Behavior Gaps
- **What happens**: Tests pass on H2 but fail on real DB2 due to syntax differences
- **Known gaps**: `WITH UR` (uncomitted read), `FETCH FIRST N ROWS ONLY` syntax, DB2-specific functions
- **Mitigation**: Use `MODE=DB2` + acceptance tests on real DB2 for critical paths
- **Rule**: H2 tests validate logic. DB2 integration tests validate SQL compatibility.
