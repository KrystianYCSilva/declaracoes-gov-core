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

# WP01 — Setup do Módulo Maven: declaracoes-gov-core-kotlin-apache

## Objetivo

Criar o esqueleto Maven do novo módulo opcional `declaracoes-gov-core-kotlin-apache` e registrá-lo no reator raiz e no BOM. Nenhum código de produção ou teste é criado aqui.

---

## T001 — Criar `declaracoes-gov-core-kotlin-apache/pom.xml`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/pom.xml`

### Estrutura de diretórios a criar

```
declaracoes-gov-core-kotlin-apache/
├── pom.xml
└── src/
    ├── main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/
    │   ├── text/
    │   ├── number/
    │   ├── math/
    │   └── codec/
    └── test/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/
        ├── text/
        ├── number/
        ├── math/
        └── codec/
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

    <artifactId>declaracoes-gov-core-kotlin-apache</artifactId>
    <name>declaracoes-gov-core-kotlin-apache</name>
    <description>Wrappers idiomáticos Kotlin sobre Apache Commons para o domínio contábil-fiscal</description>

    <dependencies>
        <!-- Core kotlin module -->
        <dependency>
            <groupId>br.com.contabilizei.obrigacoes</groupId>
            <artifactId>declaracoes-gov-core-kotlin</artifactId>
            <version>${revision}</version>
        </dependency>

        <!-- Apache Commons -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-text</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-math3</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
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

---

## T002 — Registrar módulo no POM raiz e BOM

### 2a — POM raiz (`pom.xml`)

Adicionar na seção `<modules>`:

```xml
<module>declaracoes-gov-core-kotlin-apache</module>
```

Adicionar nas `<properties>`:

```xml
<commons-lang3.version>3.14.0</commons-lang3.version>
<commons-text.version>1.12.0</commons-text.version>
<commons-math3.version>3.6.1</commons-math3.version>
<commons-codec.version>1.17.0</commons-codec.version>
```

Adicionar no `<dependencyManagement>`:

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>${commons-lang3.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-text</artifactId>
    <version>${commons-text.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-math3</artifactId>
    <version>${commons-math3.version}</version>
</dependency>
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>${commons-codec.version}</version>
</dependency>
<dependency>
    <groupId>br.com.contabilizei.obrigacoes</groupId>
    <artifactId>declaracoes-gov-core-kotlin-apache</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2b — BOM (`declaracoes-gov-core-bom/pom.xml`)

```xml
<dependency>
    <groupId>br.com.contabilizei.obrigacoes</groupId>
    <artifactId>declaracoes-gov-core-kotlin-apache</artifactId>
    <version>${revision}</version>
</dependency>
```

---

## Checklist de Validação

- [ ] `declaracoes-gov-core-kotlin-apache/pom.xml` existe com as 4 dependências Apache
- [ ] `<module>declaracoes-gov-core-kotlin-apache</module>` no POM raiz
- [ ] 4 properties de versão Apache Commons nas properties do POM raiz
- [ ] `<dependencyManagement>` inclui todas as 4 libs Apache + artefato kotlin-apache
- [ ] BOM inclui o novo artefato
- [ ] `mvn -q verify` no reator raiz reconhece o módulo sem erros de compilação
