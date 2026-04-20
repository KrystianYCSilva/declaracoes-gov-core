---
description: |
  Exact dependency versions, plugin versions, and build configuration for declaracoes-gov-core.
  Use when: adding, removing, or upgrading dependencies; debugging build issues.
---

# Technology Stack — declaracoes-gov-core

## Core Platform

| Component | Version | Notes |
|-----------|---------|-------|
| Java | `1.8` (source/target) | Hard baseline; no Java 9+ APIs |
| Maven | `3.x` | Multi-module reactor parent |

## Module Dependencies

| Module | Production Dependencies | Test Dependencies |
|--------|------------------------|-------------------|
| `domain` | JDK only | JUnit 4.13.2, Mockito 4.11.0 |
| `format` | `core-domain`, optional `jackson-databind:2.16.1`, `jackson-datatype-jsr310:2.16.1` | JUnit, Mockito |
| `xml` | `core-domain`, `core-crypto`, `xmlsec:3.0.3` (Apache Santuario) | JUnit, Mockito, `core-crypto` test-jar |
| `crypto` | `core-domain`, `bcpkix-jdk18on:1.78.1` (test scope) | JUnit, Mockito |
| `bom` | none (POM-only) | none (`jacoco.skip=true`) |

## Build Plugins

| Plugin | Version | Purpose |
|--------|---------|---------|
| `maven-compiler-plugin` | `3.12.1` | Java 8 compilation |
| `jacoco-maven-plugin` | `0.8.11` | Coverage reporting and gating |
| `maven-jar-plugin` | `3.4.2` | Publishes `test-jar` from `crypto` module |

## Coverage Configuration

| Module | Line Minimum | Branch Minimum |
|--------|-------------|----------------|
| `domain`, `format`, `xml` | `90%` | `90%` |
| `crypto` | `85%` | `90%` |
| `bom` | skipped | skipped |

### JaCoCo Properties (Parent POM)
- `jacoco.line.minimum` — controls line gate.
- `jacoco.branch.minimum` — controls branch gate.
- `jacoco.skip` — `true` in `core-bom` module only.

## Validation Gate

```bash
mvn -q verify
```
Runs: compilation → tests → JaCoCo report → JaCoCo check.

## Version Alignment

Consumers must import the BOM before selecting modules:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>br.uem.npd</groupId>
      <artifactId>declaracoes-gov-core-bom</artifactId>
      <version>1.1.0-SNAPSHOT</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Constraints

- Do not upgrade Jackson or xmlsec without checking module boundary rules (AR-003).
- Do not add Spring, Jakarta EE, Lombok, or Bean Validation to any module (AR-002).
- BouncyCastle is test-scope only in `crypto`; do not promote to production scope.
