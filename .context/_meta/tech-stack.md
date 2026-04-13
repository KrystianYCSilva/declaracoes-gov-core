---
name: tech-stack
description: |
  Technology stack and build facts for declaracoes-gov-core.
  Use when: checking dependencies, build gates, or module responsibilities.
---

# Technology Stack

## Core Platform

- Root packaging: Maven reactor parent (`pom`)
- Java baseline: source and target `1.8`
- Modules:
  - `declaracoes-gov-core-domain`
  - `declaracoes-gov-core-format`
  - `declaracoes-gov-core-xml`
  - `declaracoes-gov-core-crypto`
  - `declaracoes-gov-core-bom`

## Dependencies by Module

| Module | Key Dependencies | Notes |
| --- | --- | --- |
| `domain` | JDK only (plus shared test deps from the parent) | Models, validator policy, tables, exceptions |
| `format` | `declaracoes-gov-core-domain`, optional `jackson-databind`, optional `jackson-datatype-jsr310` | Text, number, date, JSON, and record parsing helpers |
| `xml` | `declaracoes-gov-core-domain`, `declaracoes-gov-core-crypto`, `org.apache.santuario:xmlsec:3.0.3` | XMLDSIG and DOM utilities |
| `crypto` | `declaracoes-gov-core-domain` | PKCS12 / PKCS11 providers, `SSLContext` builder, publishes a test JAR |
| `core-bom` | Packaging `pom` only | Internal version alignment; JaCoCo skipped |

## Shared Test Dependencies

- `junit:junit:4.13.2`
- `org.mockito:mockito-core:4.11.0`
- `org.bouncycastle:bcpkix-jdk15on:1.70`

## Build Plugins

- `maven-compiler-plugin:3.12.1`
- `jacoco-maven-plugin:0.8.11` at the reactor root
- `maven-jar-plugin:3.4.2` in `declaracoes-gov-core-crypto` to publish a `test-jar`

## Coverage and Validation

- Root validation gate: `mvn -q verify`
- Default JaCoCo thresholds from the parent POM:
  - line coverage: `0.90`
  - branch coverage: `0.90`
- `declaracoes-gov-core-crypto` lowers only the line threshold to `0.85`
- `declaracoes-gov-core-bom` sets `jacoco.skip=true` because it is POM-only

## Runtime Constraints

- No framework dependencies are declared in the current POMs.
- No generated source tree is present in this repository today.
- XML support uses secure DOM parsing and XMLDSIG signing APIs.
- JSON support is optional and concentrated in `declaracoes-gov-core-format`.
