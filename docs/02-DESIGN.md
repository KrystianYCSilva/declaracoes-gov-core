# Design do declaracoes-gov-core

## 1. Estrutura do reator

`declaracoes-gov-core` é um parent `pom` com quatro módulos de runtime e um BOM interno.

| Módulo | O que entrega | Observações |
| --- | --- | --- |
| `declaracoes-gov-core-domain` | domínio manual, exceções, enums, catálogo de validação e metadados de leiaute | base do restante do core |
| `declaracoes-gov-core-format` | `GovTextNormalizer`, `GovNumberFormats`, `GovCompetenceFormats`, `XmlDates`, `GovJsonFactory`, parsers e serializers manuais | depende de `domain` e usa Jackson como dependência opcional |
| `declaracoes-gov-core-crypto` | `CertificateProvider`, `AbstractKeyStoreProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder` | depende de `domain` |
| `declaracoes-gov-core-xml` | `XmlDocuments`, `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions` | depende de `domain` e `crypto` |
| `declaracoes-gov-core-bom` | versão alinhada de `domain`, `format`, `crypto` e `xml` | sem `src` e sem API de runtime |

## 2. Mapa de pacotes manuais

| Pacote | Conteúdo atual | Papel |
| --- | --- | --- |
| `br.com.contabilizei.obrigacoes.govcore.model` | `Cnpj`, `Cpf`, `Nis`, `Caepf`, `Cno`, `Cei`, `CodigoMunicipio`, `PeriodoApuracao`, `Recibo`, `Vigencia`, interfaces de inscrição | domínio brasileiro reutilizável |
| `br.com.contabilizei.obrigacoes.govcore.model.layout` | `LayoutVersion`, `NormativeSource`, `RecordDefinition`, `FieldDefinition`, `Constraint`, `ValidityWindow` | metadados manuais para catálogos de leiaute |
| `br.com.contabilizei.obrigacoes.govcore.table` | `TipoInscricao`, `TipoAmbiente`, `Uf` | tabelas manuais estáveis |
| `br.com.contabilizei.obrigacoes.govcore.validator` | `GovValidators`, `GovValidationCatalog`, `ValidationMetadata`, `ValidationLevel`, `Modulo11` e validadores concretos | contrato público de validação |
| `br.com.contabilizei.obrigacoes.govcore.exception` | `GovCoreException` e derivadas | hierarquia coesa de erro |
| `br.com.contabilizei.obrigacoes.govcore.format.parser` | `DelimitedParser`, `DelimitedSerializer`, `FixedLengthParser`, `FixedLengthSerializer` | parsers manuais de arquivo |
| `br.com.contabilizei.obrigacoes.govcore.util` | normalizadores, formatos, JSON e datas XML | utilitários leves de integração |
| `br.com.contabilizei.obrigacoes.govcore.crypto` | providers e `SSLContext` | segurança e certificado |
| `br.com.contabilizei.obrigacoes.govcore.signature` | `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions` | assinatura XML configurável |

## 3. Dependências internas

- `format` depende de `domain` porque formata tipos e contratos do core.
- `crypto` depende de `domain` para reutilizar exceções e convenções transversais.
- `xml` depende de `domain` e `crypto` porque a assinatura XML reutiliza a infraestrutura de certificado.
- `declaracoes-gov-core-bom` não entra em runtime; apenas exporta coordenadas.

## 4. Decisões duráveis

### DD-01 — Value objects permanecem no `domain`
Os tipos brasileiros manualmente mantidos vivem em `domain` porque precisam ser reutilizados por leiautes, transmissores e clientes externos sem trazer XML, criptografia ou Jackson junto.

### DD-02 — Confiança normativa é parte da API
`GovValidationCatalog` e `GovValidators` tornam público o nível de confiança de cada documento. Isso evita que heurísticas do mercado sejam vendidas como validação oficial.

### DD-03 — Dependência pesada fica isolada
Jackson fica restrito a `format`; XML Security fica restrito a `xml`; integração com keystore e PKCS#11 fica restrita a `crypto`.

### DD-04 — Assinatura XML usa opções explícitas
`XmlSignatureOptions` existe para o caller informar alvo e atributo de ID sem heurística acoplada a uma declaração específica.

### DD-05 — O core continua independente do protocolo
Nenhum módulo do reator conhece endpoint, token, fila ou fluxo de entrega. Essa separação é intencional e não deve ser relaxada.

## 5. Modelo de thread-safety

| Área | Estratégia atual |
| --- | --- |
| value objects e enums | imutáveis |
| validadores | stateless |
| utilitários de formato e XML | uso estático ou configuração explícita |
| providers PKCS#11/A3 | dependem do ambiente do consumidor e exigem cuidado operacional |
| BOM interno | sem estado e sem runtime |

## 6. Pontos de extensão permitidos

- novo documento, enum ou metadado manual -> `domain`;
- novo parser/serializer ou normalizador -> `format`;
- novo provider ou helper de trust material -> `crypto`;
- novo helper DOM ou política de assinatura -> `xml`.

## 7. Não objetivos

- clientes HTTP/SOAP/REST;
- regra negocial de obrigação acessória;
- geração de código a partir de artefatos oficiais;
- catálogo de endpoints, tokens ou retries.
