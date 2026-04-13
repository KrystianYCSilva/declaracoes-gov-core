# Implantação e consumo do declaracoes-gov-core

## 1. Pré-requisitos

- JDK 8;
- Maven compatível com o workspace;
- acesso aos artefatos publicados ou ao checkout do reator.

## 2. Validação local

Da raiz do módulo:

```bash
mvn verify
```

## 3. Forma recomendada de consumo

Para consumidores externos, o ponto de entrada normal é o BOM interno do core:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>br.uem.npd</groupId>
            <artifactId>declaracoes-gov-core-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Depois disso, cada aplicação escolhe apenas o que precisa:

```xml
<dependencies>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-domain</artifactId>
    </dependency>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-crypto</artifactId>
    </dependency>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-xml</artifactId>
    </dependency>
</dependencies>
```

## 4. Ordem de publicação

1. manter a mesma versão no parent e nos módulos filhos;
2. validar o reator com `mvn verify`;
3. publicar os artefatos concretos (`domain`, `format`, `crypto`, `xml`);
4. publicar `declaracoes-gov-core-bom` como ponto de importação do conjunto.

## 5. Cuidados de adoção

- `Cpf.of(...)` e `Nis.of(...)` fazem apenas checagem estrutural; a validação algorítmica continua explícita em `ofProvisionallyValidated(...)`;
- `GovJsonFactory` representa um mapper governamental do projeto, não um `ObjectMapper` genérico para qualquer domínio;
- `Pkcs11Provider` depende do provider e do driver nativo do ambiente do consumidor;
- assinatura XML deve preferir `XmlSignatureOptions` explícitas em vez de heurística.

## 6. Documentos relacionados

- `README.md`
- `ONBOARDING.md`
- `ARCHITECTURE.md`
- `docs/03-PLANO-TESTES.md`
- `docs/05-MATRIZ-VALIDADORES.md`
- `docs/06-GUIA-MIGRACAO-0.1.x-1.0.0.md`
