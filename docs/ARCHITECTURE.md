# Arquitetura: Declaracoes Gov Core

A biblioteca `declaracoes-gov-core` foi concebida para atuar como o "anel isolante" entre a lógica de negócios de ERPs consumindo o ecossistema brasileiro de tributos (SPED e Integra Contador) e as burocracias de criptografia e validação da Receita Federal.

## 1. Diagrama de Módulos (Macro)

```mermaid
graph TD
    %% Módulos Core
    subgraph Declaracoes_Gov_Core [declaracoes-gov-core]
        DOM[Domain / Model<br/>Value Objects imutáveis]
        VAL[Validators<br/>Strategy Pattern]
        SEC[Security / Crypto<br/>mTLS, KeyStore]
        SIG[Signature<br/>XMLDSIG RSA-SHA256]
        UTL[Util<br/>Parsers, JSON Factory]
        EXC[Exception<br/>Hierarquia Base]
    end

    %% Consumidores Internos do Ecossistema
    subgraph Ecossistema_Dependente [Consumidores do Core]
        LAY_ES[esocial-leiautes]
        TX_ES[esocial-transmissor]
        LAY_REINF[reinf-leiautes]
        TX_REINF[reinf-transmissor]
        TX_SERPRO[serpro-transmissor]
    end

    %% Relacionamentos
    DOM -->|Depende| VAL
    SIG -->|Usa| SEC
    SIG -->|Usa| UTL

    %% Relacionamentos Externos
    LAY_ES -.->|Valida Documentos| DOM
    LAY_REINF -.->|Valida Documentos| DOM
    TX_ES -.->|SslContext / Assinatura| SEC
    TX_REINF -.->|Assinatura| SIG
    TX_SERPRO -.->|SslContext (OAuth2/mTLS)| SEC
```

## 2. Pacotes Principais

### `br.uem.npd.govcore.model` e `br.uem.npd.govcore.validator`
As classes em `model` (`Cnpj`, `Cpf`) atuam como o contrato público. O cliente instancia `Cnpj.of("123")` e se a string for inválida, ele falha imediatamente. O pacote `validator` embute as regras de negócio voláteis (Ex: `CnpjValidator` com a verificação de formato Módulo 11 e suporte ao CNPJ Alfanumérico via padrão *Strategy*).

### `br.uem.npd.govcore.crypto`
Envolve a complexidade da JCA (Java Cryptography Architecture).
Interfaces como `CertificateProvider` são essenciais para que os `*-transmissores` possam lidar de forma agnóstica com a obtenção do certificado digital do cliente, abstraindo o acesso aos arquivos físicos `.p12` ou a tokens A3.

### `br.uem.npd.govcore.signature`
Isola o motor *Apache Santuario* para prover Assinatura Digital do tipo *XMLDSIG Enveloped*. O eSocial e EFD-Reinf enviam eventos síncronos e assíncronos que **obrigatoriamente** devem passar por este pacote.

### `br.uem.npd.govcore.util` e `br.uem.npd.govcore.table`
Provê os enums de metadados como Ambiente de Produção/Restrita (`TipoAmbiente`) e utilitários que formatam Data/Hora e JSON/XML mitigando os notórios erros de parse (`MS0030`, `400 Bad Request`) comuns ao tentar enviar dados formatados incorretamente (ex: nulls ou notação científica) à Receita Federal.
