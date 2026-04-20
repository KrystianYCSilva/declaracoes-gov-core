# Technology Stack

## Core Platform

- Java 8 (source and target `1.8`)
- Maven multi-module reactor parent (`pom`)
- Modules: domain, format, xml, crypto, core-bom

## Dependencies by Module

| Module | Key Dependencies |
|--------|-----------------|
| `domain` | JDK only |
| `format` | `core-domain`, optional `jackson-databind` / `jackson-datatype-jsr310` |
| `xml` | `core-domain`, `core-crypto`, `xmlsec:3.0.3` |
| `crypto` | `core-domain` (publishes test-jar) |
| `core-bom` | POM-only |

## Shared Test Dependencies

- JUnit `4.13.2`
- Mockito `4.11.0`
- BouncyCastle `bcpkix-jdk15on:1.70`

## Build Plugins

- `maven-compiler-plugin:3.12.1`
- `jacoco-maven-plugin:0.8.11`
- `maven-jar-plugin:3.4.2` (crypto test-jar)

## Coverage and Validation

- Gate: `mvn -q verify`
- JaCoCo: 90% line / 90% branch (crypto: 85% line exception)
- `core-bom` sets `jacoco.skip=true`
