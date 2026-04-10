# Design Arquitetural: Declaracoes Gov Core

Este documento fundamenta as decisões arquiteturais específicas para o módulo `declaracoes-gov-core`.

---

## 1. Imutabilidade e Value Objects
No mundo fiscal, um CNPJ não é simplesmente uma "String de 14 caracteres". Ele é uma entidade que possui integridade (Dígito Verificador). 
A decisão central deste design é que **documentos fiscais só existem na memória se forem matematicamente válidos**.
- Classes como `Cnpj` e `Cpf` são *final* e seus construtores são restritos. O único modo de instanciá-los é pelo método `of(String)`.
- Se a String contiver lixo ("00.000.000/0001-XX") ou falhar no Módulo 11, o método falha abruptamente lançando `InvalidDocumentException` (Fail-Fast).
- **Benefício:** Nenhuma parte das outras bibliotecas (eSocial, Reinf) precisará checar se o CNPJ é válido. Se o objeto existe, ele é válido.

## 2. Padrão Strategy para Validadores
A validação de documentos fiscais brasileiros é mutável. O Governo Federal oficializou o CNPJ Alfanumérico.
- Em vez de emaranhar blocos gigantes de `if (isAlfanumerico) { ... } else { ... }`, adotamos o padrão **Strategy**.
- Criamos a interface genérica `InscricaoValidator`.
- Validadores específicos (`NumericCnpjValidator`, `AlphanumericCnpjValidator`) implementam essa interface isoladamente.
- **Benefício:** Total aderência ao princípio **Open/Closed** (Aberto para expansão, fechado para alteração). Novas regras de negócio podem ser adicionadas criando uma nova estratégia, sem mexer no Value Object `Cnpj`.

## 3. Isolamento Criptográfico (CertificateProvider)
O Java Security API (JCA) é complexo e dependente de provedores nativos (`SunPKCS11`). 
- **Decisão:** Esconder completamente as instâncias de `KeyStore` e inicializações de `Provider` da visão do ERP.
- A interface `CertificateProvider` expõe de forma fácil o `PrivateKey` e o `X509Certificate[]` necessários. 
- Implementações concretas como `Pkcs12Provider` (A1) e `Pkcs11Provider` (A3) cuidam das idiossincrasias (como carregar a `.dll` do token).
- **Benefício:** Se amanhã o governo introduzir certificados via Nuvem (BirdID, NeoID), basta criar um `CloudCertificateProvider` implementando a mesma interface, sem alterar o resto da biblioteca.

## 4. Factories Configuráveis (Json e Xml)
As rejeições mais frustrantes em Web Services governamentais são as de esquema ("Bad Request", "Schema Inválido"), frequentemente causadas pela injeção indesejada de campos `<null>` em JSON, notação científica em decimais, ou *namespaces* perdidos no XML.
- **Decisão:** Criar instâncias parametrizadas via `GovJsonFactory` (envolvendo o Jackson ObjectMapper) e utilitários estáticos no `XmlDocuments` que sanitizam essas bizarrices antes do envio.

## 5. Zero Annotations Invasivas (O Core Limpo)
- **Decisão:** O Core não terá dependência do artefato `javax.validation`. 
- **Por quê?** Obrigar o uso de `BeanValidator` incha as dependências do projeto e força uma arquitetura de validação que muitos sistemas legados que consumirão a API podem não querer usar. O Core foca em validação algorítmica imperativa pura, mantendo-se leve.
