# Arquitetura do declaracoes-gov-core

## Papel estrutural

`declaracoes-gov-core` é um reator Maven do tipo `pom` que organiza a fundação manual do ecossistema `declaracoes-*`. O diretório existe para manter limites claros entre domínio brasileiro, formatação, criptografia e XML, além de publicar um BOM interno para consumidores do próprio core.

## Topologia atual

| Módulo | Dependências internas | Pacotes principais | Responsabilidade |
| --- | --- | --- | --- |
| `declaracoes-gov-core-domain` | nenhuma | `model`, `model.layout`, `table`, `validator`, `exception` | value objects, metadados de leiaute, catálogo de confiança e exceções-base |
| `declaracoes-gov-core-format` | `domain` | `format.parser`, `util` | normalização, parsers/serializers e `GovJsonFactory` |
| `declaracoes-gov-core-crypto` | `domain` | `crypto` | certificados A1/A3, PKCS#12, PKCS#11 e `SSLContext` |
| `declaracoes-gov-core-xml` | `domain`, `crypto` | `signature`, `util` | parsing XML seguro, DOM e assinatura XML |
| `declaracoes-gov-core-bom` | gerencia `domain`, `format`, `crypto` e `xml` | sem `src/` | alinhamento de versões para consumidores do core |

## Regras de dependência

- `domain` é a base e não depende dos demais módulos do core;
- `format` adiciona utilitários leves sobre `domain`;
- `crypto` isola a infraestrutura de certificados para não contaminar módulos leves;
- `xml` reaproveita `crypto` apenas quando a assinatura ou o trust material são necessários;
- `declaracoes-gov-core-bom` existe para consumo Maven e não deve receber código ou documentação de runtime.

## Decisões estruturais duráveis

### 1. O core não é transmissor

Nada neste reator deve assumir endpoint, protocolo, token, fila, retry ou regra negocial de declaração. Essas responsabilidades pertencem a módulos de leiaute, transmissor ou serviço.

### 2. Confiança do validador é contrato público

O pacote `validator` publica a distinção entre `OFFICIAL`, `PROVISIONAL` e `STRUCTURAL`. Isso aparece no código (`GovValidationCatalog`, `GovValidators`), nos testes e na documentação humana.

### 3. Dependência pesada fica opcional e isolada

Jackson fica concentrado em `format`; XML Security fica concentrado em `xml`; integração com keystore e PKCS#11 fica concentrada em `crypto`. Consumidores podem importar apenas o módulo necessário.

### 4. XML assinado exige alvo explícito

A API `XmlSignatureOptions` existe para evitar heurística implícita de assinatura. Mudanças nessa fronteira precisam preservar a independência do core em relação a declarações específicas.

## Pontos de extensão

- novos documentos, enums e metadados manuais -> `declaracoes-gov-core-domain`;
- novos parsers manuais, normalizadores ou formatos -> `declaracoes-gov-core-format`;
- novos providers ou utilitários de trust material -> `declaracoes-gov-core-crypto`;
- novos helpers DOM ou assinadores XML -> `declaracoes-gov-core-xml`.

## Fora de escopo

- transporte HTTP/SOAP/REST;
- OAuth2 e credenciais de canal;
- regras de negócio de obrigações específicas;
- geração de código a partir de artefatos oficiais.
