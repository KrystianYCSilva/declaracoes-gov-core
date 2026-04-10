# Adaptation Guide: Next Projects

## Overview

After successfully setting up `declaracoes-gov-core` as the pilot, we now adapt the same structure to the remaining 13 projects.

## Project Categories

### Category 1: Leiautes (9 projects)
Layout libraries with JAXB models for tax declarations.

- declaracoes-esocial-leiautes
- declaracoes-efd-reinf-leiautes
- declaracoes-mit-leiautes
- declaracoes-dctfweb-leiautes
- declaracoes-defis-leiautes
- declaracoes-pgdas-leiautes
- declaracoes-pgmei-leiautes
- declaracoes-perdcomp-leiautes
- declaracoes-parcelamento-leiautes

**Key Adaptations**:
- Focus: XSD-based JAXB models, versioning, event catalog
- Dependencies: declaracoes-gov-core (for validation)
- Patterns: Version enum, Event catalog, JAXB factories

### Category 2: Transmissores (3 projects)
HTTP clients for transmitting declarations.

- declaracoes-esocial-transmissor
- declaracoes-efd-reinf-transmissor
- declaracoes-serpro-transmissor

**Key Adaptations**:
- Focus: HTTP communication, SOAP/REST, mTLS, async processing
- Dependencies: declaracoes-gov-core, declaracoes-*-leiautes
- Patterns: Client interfaces, Retry strategies, Circuit breaker

### Category 3: Core/BOM (1 project)
Bill of Materials for version management.

- declaracoes-gov-bom

**Key Adaptations**:
- Focus: Maven BOM, dependency management
- No code, only POM configuration
- Simpler setup (no patterns/workflows needed)

## Adaptation Matrix

### AGENTS.md Adaptations

| Section | Core | Leiautes | Transmissores | BOM |
|---------|------|----------|---------------|-----|
| Project Type | Java Library | Java Library | Java Library | Maven BOM |
| Domain | Certificates, Signatures | Tax Declaration Models | HTTP Transmission | Dependency Mgmt |
| Key Components | certificado, assinatura, documento, json | model, versioning, validation | client, connection, retry | pom.xml only |
| Stack | JAXB, XMLSec, Jackson | JAXB, gov-core | HttpClient5, gov-core | Maven |
| Special Focus | Thread-safety, Java 8 | XSD compliance, versioning | Async, resilience | Version alignment |

### .context/ Standards

All projects share:
- `standards/architectural-rules.md` (T0 - adapt AR-00X rules)
- `standards/code-quality.md` (T1 - mostly same)
- `standards/testing-strategy.md` (T1 - adapt test types)

### Agent-Specific Files

Template replacements:
- `{PROJECT_NAME}` → actual project name
- `{DOMAIN}` → specific domain description
- `{DEPENDENCIES}` → relevant dependencies

## Step-by-Step Adaptation Process

### Step 1: Copy Structure

```powershell
# Copy from gov-core template
Copy-Item -Recurse declaracoes-gov-core/.context declaracoes-XXX/
Copy-Item -Recurse declaracoes-gov-core/.agents declaracoes-XXX/
Copy-Item -Recurse declaracoes-gov-core/.claude declaracoes-XXX/
# ... etc for all agent directories
Copy-Item declaracoes-gov-core/AGENTS.md declaracoes-XXX/
Copy-Item declaracoes-gov-core/.gitignore declaracoes-XXX/
```

### Step 2: Update Project Identity

In all files, replace:
- `declaracoes-gov-core` → `declaracoes-XXX`
- Domain description
- Key components list
- Technology stack (if different)

### Step 3: Adapt Content

#### For Leiautes Projects

**AGENTS.md changes**:
```markdown
## Project
- Name: `declaracoes-esocial-leiautes`
- Type: `Java Library (JAR)`
- Domain: eSocial event models (S-1000 to S-5000), XSD-based JAXB

## Stack
- Java 8+
- Maven
- JAXB (XML binding)
- declaracoes-gov-core (validation)

## Architecture Rules
- Keep models aligned with official XSD schemas
- Version enum for layout versions (S_1_0, S_1_1, etc.)
- Event catalog for type-safe event handling
- Factory methods for creating events
```

**_meta/project-overview.md changes**:
```markdown
## Purpose
JAXB-based layout library for eSocial (Electronic Social Declaration).
Provides type-safe Java models for all eSocial events (S-1000 to S-5000).

## Key Capabilities
1. Event Models (S-1000 to S-5000)
2. Version Management (S_1_0 through S_1_3)
3. JAXB Serialization/Deserialization
4. Validation integration with gov-core
```

**_meta/codebase-map.md changes**:
```markdown
## Package Structure

br.gov.receita.declaracoes.esocial/
├── model/              # JAXB-generated and custom models
│   ├── v1_0/          # Version 1.0 events
│   ├── v1_1/          # Version 1.1 events
│   └── ...
├── versioning/         # Version management
│   ├── EsocialVersion
│   └── VersionAdapter
├── catalog/            # Event catalog
│   ├── EsocialEvent
│   └── EventGroup
└── validation/         # Event validation
```

