# Arquitetura e Planejamento: Declaracoes Gov - Core

## 1. Visão Geral
A biblioteca `declaracoes-gov-core` (v1.0.0, Java 8) é a fundação criptográfica, de infraestrutura e de domínio para todas as interações com os serviços da Receita Federal (eSocial, EFD-Reinf, Integra Contador, SPED).
O objetivo principal é abstrair as complexidades legais e tecnológicas em componentes **eficientes, 100% thread-safe e completamente agnósticos**.

## 2. Princípios Arquiteturais e Boas Práticas
- **Zero Frameworks de Validação:** Sem dependência de `javax.validation` ou Spring. Validações via algoritmos puros (ex: Módulo 11).
- **KISS e YAGNI:** Mantenha simples. Não crie abstrações complexas para problemas que ainda não existem. Foco apenas nas dores reais relatadas pelos desenvolvedores de SPED/Integra Contador.
- **Imutabilidade e Thread-Safety:** Uso de Value Objects imutáveis. Utilitários devem ser *stateless*.
- **SOLID (Padrão Strategy):** Uso de *Strategy* para lidar com volatilidade da legislação (ex: CNPJ Alfanumérico), garantindo o Open/Closed Principle.
- **Clean Code:** Nomes autoexplicativos, tratamento explícito de erros, isolamento de responsabilidades.

## 3. As Maiores Dores dos Desenvolvedores (E como o Core resolve)
Pesquisas nos fóruns de desenvolvedores (Projeto ACBr, GitHub) revelam que as maiores dores de cabeça não são o layout em si, mas sim:
1. **Erro MS0030 (XML Inválido) / Desconformidade com XSD:** Causado por formatação errada de casas decimais, campos opcionais gerando tags vazias, ou datas fora do formato.
2. **Erros de TLS/Certificado:** Protocolos obsoletos ou cadeia de certificação falha.
3. **Falta de Padronização no Tratamento de Erros:** Respostas genéricas "Erro Lote" ou falhas assíncronas difíceis de rastrear.

Para mitigar isso, o Core fornecerá ferramentas à prova de falhas na serialização e conexões seguras nativas.

## 4. Elementos Comuns Mapeados (Domínio e Infraestrutura)

### 4.1. Value Objects: Identificadores Nacionais (Padrão Strategy)
Os identificadores não serão meras Strings. Serão classes blindadas que garantem a integridade da informação logo na instanciação.
- **`Cnpj`:** Algoritmo Strategy (Numérico Tradicional e Alfanumérico a partir de Julho/2026).
- **`Cpf`:** Módulo 11 duplo.
- **`Nis` (PIS/PASEP/NIT), `Cei`, `Cno`, `Caepf`:** Validadores específicos de Módulo 11/Híbridos.
- **`Recibo` / `Protocolo`:** Tipos comuns de retorno governamental (formatos com 40+ posições numéricas).

### 4.2. Contratos Universais (Interfaces e Abstrações Fiscais)
Baseado na análise estrutural do eSocial e Reinf, extrairemos interfaces fundamentais que representam conceitos fiscais brasileiros. Isso permite que sistemas ERP criem lógicas genéricas (ex: "Buscar todos os eventos ativos no mês X").

- **`Vigencia<T>`:** Define o tempo de vida de um evento ou tabela (Data Início e Data Fim). Essencial para controle de histórico (versionamento de tabelas).
- **`PeriodoApuracao` / `Competencia`:** Objeto de valor focado em "Ano/Mês" (`yyyy-MM`), centralizando lógicas de precedência (ex: "Não é possível fechar 2026-02 se 2026-01 está aberto").
- **`IdentificadorEmpregador`:** Interface comum para representar CNPJ, CPF, CNO ou CAEPF quando agem como titulares da declaração.
- **`Ambiente`:** Enumeração padrão (Produção = 1, Produção Restrita = 2).

### 4.3. Infraestrutura de Segurança (mTLS e XMLDSIG)
- **`CertificateProvider`:** Interface para abstrair `Pkcs12Provider` (A1) e `Pkcs11Provider` (A3).
- **`SslContextFactory`:** Construtor thread-safe de `javax.net.ssl.SSLContext` padronizado e forçado para `TLSv1.2`+. Resolve a dor de cabeça de "Conexão SSL Interrompida".
- **`XmlSigner`:** Assinador XML padrão "Enveloped" usando `RSA-SHA256` (Apache Santuario). Otimizado para reaproveitar a `PrivateKey` sem recarregar o KeyStore.

### 4.4. Formatadores Blindados (Evitando MS0030 e JSON inválido)
- **`GovJsonFactory`:** Retorna um `ObjectMapper` (Jackson) configurado para:
  - Não enviar campos nulos (`NON_NULL`).
  - Serializar `LocalDate`/`LocalDateTime` estritamente no padrão ISO-8601.
  - Formatar `BigDecimal` para `PlainString` (evitando notação científica que a Receita rejeita).
- **`GovXmlFactory`:** Utilitário base (se necessário no core) para garantir a correta injeção de namespaces em elementos XSD sem vazamento de memória do JAXBContext.

### 4.5. Padronização de Exceções
Uma hierarquia clara para facilitar o troubleshooting do ERP:
- `GovCoreException` (Base)
  - `InvalidDocumentException` (Ex: CNPJ ou CPF inválido)
  - `GovSecurityException` (Falha no certificado ou assinatura)
  - `GovCommunicationException` (Erros HTTP ou TLS formatados)

## 5. O Pipeline de Construção
1. **Inicialização:** `pom.xml` (Java 8), sem frameworks invasivos.
2. **Dependências Minhas:** `jackson-databind`, `jackson-datatype-jsr310`, `slf4j-api`, `xmlsec`.
3. **Qualidade:** Cobertura JaCoCo **95%**. Testes multithreading nos utilitários estáticos e testes rigorosos com os novos CNPJs Alfanuméricos.

## 6. Conclusão
Ao extrair não apenas os algoritmos de CNPJ/CPF, mas também os conceitos fiscais (`Vigencia`, `Competencia`) e a infraestrutura segura (`SSLContext`, `Formatadores`), o `declaracoes-gov-core` atuará como um isolante térmico entre o ERP e a volatilidade tecnológica da Receita Federal. O ERP lida com negócio; o Core lida com a burocracia do formato e da transmissão segura.