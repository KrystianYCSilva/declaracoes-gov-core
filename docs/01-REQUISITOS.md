# Requisitos do declaracoes-gov-core

## 1. Propósito

Definir o estado atual da fundação manual `declaracoes-gov-core`, organizada como reator Maven para reutilização em módulos de leiaute, transmissor e consumidores Java 11 da linha `v1.1.x`.

## 2. Público e consumidores

- mantenedores do ecossistema `declaracoes-*`;
- módulos que importam `declaracoes-gov-core-domain`, `declaracoes-gov-core-format`, `declaracoes-gov-core-crypto` e `declaracoes-gov-core-xml`;
- projetos consumidores que preferem importar `declaracoes-gov-core-bom`.

## 3. Escopo atual

### Em escopo

- reator `declaracoes-gov-core-parent` com seis módulos filhos;
- value objects, exceções e enums brasileiros;
- metadados manuais de leiaute em `br.uem.npd.govcore.model.layout`;
- catálogo público de validadores e níveis de confiança;
- utilitários manuais de formatação, JSON, XML, certificados e transporte neutro.

### Fora de escopo

- transporte específico de declaração, autenticação de canal e orquestração de entrega;
- regras específicas de eSocial, EFD-Reinf, Integra Contador ou outras famílias;
- código gerado por esquema oficial.

## 4. Inventário atual do reator

| Módulo | Embalagem | Papel |
| --- | --- | --- |
| `declaracoes-gov-core-parent` | `pom` | agregação, propriedades comuns e gate JaCoCo |
| `declaracoes-gov-core-bom` | `pom` | `dependencyManagement` do próprio core |
| `declaracoes-gov-core-domain` | `jar` | domínio, validadores, exceções e metadados manuais |
| `declaracoes-gov-core-format` | `jar` | formatação, parsers manuais e `GovJsonFactory` |
| `declaracoes-gov-core-crypto` | `jar` | certificados e `SSLContext` |
| `declaracoes-gov-core-xml` | `jar` | DOM seguro e assinatura XML |
| `declaracoes-gov-core-transport` | `jar` | SPI HTTP neutra, retry, proxy e adaptador Apache HttpClient |

## 5. Requisitos funcionais

### RF-01 — Reator multi-módulo
O parent deve continuar publicando os módulos `domain`, `format`, `crypto`, `xml`, `transport` e o BOM interno a partir de um único diretório raiz.

### RF-02 — Domínio brasileiro reutilizável
`declaracoes-gov-core-domain` deve manter os tipos manuais hoje usados pelo ecossistema, incluindo documentos, períodos, vigências, enums de inscrição/ambiente, exceções-base e metadados de leiaute.

### RF-03 — Política pública de validadores
O core deve manter `GovValidationCatalog` e `GovValidators` como fonte pública para distinguir validação `OFFICIAL`, `PROVISIONAL` e `STRUCTURAL`.

### RF-04 — Formatação manual agnóstica
`declaracoes-gov-core-format` deve continuar oferecendo normalização textual, formatos numéricos e de competência, datas XML, `GovJsonFactory` e parsers/serializers delimitados ou posicionais sem acoplamento a framework.

### RF-05 — Criptografia transversal
`declaracoes-gov-core-crypto` deve manter abstrações de certificado e `SSLContext` para A1/A3, PKCS#12 e PKCS#11 sem assumir protocolo de transporte.

### RF-06 — XML seguro e configurável
`declaracoes-gov-core-xml` deve manter parsing XML seguro, utilitários DOM, `XmlSigner`, `XmlDsigSigner` e `XmlSignatureOptions` como fronteira genérica de assinatura.

### RF-07 — Transporte neutro por composição
`declaracoes-gov-core-transport` deve manter uma SPI HTTP neutra, com `HttpRequest`, `HttpResponse`, `RestTransport`, `ProxyConfig`, `RetryPolicy`, `TransportException` e implementação Apache desacoplada de regra negocial.

### RF-08 — Consumo incremental
Consumidores do core devem poder importar somente os módulos necessários, preferencialmente via `declaracoes-gov-core-bom` quando quiserem uma matriz interna de versões.

## 6. Requisitos não funcionais

### RNF-01 — Compatibilidade Java 11
O reator deve continuar compilando com `maven.compiler.release` em `11`.

### RNF-02 — Agnosticidade de framework
Nenhum módulo do core deve exigir Spring, Jakarta EE, Bean Validation ou runtime equivalente para cumprir seu contrato.

### RNF-03 — Dependências internas mínimas
`domain` deve permanecer independente; `format`, `crypto` e `xml` só podem depender do que a fronteira técnica exigir.

### RNF-04 — Validação contínua do reator
O gate atual é `mvn verify`. O parent exige JaCoCo mínimo de `0.90` linha / `0.90` ramo por padrão, `declaracoes-gov-core-crypto` reduz linhas para `0.85` e `declaracoes-gov-core-bom` mantém JaCoCo desabilitado.

### RNF-05 — Thread-safety por contrato
Value objects e validadores manuais devem permanecer imutáveis ou stateless, e utilitários compartilhados só podem expor estado quando isso estiver documentado.

### RNF-06 — Documentação sincronizada
`README.md`, `ONBOARDING.md`, `CONTRIBUTING.md`, `ARCHITECTURE.md` e `docs/01-06` devem acompanhar o código e os POMs atuais.

## 7. Referências

- `pom.xml`
- `declaracoes-gov-core-bom/pom.xml`
- `declaracoes-gov-core-domain/pom.xml`
- `declaracoes-gov-core-format/pom.xml`
- `declaracoes-gov-core-crypto/pom.xml`
- `declaracoes-gov-core-xml/pom.xml`
- `declaracoes-gov-core-transport/pom.xml`
- `README.md`
- `docs/05-MATRIZ-VALIDADORES.md`
- `docs/06-GUIA-MIGRACAO-0.1.x-1.0.0.md`
