---
work_package_id: WP01
title: Setup do módulo Maven (pom.xml, build config, BOM)
lane: "planned"
dependencies: []
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T001
- T002
---

# WP01 — Setup do Módulo Maven: declaracoes-gov-core-kotlin-guava

## Objetivo

Criar o esqueleto Maven do novo módulo opcional `declaracoes-gov-core-kotlin-guava` e registrá-lo no reator raiz e no BOM. Nenhum código de produção ou teste é criado aqui — apenas estrutura Maven.

---

## T001 — Criar `declaracoes-gov-core-kotlin-guava/pom.xml`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/pom.xml`

### Estrutura de diretórios a criar

```
declaracoes-gov-core-kotlin-guava/
├── pom.xml
└── src/
    ├── main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/
    │   ├── collections/
    │   ├── ranges/
    │   ├── table/
    │   ├── cache/
    │   └── hash/
    └── test/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/
        ├── collections/
        ├── ranges/
        ├── table/
        ├── cache/
        └── hash/
```

### Conteúdo do pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>br.com.contabilizei.obrigacoes</groupId>
        <artifactId>declaracoes-gov-core</artifactId>
        <version>${revision}</version>
    </parent>

    <artifactId>declaracoes-gov-core-kotlin-guava</artifactId>
    <name>declaracoes-gov-core-kotlin-guava</name>
    <description>Wrappers idiomáticos Kotlin sobre Google Guava para o domínio contábil-fiscal</description>

    <dependencies>
        <!-- Core kotlin module -->
        <dependency>
            <groupId>br.com.contabilizei.obrigacoes</groupId>
            <artifactId>declaracoes-gov-core-kotlin</artifactId>
            <version>${revision}</version>
        </dependency>

        <!-- Google Guava -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-test-junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Instruções

1. Criar diretórios `src/main/kotlin/.../guava/{collections,ranges,table,cache,hash}/` e `src/test/kotlin/.../guava/{collections,ranges,table,cache,hash}/`.
2. Criar `.gitkeep` em cada diretório vazio para o commit inicial.
3. O módulo usa `${revision}` para versão — herdado do pai.

---

## T002 — Registrar módulo no POM raiz e BOM

### 2a — Adicionar ao POM raiz (`pom.xml`)

Localizar a seção `<modules>` e adicionar:

```xml
<module>declaracoes-gov-core-kotlin-guava</module>
```

Adicionar propriedades de versão na seção `<properties>`:

```xml
<guava.version>33.0.0-jre</guava.version>
```

Adicionar na seção `<dependencyManagement>` do POM raiz:

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>${guava.version}</version>
</dependency>
<dependency>
    <groupId>br.com.contabilizei.obrigacoes</groupId>
    <artifactId>declaracoes-gov-core-kotlin-guava</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2b — Adicionar ao BOM (`declaracoes-gov-core-bom/pom.xml`)

```xml
<dependency>
    <groupId>br.com.contabilizei.obrigacoes</groupId>
    <artifactId>declaracoes-gov-core-kotlin-guava</artifactId>
    <version>${revision}</version>
</dependency>
```

---

## Checklist de Validação

- [ ] `declaracoes-gov-core-kotlin-guava/pom.xml` existe e tem parent correto
- [ ] `<module>declaracoes-gov-core-kotlin-guava</module>` está no POM raiz
- [ ] `guava.version=33.0.0-jre` está nas properties do POM raiz
- [ ] `<dependencyManagement>` do POM raiz inclui `guava` e `kotlin-guava`
- [ ] BOM inclui o novo artefato
- [ ] `mvn -q verify` no reator raiz (sem código de produção ainda) passa — módulo reconhecido
- [ ] Diretórios `src/main/kotlin` e `src/test/kotlin` existem com estrutura de pacotes
