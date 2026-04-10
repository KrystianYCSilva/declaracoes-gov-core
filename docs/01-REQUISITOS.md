# Requisitos do Módulo: Declaracoes Gov Core

## 1. Introdução
Este documento descreve os requisitos funcionais e não funcionais específicos da biblioteca `declaracoes-gov-core`. Esta biblioteca é o alicerce criptográfico e de domínio (Value Objects) para todo o ecossistema de integrações com a Receita Federal do Brasil (SPED e Integra Contador).

## 2. Requisitos Não Funcionais (Técnicos)

### RNF01: Agnóstico e Sem Frameworks Invasivos
A biblioteca **não deve** depender de frameworks de validação como Hibernate Validator ou Java EE (JSR 380). Toda validação deve ser realizada programaticamente (matemática pura) dentro das próprias classes de valor. Apenas as bibliotecas `jackson-databind` (opcional), `slf4j-api` e `xmlsec` são permitidas.

### RNF02: Thread-Safety
Todos os validadores, conversores JSON/XML e utilitários de assinatura digital devem ser estritamente *stateless* ou imutáveis para suportar uso simultâneo por múltiplas threads em servidores de alta concorrência.

### RNF03: Compatibilidade Java
O código deve ser compatível com **Java 8**, visando maximizar a adoção em sistemas legados.

### RNF04: Cobertura de Testes
O projeto deve possuir uma cobertura de código aferida pelo JaCoCo de no mínimo **90%**, com testes englobando cenários válidos, inválidos e condições de contorno (ex: CNPJ alfanumérico).

## 3. Requisitos Funcionais

### RF01: Validação Estratégica de Documentos Fiscais
A biblioteca deve validar CNPJ e CPF em tempo de instanciação, implementando o padrão *Strategy* para os algoritmos. O validador de CNPJ deve prever a transição para a regra de "CNPJ Alfanumérico" (prevista para meados de 2026), baseando o cálculo Módulo 11 na conversão de caracteres ASCII.

### RF02: Modelagem via Value Objects
Entidades fiscais (CNPJ, CPF, Período de Apuração) devem ser representadas por Value Objects (ex: `Cnpj.java`). É proibido que essas classes assumam um estado inválido; a falha na validação em seu construtor deve lançar a exceção específica `InvalidDocumentException`.

### RF03: Carregamento Modular de Certificados
Deve existir uma abstração (`CertificateProvider`) capaz de carregar de forma segura e padronizada as chaves criptográficas (ICP-Brasil), independentemente da origem ser um arquivo físico (PKCS12 / A1) ou um Token/Smartcard (PKCS11 / A3).

### RF04: Geração de Contexto SSL Seguro (mTLS)
A biblioteca deve oferecer uma fábrica (`SslContextBuilder`) para criação de instâncias `javax.net.ssl.SSLContext` estritamente configuradas para uso com TLS 1.2 ou superior, embutindo o certificado do cliente (`CertificateProvider`) para autenticação mútua (mTLS) exigida pelos portais e-CAC e SPED.

### RF05: Assinatura de XML Padrão ICP-Brasil (XMLDSIG)
Deve ser disponibilizado um utilitário de assinatura XML (`XmlDsigSigner`) configurado rigidamente com os parâmetros exigidos pela RFB: Canonicalização *C14N*, Transformação *Enveloped*, Algoritmo de Assinatura *RSA-SHA256* e Digest *SHA-256*.

### RF06: Formatação Padronizada de JSON para a Receita (GovJsonFactory)
O módulo deve prover mecanismos de customização de conversores JSON (Jackson `ObjectMapper`) que cumpram com a padronização do Serpro: supressão estrita de campos nulos (`NON_NULL`), inibição de notação científica em campos financeiros numéricos (`BigDecimal`) e formatação de datas alinhada à ISO-8601, reduzindo assim falhas nas APIs REST.
