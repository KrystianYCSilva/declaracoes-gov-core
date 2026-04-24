# Design do declaracoes-gov-core

## 1. Estrutura do reator

`declaracoes-gov-core` é um parent `pom` com cinco módulos de runtime e um BOM interno.

| Módulo | O que entrega | Observações |
| --- | --- | --- |
| `declaracoes-gov-core-domain` | domínio manual, exceções, enums, catálogo de validação e metadados de leiaute | base do restante do core |
| `declaracoes-gov-core-format` | `GovTextNormalizer`, `GovNumberFormats`, `GovCompetenceFormats`, `XmlDates`, `GovJsonFactory`, parsers e serializers manuais | depende de `domain` e usa Jackson como dependência opcional |
| `declaracoes-gov-core-crypto` | `CertificateProvider`, `AbstractKeyStoreProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder` | depende de `domain` |
| `declaracoes-gov-core-xml` | `XmlDocuments`, `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions` | depende de `domain` e `crypto` |
| `declaracoes-gov-core-transport` | `HttpRequest`, `HttpResponse`, `RestTransport`, `RetryPolicy`, `ProxyConfig`, `TransportException`, `ApacheHttpClientRestTransport` | depende de `crypto` e isola HttpClient 5 |
| `declaracoes-gov-core-bom` | versão alinhada de `domain`, `format`, `crypto`, `xml` e `transport` | sem `src` e sem API de runtime |

## 2. Mapa de pacotes manuais

| Pacote | Conteúdo atual | Papel |
| --- | --- | --- |
| `br.uem.npd.govcore.model` | `Cnpj`, `Cpf`, `Nis`, `Caepf`, `Cno`, `Cei`, `CodigoMunicipio`, `PeriodoApuracao`, `Recibo`, `Vigencia`, interfaces de inscrição | domínio brasileiro reutilizável |
| `br.uem.npd.govcore.model.layout` | `LayoutVersion`, `NormativeSource`, `RecordDefinition`, `FieldDefinition`, `Constraint`, `ValidityWindow` | metadados manuais para catálogos de leiaute |
| `br.uem.npd.govcore.table` | `TipoInscricao`, `TipoAmbiente`, `Uf` | tabelas manuais estáveis |
| `br.uem.npd.govcore.validator` | `GovValidators`, `GovValidationCatalog`, `ValidationMetadata`, `ValidationLevel`, `Modulo11` e validadores concretos | contrato público de validação |
| `br.uem.npd.govcore.exception` | `GovCoreException` e derivadas | hierarquia coesa de erro |
| `br.uem.npd.govcore.format.parser` | `DelimitedParser`, `DelimitedSerializer`, `FixedLengthParser`, `FixedLengthSerializer` | parsers manuais de arquivo |
| `br.uem.npd.govcore.util` | normalizadores, formatos, JSON e datas XML | utilitários leves de integração |
| `br.uem.npd.govcore.crypto` | providers e `SSLContext` | segurança e certificado |
| `br.uem.npd.govcore.signature` | `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions` | assinatura XML configurável |
| `br.uem.npd.govcore.transport` | request/response, proxy, retry e exceções neutras | fronteira HTTP reutilizável por composição |
| `br.uem.npd.govcore.transport.apache` | adaptador Apache HttpClient 5 | implementação padrão isolada |

## 3. Dependências internas

- `format` depende de `domain` porque formata tipos e contratos do core.
- `crypto` depende de `domain` para reutilizar exceções e convenções transversais.
- `xml` depende de `domain` e `crypto` porque a assinatura XML reutiliza a infraestrutura de certificado.
- `transport` depende de `crypto` para reaproveitar material TLS/mTLS sem conhecer payload ou regra de declaração.
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

### DD-05 — Transporte neutro fica isolado
`core-transport` pode conhecer HTTP, proxy, timeout e retry genérico, mas não conhece endpoint de governo, token, contrato SOAP/REST ou fluxo de entrega.

## 5. Modelo de thread-safety

| Área | Estratégia atual |
| --- | --- |
| value objects e enums | imutáveis |
| validadores | stateless |
| utilitários de formato e XML | uso estático ou configuração explícita |
| SPI de transporte | value objects imutáveis + implementação injetável |
| providers PKCS#11/A3 | dependem do ambiente do consumidor e exigem cuidado operacional |
| BOM interno | sem estado e sem runtime |

## 6. Pontos de extensão permitidos

- novo documento, enum ou metadado manual -> `domain`;
- novo parser/serializer ou normalizador -> `format`;
- novo provider ou helper de trust material -> `crypto`;
- novo helper DOM ou política de assinatura -> `xml`.
- nova SPI ou política HTTP neutra -> `transport`.

## 7. Não objetivos

- clientes HTTP/SOAP/REST específicos de declaração;
- regra negocial de obrigação acessória;
- geração de código a partir de artefatos oficiais;
- catálogo de endpoints, tokens ou contratos oficiais.
