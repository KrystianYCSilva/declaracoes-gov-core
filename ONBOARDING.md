# Onboarding do declaracoes-gov-core

## Pré-requisitos

- JDK 8;
- Maven compatível com o workspace;
- checkout completo do repositório `declaracoes`.

## Primeiro caminho feliz

1. Entre na raiz do módulo `declaracoes-gov-core`.
2. Rode o gate atual do reator:

```bash
mvn verify
```

3. Confirme a divisão dos módulos em `pom.xml` e nos POMs filhos:
   - `declaracoes-gov-core-bom/pom.xml`
   - `declaracoes-gov-core-domain/pom.xml`
   - `declaracoes-gov-core-format/pom.xml`
   - `declaracoes-gov-core-crypto/pom.xml`
   - `declaracoes-gov-core-xml/pom.xml`
4. Abra os testes que hoje resumem o contrato público:
   - `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/validator/GovValidationCatalogTest.java`
   - `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/validator/GovValidatorsTest.java`
   - `declaracoes-gov-core-format/src/test/java/br/uem/npd/govcore/util/GovJsonFactoryTest.java`
   - `declaracoes-gov-core-xml/src/test/java/br/uem/npd/govcore/signature/XmlDsigSignerTest.java`
   - `declaracoes-gov-core-crypto/src/test/java/br/uem/npd/govcore/crypto/SslContextBuilderTest.java`

## Pastas e pacotes para conhecer primeiro

| Caminho | Papel |
| --- | --- |
| `pom.xml` | parent do reator e gate JaCoCo padrão (`0.90` linha / `0.90` ramo). |
| `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model` | value objects e tipos reutilizáveis. |
| `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/validator` | validadores, catálogo de confiança e `Modulo11`. |
| `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout` | metadados manuais de leiaute compartilháveis. |
| `declaracoes-gov-core-format/src/main/java/br/uem/npd/govcore` | parsers e utilitários de texto, número, competência e JSON. |
| `declaracoes-gov-core-crypto/src/main/java/br/uem/npd/govcore/crypto` | certificados A1/A3 e `SSLContext`. |
| `declaracoes-gov-core-xml/src/main/java/br/uem/npd/govcore` | utilitários DOM e assinatura XML. |

## Como escolher o módulo certo

- regra de domínio brasileiro, exceção-base ou catálogo de confiança -> `domain`;
- formato, parser manual ou `ObjectMapper` especializado -> `format`;
- certificado, keystore, PKCS#11 ou `SSLContext` -> `crypto`;
- XML seguro, DOM ou assinatura -> `xml`;
- alinhamento de versões para consumidores do próprio core -> `declaracoes-gov-core-bom`.

## Diagnóstico rápido

| Sinal | Onde olhar primeiro |
| --- | --- |
| dúvida sobre nível de confiança de um documento | `docs/05-MATRIZ-VALIDADORES.md` e `GovValidationCatalog` |
| quebra em `Cpf` ou `Nis` | `GovValidators`, `Cpf`, `Nis` e testes de `validator` |
| mudança em `GovJsonFactory` ou formatos | `declaracoes-gov-core-format/src/main/java/br/uem/npd/govcore/util` |
| falha de assinatura XML | `XmlSignatureOptions`, `XmlDsigSigner` e testes de `signature` |
| comportamento dependente de token A3 | `Pkcs11Provider`, `SslContextBuilder` e testes de `crypto` |

## Próximas leituras

- [README.md](README.md)
- [ARCHITECTURE.md](ARCHITECTURE.md)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [docs/02-DESIGN.md](docs/02-DESIGN.md)
- [docs/05-MATRIZ-VALIDADORES.md](docs/05-MATRIZ-VALIDADORES.md)
