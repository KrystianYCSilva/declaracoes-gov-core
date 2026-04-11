# declaracoes-gov-core

Biblioteca Java 8, agnóstica a framework, para primitivas brasileiras reutilizáveis no ecossistema fiscal e contábil: documentos, períodos, normalização, XML e criptografia.

## Módulos

- `declaracoes-gov-core-domain`: documentos, tipos, períodos, vigências, território e política pública de validadores.
- `declaracoes-gov-core-format`: normalização textual, formatos numéricos, competências e `ObjectMapper` governamental.
- `declaracoes-gov-core-xml`: parsing XML seguro, utilitários DOM e assinatura XML configurável.
- `declaracoes-gov-core-crypto`: certificados A1/A3, PKCS11 e `SSLContext`.
- `declaracoes-gov-core-bom`: alinhamento de versões do ecossistema.

## Uso com BOM

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
</dependencies>
```

## Política de validadores

O core publica três níveis de confiança:

- `OFFICIAL`: regra normativa mapeada e apta a sustentar fail-fast.
- `PROVISIONAL`: algoritmo disponível, mas sem fonte primária catalogada o suficiente para ser tratado como oficial.
- `STRUCTURAL`: apenas tamanho, forma e normalização.

Para `CPF` e `NIS`, a criação padrão é estrutural:

```java
Cpf cpf = Cpf.of("111.111.111-11");
Nis nis = Nis.of("111.11111.11-1");
```

Quando o consumidor quiser o algoritmo legado explicitamente provisório:

```java
Cpf cpf = Cpf.ofProvisionallyValidated("123.456.789-09");
Nis nis = Nis.ofProvisionallyValidated("170.33259.50-4");
```

Veja [05-MATRIZ-VALIDADORES.md](./docs/05-MATRIZ-VALIDADORES.md).

## Exemplos

### Domínio

```java
Cnpj cnpj = Cnpj.of("12.345.678/0001-90");
PeriodoApuracao periodo = PeriodoApuracao.parse("2026-04");
```

### Formatação

```java
String texto = GovTextNormalizer.toGovUpper(" João d'Ávila Ltda ");
String valor = GovNumberFormats.toPlainString(new BigDecimal("1000.00"));
String competencia = GovCompetenceFormats.toCompactFormat(YearMonth.of(2026, 4));
```

### XML

```java
XmlSigner signer = new XmlDsigSigner(
    certificateProvider,
    XmlSignatureOptions.forElement("info", "IdEvento")
);
String signedXml = signer.sign(xml);
```

### Crypto

```java
SSLContext sslContext = SslContextBuilder.build(certificateProvider);
```

## Build

```bash
mvn clean verify
```

## Documentação

- [01-REQUISITOS.md](./docs/01-REQUISITOS.md)
- [02-DESIGN.md](./docs/02-DESIGN.md)
- [03-PLANO-TESTES.md](./docs/03-PLANO-TESTES.md)
- [04-IMPLANTACAO.md](./docs/04-IMPLANTACAO.md)
- [06-GUIA-MIGRACAO-0.1.x-1.0.0.md](./docs/06-GUIA-MIGRACAO-0.1.x-1.0.0.md)
