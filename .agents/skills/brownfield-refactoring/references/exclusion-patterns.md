---
description: |
  JaCoCo exclusion patterns and strategies for legacy Java projects.
  Use when: configuring JaCoCo excludes for brownfield projects with UI frameworks, native DB drivers, or LDAP/SMTP.
---

# JaCoCo Exclusion Patterns

## What to Exclude and Why

| Category | Pattern Example | Reason |
|----------|----------------|--------|
| UI Controllers | `**/controlador/**`, `**/controller/**` | Need ZK/Servlet container to instantiate |
| ZK Utilities | `**/Sessao.class`, `**/ZkUtils.class`, `**/MenuZK*.class` | Need ZK Desktop context |
| DB Connection | `**/ConexaoDB2.class`, `**/Conexao*.class` | Native DB2/Oracle driver required |
| Auth/LDAP | `**/LdapUtil.class`, `**/Login*.class` | Need LDAP server |
| Email | `**/Email.class`, `**/EmailUtil.class` | Need SMTP server |
| File/Attachment | `**/AnexoUtils.class` | File system operations |
| Window/Dialog | `**/Window*.class`, `**/InputMessageBox.class` | ZK window components |
| Legacy DAOs with native SQL | Individual class exclusions | PreparedStatement with DB-specific SQL |

## DTOs with ZK Imports — The Hidden Trap

Some DTOs import ZK UI types as fields:
```java
public class SomeDTO {
    private Textbox txtNome;    // org.zkoss.zul.Textbox
    private Longbox lbxCodigo;  // org.zkoss.zul.Longbox
}
```

These classes EXIST in the source and COMPILE in the WAR, but cause `NoClassDefFoundError` in test context because ZK jars aren't in test classpath. **EXCLUDE them, don't test them.**

How to find them:
```bash
grep -rn "import org.zkoss" src/java --include="*.java" -l
```

## JaCoCo XML Syntax

```xml
<configuration>
    <excludes>
        <!-- Package-level exclusion -->
        <exclude>**/controlador/**</exclude>
        
        <!-- Individual class exclusion -->
        <exclude>**/ConexaoDB2.class</exclude>
        <exclude>**/LdapUtil.class</exclude>
        
        <!-- Wildcard pattern -->
        <exclude>**/controlador*/**</exclude>
    </excludes>
</configuration>
```

Place this inside the `jacoco-maven-plugin` configuration block.

## Strategy: Exclude, Then Test Everything Else

1. First run: add minimal exclusions (controllers, ZK utils, DB connection)
2. Check coverage report
3. If a package shows 0% and all classes import ZK → add to exclusions
4. If a package shows 30% → write more tests for it
5. Repeat until measurable code is ≥70% covered

70% of testable code > 40% of all code.
