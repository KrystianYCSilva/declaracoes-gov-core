---
work_package_id: WP08
title: KeyManagerFactoryBuilder + GovSslContextFactory
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.183157+00:00'
subtasks:
- T001: Criar `KeyManagerFactoryBuilder`
- T002: Criar `GovSslContextFactory`
- T003: Testes de integração com infra crypto test-jar
- T004: Atualizar `SslContextBuilder` se houver sobreposição
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:47:39.880315+00:00"
loops_doing_to_done: "1"
ended_at: "2026-04-20T21:58:24.553380+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP08 — KeyManagerFactoryBuilder + GovSslContextFactory

## Context

O módulo `crypto` possui `Pkcs12Provider` e `SslContextBuilder` para acesso a certificados, mas carece de dois componentes adicionais que aparecem como duplicatas em projetos consumidores: `KeyManagerFactoryBuilder` (tradução Java de versão Kotlin em `obrigacoes-service-reinf`) e `GovSslContextFactory` (para criação de `SSLContext` dinâmico multi-tenant por CNPJ, extraído de `v2/esocial-tombamento`). Ambos dependem apenas de `javax.net.ssl.*` e `java.security.*` do JDK. A hierarquia de exceções expandida do WP04 (`CertificadoInvalidoException`) é usada para reportar erros de certificado.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `crypto` ≥ 85% linha + ≥ 90% branch
- Dependências: `domain` + JDK `javax.net.ssl.*` + `java.security.*` (zero dependências externas novas)
- BouncyCastle (`bcpkix-jdk18on`) disponível apenas em test scope
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Criar `KeyManagerFactoryBuilder`

**Objetivo:** Criar builder Java que constrói um `KeyManagerFactory` a partir de um certificado PFX em Base64 e sua senha, traduzindo a implementação Kotlin de `obrigacoes-service-reinf` para Java idiomático.

**Passos:**
1. Criar `declaracoes-gov-core-crypto/src/main/java/br/com/contabilizei/obrigacoes/govcore/crypto/KeyManagerFactoryBuilder.java`
2. Declarar como `public final class KeyManagerFactoryBuilder`
3. Método principal: `public KeyManagerFactory build(String pfxBase64, String senha) throws CertificadoInvalidoException`
   - Decodificar `pfxBase64` com `java.util.Base64`
   - Carregar `KeyStore` do tipo `PKCS12` a partir dos bytes
   - Criar e inicializar `KeyManagerFactory` com o `KeyStore` e senha
   - Em caso de erro (`KeyStoreException`, `UnrecoverableKeyException`, `NoSuchAlgorithmException`, `IOException`), encapsular em `CertificadoInvalidoException`
4. Verificar a implementação Kotlin original em `obrigacoes-service-reinf` para garantir paridade de comportamento
5. Complemento ao `Pkcs12Provider` existente — não duplicar lógica; usar `Pkcs12Provider` internamente se possível
6. Javadoc em português

**Arquivos:**
- `declaracoes-gov-core-crypto/src/main/java/br/com/contabilizei/obrigacoes/govcore/crypto/KeyManagerFactoryBuilder.java`

**Validação:**
- Com PFX de teste (Base64) e senha correta: retorna `KeyManagerFactory` inicializado
- Com senha incorreta: lança `CertificadoInvalidoException`
- Com Base64 inválido: lança `CertificadoInvalidoException`

**Edge cases:**
- `pfxBase64 == null` lança `CertificadoInvalidoException` com mensagem descritiva
- `senha == null` deve ser suportado (PFX sem senha) — documentar
- Garantir que o `KeyStore` é criado com algoritmo `PKCS12`, não `JKS`

---

### T002 — Criar `GovSslContextFactory`

**Objetivo:** Criar factory de `SSLContext` dinâmico que aceita certificado por CNPJ do chamador, permitindo multi-tenancy em cenários onde cada tenant tem seu próprio certificado (ex: tombamento eSocial).

**Passos:**
1. Criar `declaracoes-gov-core-crypto/src/main/java/br/com/contabilizei/obrigacoes/govcore/crypto/GovSslContextFactory.java`
2. Declarar como `public final class GovSslContextFactory`
3. Método principal: `public SSLContext create(String pfxBase64, String senha) throws CertificadoInvalidoException`
   - Usar `KeyManagerFactoryBuilder.build()` para obter o `KeyManagerFactory`
   - Criar `SSLContext` com protocolo `TLSv1.2` (mínimo seguro)
   - Inicializar com `KeyManager[]` do factory, `null` TrustManager (sistema), `null` SecureRandom
   - Retornar `SSLContext` pronto para uso
