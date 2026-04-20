---
description: |
  Tech stack recommendations optimized for LLM accuracy and project longevity.
  Use when: choosing frameworks, libraries, and tools for a new project.
---

# Tech Stack Decision Guide

## Principle: Boring Technology Wins

LLMs perform best with technologies that have the most training data. Novel or niche tools cause hallucination.

## Java Stack (Recommended)

| Component | Choice | Version | Why |
|-----------|--------|---------|-----|
| Build | Maven | 3.9.x | Universal LLM knowledge, XML is unambiguous |
| Framework | Spring Boot | 3.2.x | Most examples in training data |
| ORM | Spring Data JPA | (matches Boot) | Convention-over-config reduces hallucination |
| Test | JUnit 5 | 5.12.x | Modern, well-documented |
| Mock | Mockito | 5.x | De facto standard |
| Coverage | JaCoCo | 0.8.12 | Maven plugin, CI-friendly |
| Test DB | H2 | 2.2.224 | In-memory, MODE=compat for any target DB |
| Test runner | Surefire | 3.2.5 | Works with JUnit 5 out of the box |

### Legacy Java (Java 8)
If stuck on Java 8, use these instead:
- JUnit 5.12.x (still works on Java 8)
- Mockito **4.11.0** (last version supporting Java 8)
- H2 2.2.224 with `MODE=DB2` or `MODE=Oracle`
- Add JAXB if running on Java 11+ runtime: `jakarta.xml.bind-api:4.0.2` + `jaxb-runtime:4.0.5`

## Node.js Stack (Recommended)

| Component | Choice | Version | Why |
|-----------|--------|---------|-----|
| Runtime | Node.js | 20 LTS | Stable, widespread |
| Package | npm | (bundled) | yarn/pnpm cause LLM confusion |
| Framework | Express or Fastify | Latest | Express: more training data. Fastify: better DX |
| Test | Jest | 29.x | Zero-config, snapshot testing |
| Lint | ESLint + Prettier | Latest | Standard combo |
| Coverage | Jest --coverage | Built-in | Istanbul under the hood |
| ORM | Prisma or TypeORM | Latest | Prisma: schema-first. TypeORM: decorator-based |

## Python Stack (Recommended)

| Component | Choice | Version | Why |
|-----------|--------|---------|-----|
| Version | Python | 3.11+ | Type hints, performance |
| Package | pip + venv | Built-in | Poetry causes LLM confusion with pyproject.toml |
| Framework | FastAPI or Flask | Latest | FastAPI: modern. Flask: simpler |
| Test | pytest | Latest | De facto standard |
| Coverage | pytest-cov | Latest | --cov flag |
| Lint | ruff | Latest | Fast, replaces flake8+isort+black |
| ORM | SQLAlchemy 2.0 | Latest | Most training data |

## Coverage Targets

| Project Type | Line Coverage | Branch Coverage |
|--------------|---------------|-----------------|
| Greenfield | **80%** | **70%** |
| Brownfield (existing) | **70%** | **60%** |
| Library/SDK | **90%** | **80%** |
| UI-only | **60%** | **50%** |

## What NOT to Choose

| Avoid | Why |
|-------|-----|
| Gradle (Kotlin DSL) | LLMs confuse Groovy DSL and Kotlin DSL, hallucinate task names |
| Micronaut/Quarkus | Less training data → more hallucination in DI/config |
| TestNG | JUnit 5 has more examples in training data |
| yarn/pnpm | npm is the baseline, others cause lockfile confusion |
| Poetry | pyproject.toml variants confuse LLMs |
