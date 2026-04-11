# Guia de Migração - 0.1.x para 1.0.0

## 1. Resumo

A `v1.0.0` consolida a biblioteca como projeto multi-módulo Maven e endurece os contratos públicos do domínio, do XML e da criptografia. O principal efeito prático da migração é que o consumidor passa a importar apenas os módulos necessários.

## 2. Mudanças principais

### 2.1 Estrutura de artefatos

Antes:

- um único artefato com domínio, XML, JSON e crypto juntos.

Agora:

- `declaracoes-gov-core-domain`
- `declaracoes-gov-core-format`
- `declaracoes-gov-core-xml`
- `declaracoes-gov-core-crypto`
- `declaracoes-gov-core-bom`

### 2.2 Política de validadores

- `CNPJ/CGC` continuam com validação oficial no core.
- `CPF` e `NIS` passam a ter criação padrão estrutural.
- A validação algorítmica de `CPF` e `NIS` continua disponível, mas apenas por opt-in explícito:
  - `Cpf.ofProvisionallyValidated(...)`
  - `Nis.ofProvisionallyValidated(...)`

### 2.3 Assinatura XML

- `XmlDsigSigner` mantém o construtor compatível por provider.
- A `v1.0.0` adiciona configuração explícita do alvo de assinatura:
  - `XmlSignatureOptions.forElement(...)`
  - `XmlSignatureOptions.forIdAttribute(...)`

### 2.4 SSL/TLS

- `SslContextBuilder.build(provider)` continua suportado.
- A `v1.0.0` adiciona `SslContextBuilder.build(provider, trustStore)` para cenários com cadeia confiável explícita.

## 3. Passos recomendados

1. Importar o BOM `1.0.0`.
2. Trocar dependência monolítica pelo módulo mínimo necessário.
3. Revisar qualquer uso de `CPF`/`NIS` que dependia de rejeição por DV no construtor padrão.
4. Migrar assinatura XML implícita para `XmlSignatureOptions` quando o integrador precisar controle explícito.
5. Rodar `mvn clean verify` no projeto consumidor.

## 4. Exemplos

### 4.1 Dependências

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

```xml
<dependencies>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-domain</artifactId>
    </dependency>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-xml</artifactId>
    </dependency>
</dependencies>
```

### 4.2 CPF/NIS

Antes:

```java
Cpf cpf = Cpf.of("123.456.789-09");
```

Agora:

```java
Cpf estrutural = Cpf.of("123.456.789-00");
Cpf provisoriamenteValidado = Cpf.ofProvisionallyValidated("123.456.789-09");
```

## 5. Checklist de adoção

- consumidor importa apenas os módulos necessários;
- uso de `CPF` e `NIS` está alinhado com a política `PROVISIONAL`;
- qualquer assinatura XML com alvo específico usa `XmlSignatureOptions`;
- o build consumidor continua compatível com Java 8.
