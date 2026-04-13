# Changelog

Todas as mudanças relevantes deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2026-04-13

Release inicial do `declaracoes-gov-core` — biblioteca central, agnóstica de framework,
para validação, serialização e segurança de declarações governamentais brasileiras.

### Adicionado

#### core-domain
- Validador de **CPF** com dígito verificador e formatação.
- Validador de **CNPJ** com dígito verificador e formatação.
- Validador de **NIS** (PIS/PASEP/NIT) com dígito verificador.
- Validador de **CAEPF** (Cadastro de Atividade Econômica da Pessoa Física).
- Validador de **CNO** (Cadastro Nacional de Obras).
- Validador de **CEI** (Cadastro Específico do INSS — legado).
- Value object `PeriodoApuracao` para competências mensais e anuais.
- Value object `Vigencia` para intervalos temporais com validação de sobreposição.

#### core-format
- `DelimitedParser` e `DelimitedSerializer` para arquivos com separador (pipe, ponto-e-vírgula, etc.).
- `FixedLengthParser` e `FixedLengthSerializer` para leiautes posicionais de largura fixa.

#### core-xml
- Assinatura digital XML (XMLDSig / Enveloped Signature) com suporte a certificados A1 e A3.
- Proteção contra ataques XXE (XML External Entity) habilitada por padrão.

#### core-crypto
- Carregamento de certificados digitais a partir de keystores **PKCS#12** (.p12 / .pfx).
- Suporte a tokens criptográficos **PKCS#11** (smart cards / HSMs).
- Utilitários para carregamento e inspeção de certificados X.509.

#### core-bom
- BOM (Bill of Materials) para gestão centralizada de versões dos módulos core.

### Destaques
- Cobertura de testes ≥ 90 % em linhas e branches.
- Compatível com **Java 8+**.
- Totalmente agnóstico de framework (sem dependências de Spring, Jakarta EE, etc.).
