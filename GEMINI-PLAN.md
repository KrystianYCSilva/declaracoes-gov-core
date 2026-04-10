# Plano de Implementação: Declaracoes Gov - Core

## 1. Visão Geral e Objetivo
A biblioteca `declaracoes-gov-core` é o alicerce (fundação) de todo o ecossistema de bibliotecas de integrações com o Governo Federal (SPED, Serpro, e-CAC).
Seu objetivo é centralizar as responsabilidades técnicas transversais que se repetem em todas as declarações (eSocial, EFD-Reinf, DCTFWeb, PGDAS, etc.), evitando duplicação de código, garantindo manutenibilidade e padronizando o comportamento de segurança e criptografia.

## 2. Escopo Arquitetural

### O que a biblioteca FARÁ:
- **Segurança e Criptografia (mTLS):** Carregamento de Certificados Digitais ICP-Brasil (A1 em PKCS12 e A3 em PKCS11) e montagem do `SSLContext` para conexões HTTPS.
- **Assinatura Digital (XMLDSIG):** Lógica genérica de assinatura de XML (usada pelo eSocial e EFD-Reinf).
- **Serializadores Universais (Jackson/JSON):** Fornecer instâncias do `ObjectMapper` já configuradas com o padrão exigido pelas APIs REST do Serpro (ex: `NON_NULL`, formato de datas `yyyy-MM-dd` ou `ISO-8601`, BigDecimal para valores financeiros).
- **Validadores de Domínio:** Anotações e lógicas de validação para tipos comuns do Brasil (CNPJ, CPF, NIS, CEI, CNO).
- **Modelos Básicos:** Enums transversais como `TipoAmbiente` (Produção, Produção Restrita) e `TipoInscricao` (CNPJ, CPF).

### O que a biblioteca NÃO FARÁ:
- Não conterá leiautes específicos de nenhuma declaração (nenhum XSD ou OpenAPI).
- Não fará conexões HTTP (transporte de dados).

## 3. Migração de Código Existente
Para construir este módulo, iremos extrair (refatorar) as seguintes classes e pacotes que atualmente estão embutidos (ou duplicados) nos projetos `declaracoes-esocial-leiautes` e `declaracoes-esocial-transmissor`:
- `br.uem.npd.esocial.table.TipoAmbiente` -> `br.uem.npd.gov.core.domain.Environment`
- `br.uem.npd.esocial.table.TipoInscricao` -> `br.uem.npd.gov.core.domain.TaxIdType`
- `br.uem.npd.esocial.validators.*` -> `br.uem.npd.gov.core.validation.*`
- `br.uem.npd.esocial.transmissor.security.CertificateProvider` -> `br.uem.npd.gov.core.security.CertificateProvider`
- `br.uem.npd.esocial.transmissor.signature.XmlSigner` -> `br.uem.npd.gov.core.signature.XmlSigner`

## 4. Dependências
- `jackson-databind` e `jackson-datatype-jsr310` (Para o módulo JSON).
- `xmlsec` (Apache Santuario para assinatura de XML).
- Nenhuma dependência pesada de frameworks web (como Spring). Manter o JAR enxuto.

## 5. Próximos Passos
1. Criar o `pom.xml` definindo este artefato como Java 8.
2. Trazer as classes de segurança, validação e assinatura listadas acima.
3. Garantir alta cobertura de testes unitários (+95%), pois bugs aqui quebram todas as declarações.