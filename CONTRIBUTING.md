# Contribuindo com declaracoes-gov-core

## Princípios do módulo

- preserve compatibilidade com Java 8;
- trate `declaracoes-gov-core` como fundação transversal, não como transmissor;
- mantenha a fronteira entre `domain`, `format`, `crypto` e `xml`;
- não apresente heurística como validação normativa oficial;
- atualize a documentação humana no mesmo change set sempre que API pública, build ou fronteira arquitetural mudar.

## Fluxo mínimo de contribuição

1. classifique a mudança: domínio, formatação, XML, criptografia ou BOM interno;
2. altere o código no módulo correto e ajuste os testes afetados;
3. sincronize a documentação canônica do root e de `docs/`;
4. rode o gate atual do módulo:

```bash
mvn verify
```

5. abra o PR explicando problema, abordagem, impacto público e documentos atualizados.

## Quando a documentação precisa ser atualizada

| Tipo de mudança | Documentos mínimos |
| --- | --- |
| API pública de value objects, exceções ou utilitários | `README.md`, `ARCHITECTURE.md`, `docs/01-REQUISITOS.md`, `docs/02-DESIGN.md` |
| política de validadores ou catálogo de confiança | `README.md`, `docs/03-PLANO-TESTES.md`, `docs/05-MATRIZ-VALIDADORES.md` |
| build, gates, ordem de publicação ou consumo Maven | `README.md`, `ONBOARDING.md`, `CONTRIBUTING.md`, `docs/04-IMPLANTACAO.md` |
| material histórico substituído | `docs/hist/README.md` e o índice do ciclo arquivado |

## Qualidade mínima antes da revisão

- `mvn verify` verde no reator;
- JaCoCo preservado em `0.90` linha e `0.90` ramo por padrão;
- exceção conhecida: `declaracoes-gov-core-crypto` reduz o gate de linhas para `0.85` e mantém `0.90` para ramos;
- `declaracoes-gov-core-bom` continua sem código Java e com JaCoCo desabilitado;
- sem mudança que empurre transporte, OAuth2 ou regra específica de declaração para o core.

## Mudanças que não cabem aqui

Não adicione neste módulo:

- cliente HTTP, SOAP ou REST;
- autenticação OAuth2 ou lógica de endpoint;
- persistência, fila ou workflow operacional;
- código gerado por XSD, WSDL ou OpenAPI;
- regra negocial específica de eSocial, Reinf, Integra Contador ou outra declaração.

## O que toda revisão deve explicar

- módulo(s) alterado(s);
- impacto na API pública ou nos consumidores;
- testes executados;
- documentação sincronizada;
- eventuais riscos residuais, especialmente em validadores provisórios e PKCS#11.