4. Método alternativo opcional: `SSLContext createWithTrustStore(String pfxBase64, String senha, TrustManager[] trustManagers)` — para casos que precisam de TrustManager customizado
5. Verificar implementação em `v2/esocial-tombamento` para garantir paridade
6. Javadoc em português; documentar que o SSLContext não é cacheado (responsabilidade do chamador)

**Arquivos:**
- `declaracoes-gov-core-crypto/src/main/java/br/com/contabilizei/obrigacoes/govcore/crypto/GovSslContextFactory.java`

**Validação:**
- Com PFX de teste válido: retorna `SSLContext` com protocolo `TLSv1.2` ou superior
- `sslContext.getProtocol()` retorna `"TLSv1.2"` ou `"TLS"`
- Com certificado inválido: lança `CertificadoInvalidoException`

**Edge cases:**
- `SSLContext` criado deve usar TLS 1.2 mínimo (não TLS 1.0 ou SSL — inseguro)
- Documentar que o chamador é responsável por cachear o `SSLContext` para desempenho (criação é custosa)
- Multi-threading: `SSLContext` gerado deve ser thread-safe para uso concorrente após criação

---

### T003 — Testes de integração com infra crypto test-jar

**Objetivo:** Verificar o comportamento real dos novos componentes usando certificados de teste gerados com BouncyCastle (já disponível em test scope no módulo `crypto`).

**Passos:**
1. Verificar a infraestrutura de testes do `crypto` test-jar em `declaracoes-gov-core-crypto/src/test/`
2. Localizar utilitários de geração de certificado de teste (BouncyCastle) já existentes
3. Criar `declaracoes-gov-core-crypto/src/test/java/br/com/contabilizei/obrigacoes/govcore/crypto/KeyManagerFactoryBuilderTest.java`:
   - Gerar certificado PFX de teste via BouncyCastle
   - Encodar em Base64
   - Verificar que `KeyManagerFactoryBuilder.build()` retorna `KeyManagerFactory` funcional
   - Verificar que senha errada lança `CertificadoInvalidoException`
4. Criar `declaracoes-gov-core-crypto/src/test/java/br/com/contabilizei/obrigacoes/govcore/crypto/GovSslContextFactoryTest.java`:
   - Verificar que `SSLContext` criado com certificado de teste não é nulo
   - Verificar protocolo TLS

**Arquivos:**
- `declaracoes-gov-core-crypto/src/test/java/br/com/contabilizei/obrigacoes/govcore/crypto/KeyManagerFactoryBuilderTest.java`
- `declaracoes-gov-core-crypto/src/test/java/br/com/contabilizei/obrigacoes/govcore/crypto/GovSslContextFactoryTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-crypto` verde
- JaCoCo ≥ 85% linha + ≥ 90% branch no módulo `crypto`

**Edge cases:**
- Usar certificado de teste com data de validade futura (não certificado real)
- Verificar que testes não falham por diferença de provedor JCE entre JDKs

---

### T004 — Atualizar `SslContextBuilder` se houver sobreposição

**Objetivo:** Garantir que `SslContextBuilder` existente e `GovSslContextFactory` novo não duplicam lógica — refatorar para delegação se necessário.

**Passos:**
1. Comparar a API e implementação de `SslContextBuilder` com `GovSslContextFactory`
2. Se houver sobreposição significativa: refatorar `GovSslContextFactory` para delegar a `SslContextBuilder` ou vice-versa
3. Se as responsabilidades são distintas: adicionar comentário no Javadoc de cada classe explicando a diferença
4. Garantir que nenhum teste existente quebrou

**Arquivos:**
- `declaracoes-gov-core-crypto/src/main/java/br/com/contabilizei/obrigacoes/govcore/crypto/SslContextBuilder.java` — revisão (somente leitura se não mudar)

**Validação:**
- `mvn -B -q verify` no reactor completo sem regressões

**Edge cases:**
- Se `SslContextBuilder` usa fluent API e `GovSslContextFactory` usa método estático, manter ambos como estilos complementares — documentar a diferença no Javadoc

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP08
```

## Review Feedback

TBD

## Activity Log

- 2026-04-20T21:47:39Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:58:24Z – unknown – lane=done – Moved to done
