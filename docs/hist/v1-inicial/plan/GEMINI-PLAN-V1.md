# GEMINI-PLAN-V1.md - Planejamento Estratégico gov-core v1.0.0

## Visão Geral

A biblioteca `declaracoes-gov-core` não é apenas um utilitário para um projeto isolado. Ela foi concebida para ser o **coração e alicerce** de todas as integrações com os sistemas da Receita Federal e outros órgãos do governo brasileiro (eSocial, EFD-Reinf, DCTFWeb, SPED Fiscal/Contribuições, ECD, ECF, NF-e, NFS-e, etc.).

O objetivo principal da versão 1.0.0 é fornecer uma suíte completa, robusta e agnóstica de utilitários que abstraia a complexidade das regras estruturais e de formatação exigidas pelos leiautes governamentais, permitindo que as aplicações finais foquem puramente em regras de negócio e orquestração.

**Princípios Fundamentais (v1.0.0):**
1. **Não reinventar a roda:** Utilizar lógicas consagradas na comunidade (inspirado no Caelum Stella e Apache Commons), mas trazendo para o nosso contexto específico de declarações com alta performance.
2. **Agnosticidade:** Nenhuma dependência de frameworks pesados como Spring ou Jakarta EE. A biblioteca deve poder ser importada em qualquer ambiente Java (Android, Servidores Legados, Servidores Modernos, Lambdas, etc).
3. **Java 8+:** Manter a compatibilidade na base Java 8, dado o vasto ecossistema de sistemas fiscais legados ainda em operação no Brasil.
4. **Modularização Interna (YAGNI):** Seguir um padrão de pacotes bem definidos ou submódulos no estilo Apache Commons, agrupando funcionalidades coesas e mantendo o design limpo.

---

## Estrutura Modular Planejada

A biblioteca deverá ser reorganizada nos seguintes domínios de pacote (ou submódulos Maven, a depender da complexidade futura, mas inicialmente pacotes):

### 1. `br.uem.npd.govcore.documentos` (Validação e Modelagem)
Classes voltadas estritamente a documentos de identificação brasileiros, seus validadores, formatadores (máscaras) e Value Objects.
*   **Implementados:** `Cpf`, `Cnpj` (Numérico e Alfanumérico), `Nis`.
*   **A Implementar:** `InscricaoEstadual` (com regras por UF), `TituloEleitor`, `PisPasep`, `Cei`, `Cno`, `Caepf`, `Renavam` (opcional, se útil), Validadores para cada um, Formatadores de Máscara (`###.###.###-##`).

### 2. `br.uem.npd.govcore.tempo` (Datas, Períodos e Feriados)
O contexto brasileiro e o SPED possuem regras muito específicas sobre datas, competências e vigências.
*   **Implementados:** `PeriodoApuracao`, `Vigencia`, `XmlDates`.
*   **A Implementar:** Cálculos de dias úteis baseados no calendário da FEBRABAN/Feriados Nacionais (extremamente útil para cálculo de vencimento de tributos), formatadores flexíveis (AAAAMM, MM/AAAA, MMAAAA).

### 3. `br.uem.npd.govcore.texto` (Manipulação de Strings para SPED/TXT)
Muitos sistemas, como SPED ECD, ECF e Fiscal, dependem da geração posicional e tratativa pesada de strings.
*   **A Implementar:** `StringUtils` específico para remover acentuação (substituição por caracteres base, exigência de muitos webservices), `LPad`/`RPad` (para geração de TXT), formatadores numéricos (`BigDecimal` para string sem ponto decimal como `0000150` para representar R$ 1,50).

### 4. `br.uem.npd.govcore.crypto` e `br.uem.npd.govcore.signature` (Segurança ICP-Brasil)
O núcleo para comunicação com WebServices e assinatura de arquivos.
*   **Implementados:** `XmlSigner`, `CertificateProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder`.
*   **A Melhorar:** Ajustes de performance para reuso de chaves A3 sem lock de driver, maior flexibilidade no `SslContextBuilder` (TLS 1.2 e TLS 1.3 force), suporte mais claro a HSMs na nuvem, validação de cadeia de certificados ICP-Brasil (verificar revogação/CRL).

### 5. `br.uem.npd.govcore.tabelas` (Domínios Fundamentais Constantes)
Tabelas que quase não mudam e estruturam praticamente toda declaração.
*   **Implementados:** `Uf`, `TipoAmbiente`, `TipoInscricao`.
*   **A Implementar:** `CodigoMunicipio` (Base do IBGE completa), Tabelas básicas padronizadas transversalmente. (Nota: domínios voláteis como CST, NCM e CFOP devem ser mantidos fora do core ou num módulo separado para não exigir versionamento constante do core por conta de legislação).

---

## Roadmap de Desenvolvimento v1.0.0

### Passo 0: Preparação do Terreno (Concluído)
- [x] Avaliar situação atual e fechar release v0.1.0 (`pom.xml` atualizado).
- [x] Criar tag no Git `v0.1.0`.
- [x] Criar branch `develop` para centralizar as inovações da v1.0.0.

### Passo 1: Implementação - Módulo Documentos (`documentos`)
- [ ] Incorporar lógicas consagradas de cálculo de dígito verificador para IE de todos os estados (inspirado no Caelum Stella/br-validator).
- [ ] Adicionar VO e validadores para PIS/PASEP, CEI, CNO, CAEPF.
- [ ] Garantir 100% de cobertura nos algoritmos de módulo 11.

### Passo 2: Implementação - Módulos Texto e Tempo (`texto`, `tempo`)
- [ ] Criar `GovStringUtils` para remoção severa de acentos e escapes XML/TXT seguros.
- [ ] Criar `GovNumberUtils` para formatação segura de moedas e pesos no padrão SEFAZ/SPED.
- [ ] Refatorar e ampliar `PeriodoApuracao` com utilitários de rolagem mensal, adição/subtração de competências.

### Passo 3: Aprimoramento e Validação da Criptografia (`crypto`, `signature`)
- [ ] Melhorar a estrutura de erros para problemas comuns em A3 (token desconectado, senha bloqueada).
- [ ] Implementar verificação opcional da cadeia ICP-Brasil.

### Passo 4: Qualidade e CI/CD
- [ ] Rodar e fixar todos os alertas do Checkstyle e PMD.
- [ ] Cobertura de teste superior a 90% (obrigatória para os algoritmos de validação).
- [ ] Revisão da Javadoc e geração de documentação estática (Markdown/HTML) para os desenvolvedores das aplicações que consumirão a biblioteca.

### Passo 5: Release v1.0.0
- [ ] Teste de integração ponta-a-ponta consumindo os novos módulos em projetos locais (eSocial e EFD-Reinf).
- [ ] Merge `develop` em `master`.
- [ ] Publicação da Release v1.0.0.

---
*Gerado por Gemini CLI em planejamento colaborativo baseando-se em documentações da Receita Federal e utilitários padrão da comunidade open-source em Java.*