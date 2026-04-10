# Plano de Testes: Declaracoes Gov Core

## 1. Visão Geral
Este plano descreve as estratégias e suítes de teste necessárias para garantir a integridade dos componentes críticos da biblioteca `declaracoes-gov-core`. A meta global de cobertura (*Code Coverage*) é **90%** (via JaCoCo), focada nas linhas de código e caminhos lógicos (branches).

## 2. Níveis de Teste

### 2.1 Testes Unitários
Testes isolados (JUnit 4) e focados em métodos puros. Nenhum teste unitário deve depender de acesso externo à internet.

#### A. Suíte de Value Objects e Validators (Extremamente Crítico)
*   **CnpjValidatorTest:**
    *   Testar com Cnpjs numéricos conhecidos válidos.
    *   Testar com Cnpjs com dígitos verificadores errados (retorno `false`).
    *   Testar com Cnpjs numéricos contendo tamanhos inválidos (ex: 13 ou 15 dígitos).
    *   **Cenário Especial (CNPJ Alfanumérico):** Testar o comportamento do novo cálculo (ASCII - 48) conforme documentação da RFB.
*   **CpfValidatorTest:**
    *   Testar CPFs válidos (Módulo 11).
    *   Testar regras de rejeição de strings homogêneas ("111.111.111-11", "000.000.000-00"), que apesar de passarem no algoritmo m11, são proibidas.
*   **PeriodoApuracaoTest:**
    *   Testar *parse* de múltiplos formatos ("YYYYMM", "YYYY-MM", "MM/YYYY").
    *   Testar exceções em formatos absurdos.

#### B. Suíte de Segurança (Certificate Provider)
*   **Pkcs12ProviderTest:**
    *   Utilizar um arquivo certificado `.pfx` de teste (mock/self-signed criado no build ou com a biblioteca Bouncy Castle) e testar a extração correta de chave e cadeias X509.
    *   Testar falha elegante ("Fail-Fast") com `CertificateException` caso a senha fornecida seja incorreta ou se o arquivo for inválido.

#### C. Suíte de Assinatura XML (XmlDsigSigner)
*   **XmlDsigSignerTest:**
    *   Pegar um fragmento XML simples simulando um evento eSocial/Reinf.
    *   Carregar o certificado de teste.
    *   Assinar e validar se as *tags* de assinatura (`<Signature>`, `<SignedInfo>`, `<X509Data>`) foram inseridas corretamente.
    *   Validar a estrutura criptográfica com a própria API nativa Java de validação de XMLDSIG.

### 2.2 Testes de Concorrência (Thread-Safety)
Como o core será usado intensamente em servidores que lidam com múltiplas requisições (Tomcat, Undertow, Batch Workers), devemos garantir a segurança de execução paralela.
*   **ConcurrencyTest:** Subir *N* threads (ex: `Executors.newFixedThreadPool(100)`). Executar operações sobre o `GovValidators.isCnpjValid()`, `Cnpj.of()` e `XmlDsigSigner.sign()`. O teste é considerado bem sucedido se nenhuma *Exception* vazada ou corrupção de estado (ex: assinatura gerada a partir da thread errada) ocorrer.

## 3. Ferramentas e Configurações
*   **Framework:** JUnit 4 (Padrão corporativo herdado) / Mockito.
*   **Certificados de Teste:** Emprego de um gerador estático (Bouncy Castle) nos testes ou fornecimento de um `mock-cert.p12` no diretório `src/test/resources` (com chave falsa para propósitos exclusivos de teste de assinatura).
*   **Execução Automática:** Incorporado à fase `mvn verify` integrado com relatório de cobertura `jacoco`.