#### For Transmissores Projects

**AGENTS.md changes**:
```markdown
## Project
- Name: `declaracoes-esocial-transmissor`
- Type: `Java Library (JAR)`
- Domain: SOAP-based transmission to eSocial government endpoint

## Stack
- Java 8+
- Maven
- Apache HttpClient 5 (SOAP/HTTP)
- declaracoes-gov-core (certificates, signature)
- declaracoes-esocial-leiautes (models)

## Architecture Rules
- Client interfaces for testability
- Retry with exponential backoff
- Circuit breaker for resilience
- Async support for batch processing
```

**_meta/project-overview.md changes**:
```markdown
## Purpose
SOAP-based transmitter for eSocial declarations.
Handles secure communication with government endpoint using mTLS and WS-Security.

## Key Capabilities
1. SOAP Client for eSocial webservices
2. mTLS Connection Management
3. WS-Security Header Generation
4. Async Batch Transmission
5. Response Processing
```

**_meta/codebase-map.md changes**:
```markdown
## Package Structure

br.gov.receita.declaracoes.esocial.transmissor/
├── client/             # SOAP client interfaces
│   ├── EsocialClient
│   └── EsocialClientBuilder
├── connection/         # Connection management
│   ├── SoapConnection
│   └── WsSecurityHeader
├── retry/              # Retry strategies
│   ├── RetryPolicy
│   └── ExponentialBackoff
├── circuitbreaker/     # Resilience
│   └── CircuitBreaker
└── response/           # Response handling
```

#### For BOM Project

**AGENTS.md changes**:
```markdown
## Project
- Name: `declaracoes-gov-bom`
- Type: `Maven BOM (Bill of Materials)`
- Domain: Centralized dependency version management

## Purpose
Maven BOM that centralizes version management for all declaracoes-* libraries.
Eliminates classpath conflicts by providing compatible version sets.

## Stack
- Maven 3.9+
- No Java code
- Only POM configuration

## Architecture Rules
- Keep versions synchronized across ecosystem
- Document version compatibility matrix
- Update with each release cycle
```

**Remove unnecessary directories**:
- No `.context/patterns/` (no code)
- No `.context/workflows/` (simpler)
- Keep only: `_meta/`, basic `standards/`

### Step 4: Validate

Checklist:
- [ ] All files updated with correct project name
- [ ] Domain description accurate
- [ ] Technology stack correct
- [ ] Dependencies listed correctly
- [ ] No references to gov-core-specific concepts
- [ ] Build commands appropriate

### Step 5: Test

```bash
cd declaracoes-XXX

# Verify structure
ls -la .context/
ls -la .kimi/

# If has code:
mvn -q verify
```

## Recommended Order

1. **declaracoes-gov-core** ✅ DONE (pilot)
2. **declaracoes-gov-bom** (simplest - no code)
3. **declaracoes-esocial-leiautes** (reference leiaute)
4. **declaracoes-esocial-transmissor** (reference transmissor)
5. **declaracoes-efd-reinf-leiautes** (similar to esocial)
6. **declaracoes-efd-reinf-transmissor** (similar to esocial)
7. **declaracoes-serpro-transmissor** (REST vs SOAP - different)
8. Remaining leiautes (mit, dctfweb, defis, pgdas, pgmei, perdcomp, parcelamento)

## Common Pitfalls

1. **Copying without adapting**: Always update project-specific content
2. **Forgetting dependencies**: List actual dependencies in tech-stack.md
3. **Wrong architecture rules**: Adapt T0 rules to project type
4. **Inconsistent naming**: Use project name consistently
5. **Missing agent files**: Ensure all 11 agent configs created

## Quick Validation Script

```powershell
function Validate-Project {
    param($ProjectPath)
    
    $required = @(
        "AGENTS.md",
        ".gitignore",
        ".context/README.md",
        ".context/ai-assistant-guide.md",
        ".context/standards/architectural-rules.md",
        ".kimi/AGENTS.md"
    )
    
    foreach ($file in $required) {
        $path = Join-Path $ProjectPath $file
        if (Test-Path $path) {
            Write-Host "✓ $file" -ForegroundColor Green
        } else {
            Write-Host "✗ $file MISSING" -ForegroundColor Red
        }
    }
}

# Usage
Validate-Project "declaracoes-esocial-leiautes"
```

## Success Criteria

For each project:
- [ ] AGENTS.md reflects actual project purpose
- [ ] .context/ files adapted to project domain
- [ ] All 11 agent configurations present
- [ ] No gov-core-specific references (unless shared)
- [ ] Technology stack accurate
- [ ] Build commands appropriate

## Timeline Estimate

- Simple project (BOM): 30 minutes
- Standard project (leiaute/transmissor): 1 hour
- Complex project (serpro with REST): 1.5 hours

Total: ~15 hours for all 14 projects
