# PLANO UNIFICADO V1.0.0: Declaracoes Gov Core

Este documento é a síntese e unificação dos planejamentos arquiteturais propostos pelas frentes (Gemini, Qwen, Kimi e OpenCode) para a criação do `declaracoes-gov-core`. Ele serve como um **Pré-Documento de Requisitos** e visão macro para a construção da biblioteca fundacional do ecossistema de declarações governamentais brasileiras.

---

## 1. Visão e Princípios Arquiteturais (A Alma do Core)

O **`declaracoes-gov-core`** será uma biblioteca Java 8+, atuando como o motor central e agnóstico para as complexidades fiscais e criptográficas do Governo Federal (eSocial, EFD-Reinf, DCTFWeb, PGDAS, DEFIS, MIT, PER/DCOMP).

**Princípios Fundamentais:**
1.  **Imutabilidade e Thread-Safety Nativo:** Todas as classes principais (Value Objects, Factories, Validadores e Signers) serão *stateless* ou imutáveis. O core foi projetado para sobreviver a ambientes multithreading intensos de grandes ERPs sem *locks* ou vazamentos.
2.  **Zero Bloatware e Frameworks:** Sem dependências invasivas. Não usaremos `javax.validation` (Bean Validation), Spring ou Hibernate Validator. A validação é matemática e pura. Dependências limitadas a: `jackson-databind/jsr310` (opcional/provided), `slf4j-api` e `xmlsec`.
3.  **KISS e YAGNI:** Foco absoluto em resolver as reais "dores de cabeça" mapeadas na comunidade (ex: Erro MS0030 XML, falhas de TLS, formatação rigorosa de JSON governamental).
4.  **SOLID & Design Patterns:** Uso extensivo de *Strategy*, *Factory* e *Value Objects* para blindar a biblioteca contra a instabilidade legislativa do país (ex: adoção do CNPJ Alfanumérico).

---

## 2. Visão Macro dos Componentes (O que será implementado)

### 2.1. Modelos Fiscais (Value Objects - V.O.)
Em vez de trafegar `String` soltas pelo sistema, usaremos Classes de Valor que garantem sua própria integridade no momento da instanciação.
*   `Cnpj`: Suporta e valida a versão numérica tradicional (14 dígitos) e a futura versão **Alfanumérica (Julho/2026)**.
*   `Cpf`: Validação rigorosa com regras contra dígitos repetidos.
*   `Nis`: Representa PIS/PASEP/NIT (Módulo 11).
*   `Cei`, `Cno`, `Caepf`: V.O.s para identificadores de obra, rural e específicos.
*   `PeriodoApuracao` / `Competencia`: Wrapper imutável (ex: `yyyy-MM`) facilitando comparações temporais de fechamentos.
*   `Recibo` / `Protocolo`: Representam os retornos de integração com chaves de até 40 posições.

### 2.2. Algoritmos de Validação (Padrão *Strategy*)
O motor que alimenta os Value Objects acima. Fica isolado para permitir a injeção de novas regras fiscais sem quebrar contratos.
*   `DocumentValidator` (Interface central)
*   `NumericCnpjValidator` (Módulo 11 Base 10)
*   `AlphanumericCnpjValidator` (Módulo 11 Base 36 / ASCII - Ato Declaratório 15/2024)
*   `CpfValidator`, `NisValidator`, `CnoValidator`, etc.
*   `GovValidators` (Facade estático utilitário para consumo rápido).

### 2.3. Contratos e Enumerações Universais
Interfaces fundamentais que parametrizam como as outras bibliotecas (e ERPs consumidores) entenderão os dados.
*   `Vigencia<T>`: Define o *Time-to-Live* temporal (início/fim) de regras e tabelas fiscais.
*   `IdentificadorEmpregador`: Interface polimórfica que permite tratar `Cnpj`, `Cpf`, `Cno` e `Caepf` como a mesma entidade transmissora.
*   `TipoAmbiente` (Enum): Produção (1) vs Produção Restrita (2).
*   `TipoInscricao` (Enum): Padronização governamental (CNPJ=1, CPF=2, CAEPF=3, etc).

### 2.4. Infraestrutura de Segurança (Criptografia e mTLS)
Abstração completa do *Keystore* do Java para garantir conectividade segura sem dor de cabeça.
*   `CertificateProvider` (Interface).
*   `AbstractKeyStoreProvider` (Rotinas defensivas para extrair cadeias `X509Certificate` e `PrivateKey`).
*   `Pkcs12Provider` (Certificados A1).
*   `Pkcs11Provider` (Certificados A3 via smartcards/tokens).
*   `SslContextBuilder` / `MtlsConnectionFactory`: *Factory* thread-safe que constrói instâncias do `javax.net.ssl.SSLContext` travadas em protocolos modernos (`TLSv1.2` ou superior), suportando as cifras criptográficas corretas do portal e-CAC.

### 2.5. Assinatura Digital (XMLDSIG)
Necessário para as declarações do escopo SPED/XML (eSocial, EFD-Reinf).
*   `XmlSigner` (Interface).
*   `XmlDsigSigner`: Motor que implementa a Canonicalização (`C14N_INCLUSIVE`), o padrão "Enveloped", método de chave `RSA-SHA256` e Digest `SHA-256`, em conformidade estrita com o manual do desenvolvedor.

### 2.6. Formatadores e Utilitários Blindados
Utilitários desenhados para evitar os famosos erros de rejeição sintática (MS0030 / Bad Request 400).
*   **`GovJsonFactory`:** Fornece um `ObjectMapper` do Jackson customizado. Configurações chaves: supressão de chaves nulas (`NON_NULL`), datas ISO-8601 estritas sem timestamps, e `BigDecimal` serializados em `PlainString` (evita notação científica).
*   **`XmlHelper` / `XmlDocuments`:** Parsers de DOM seguros, blindados contra injeções de entidade (XXE), e mecanismos limpos de injeção e remoção de *Namespaces* indesejados.
*   **`XmlDates`:** Conversores padronizados entre `OffsetDateTime`/`LocalDate` nativos e o formato XSD string.

### 2.7. Hierarquia de Exceções Padronizada
Substitui retornos silenciosos ou genéricos por falhas de domínio mapeadas.
*   `GovCoreException` (Runtime Base)
    *   `InvalidDocumentException` (Erro em CNPJ/CPF/Formatos)
    *   `GovSecurityException` (Falha em leitura de KeyStore, senhas incorretas)
    *   `GovSignatureException` (Falhas ao processar o XMLDSIG)
    *   `GovCommunicationException` (Envelopamento genérico para erros previstos do TLS/Rede)

---

## 3. Resumo da Abordagem de Engenharia

Ao unificar o conhecimento e as melhores práticas das frentes em um único plano, este `declaracoes-gov-core` atuará como um isolante térmico. O sistema do usuário final (ERP, Folha de Pagamento) se preocupa exclusivamente em popular os dados do negócio; a biblioteca toma conta da conformidade da string, do parsing matemático das datas e números, e da burocracia de instanciar protocolos seguros (TLS/Assinatura).

**Próximo Passo Lógico:** Com essa visão consolidada, a transição natural é iniciar o documento de requisitos arquiteturais e, subsequentemente, a materialização da estrutura Maven (`pom.xml`) e das classes base do pacote `br.uem.npd.govcore`.