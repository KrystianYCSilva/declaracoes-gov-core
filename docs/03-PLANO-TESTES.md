# Plano de testes do declaracoes-gov-core

## 1. Objetivo

Validar o contrato público do reator e garantir que os módulos manuais do core continuem coerentes entre si.

## 2. Comando principal

Da raiz de `declaracoes-gov-core`:

```bash
mvn verify
```

Esse é o gate atual do módulo e cobre compilação, testes e checagem JaCoCo configurada no parent.

## 3. Escopo das suítes atuais

| Módulo | Suítes representativas | O que precisa continuar coberto |
| --- | --- | --- |
| `declaracoes-gov-core-domain` | `model/*Test`, `model/layout/LayoutModelsTest`, `validator/*Test`, `table/*Test`, `exception/ExceptionsTest` | value objects, catálogo de confiança, `Modulo11`, enums e exceções |
| `declaracoes-gov-core-format` | `format/parser/*Test`, `util/GovJsonFactoryTest`, `GovTextNormalizerTest`, `GovNumberFormatsTest`, `GovCompetenceFormatsTest`, `XmlDatesTest` | parsers manuais, normalização, números, competência, datas XML e JSON governamental |
| `declaracoes-gov-core-crypto` | `AbstractKeyStoreProviderTest`, `Pkcs12ProviderTest`, `Pkcs11ProviderTest`, `SslContextBuilderTest` | providers A1/A3, leitura de keystore e `SSLContext` |
| `declaracoes-gov-core-xml` | `XmlDocumentsTest`, `XmlSignatureOptionsTest`, `XmlDsigSignerTest` | parsing seguro, opções explícitas de assinatura e assinatura XML |
| `declaracoes-gov-core-transport` | `HttpRequestTest`, `HttpResponseTest`, `ProxyConfigTest`, `TransportExceptionTest`, `RetryPolicyIntegrationTest`, `apache/*Test` | contrato da SPI HTTP, retry, proxy, timeouts, mTLS e adaptador Apache |
| `declaracoes-gov-core-bom` | não possui suíte própria | módulo POM-only; a validação relevante é manter o reator verde e o BOM sem código |

## 4. Gates documentados

| Módulo | Gate atual |
| --- | --- |
| `domain`, `format`, `xml`, `transport` | JaCoCo mínimo `0.90` linha / `0.90` ramo |
| `crypto` | JaCoCo mínimo `0.85` linha / `0.90` ramo |
| `core-bom` | JaCoCo desabilitado |
| reator | `mvn verify` |

## 5. Regressões que merecem atenção extra

- mudanças na política `OFFICIAL`/`PROVISIONAL`/`STRUCTURAL` exigem sincronismo imediato com `docs/05-MATRIZ-VALIDADORES.md`;
- `Cpf` e `Nis` precisam preservar o caminho padrão estrutural e o opt-in provisório;
- testes negativos de XML podem emitir mensagens do parser no stderr por desenho do teste, sem caracterizar falha do build;
- o adaptador Apache em `core-transport` é coberto principalmente por testes de integração; o gate usa exclusão explícita para a fronteira pura de I/O;
- cenários de PKCS#11 e A3 continuam limitados ao que a suíte manual consegue simular sem hardware real.

## 6. Critérios de aceite

- `mvn verify` verde;
- nenhuma suíte crítica removida sem substituição equivalente;
- documentação técnica atualizada quando o contrato público mudou;
- gates JaCoCo preservados conforme os POMs atuais.
