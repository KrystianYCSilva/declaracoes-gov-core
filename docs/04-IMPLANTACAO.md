# Guia de Implantacao - v1.0.0

## 1. Pre-requisitos

| Componente | Versao minima | Observacao |
|------------|---------------|------------|
| JDK | 1.8 | baseline obrigatorio da biblioteca |
| Maven | 3.3.9+ | build e testes |
| Git | 2.x | tag da `v0.1.0` e branch `develop` |
| Memoria | 512 MB+ livres | build, testes e reports |

## 2. Estrategia de implantacao

### 2.1 Passo 0 - Fechamento da v0.1.0
1. validar o estado atual como baseline funcional;
2. registrar bugs e debitos conhecidos;
3. criar a tag `v0.1.0`;
4. abrir a branch `develop`;
5. ajustar a versao de trabalho para a linha `1.0.0-SNAPSHOT`.

### 2.2 Passo 1 - Workflow cascata
1. publicar requisitos;
2. publicar design;
3. publicar plano de testes;
4. publicar implantacao;
5. alinhar os documentos de raiz (`CODEX-PLAN-V1`, `PLANO-UNIFICADO-V1`, `PRE-DOCUMENTO-DE-REQUISITOS-V1`).

### 2.3 Passo 2 - Correcao do baseline
1. corrigir bugs factuais do baseline, como `Uf`;
2. explicitar a politica de cobertura;
3. registrar contratos e limites dos validadores atuais;
4. remover ambiguidade entre validacao oficial e heuristica.

### 2.4 Passo 3 - Reestruturacao para v1.0.0
1. converter o projeto para parent + BOM + modulos;
2. mover `domain`, `format`, `xml` e `crypto`;
3. estabilizar pacotes publicos;
4. atualizar guias de consumo.

## 3. Build e verificacao

### 3.1 Baseline atual
```bash
mvn clean test
```

### 3.2 Verificacao completa da linha de trabalho
```bash
mvn clean verify
```

### 3.3 Empacotamento
```bash
mvn clean package
```

## 4. Sequencia recomendada para implementacao

### Fase A - Documentacao e rastreabilidade
- consolidar os documentos atuais;
- revisar os contratos publicos existentes;
- publicar matriz de validadores.

### Fase B - Testes e confianca
- ampliar cobertura de `crypto` e `signature`;
- introduzir testes para `Modulo11`;
- eliminar sucesso artificial por exclusao ampla de cobertura.

### Fase C - Modularizacao
- criar parent/BOM;
- separar codigo atual nos modulos corretos;
- manter compatibilidade de Java 8 e contratos essenciais.

### Fase D - Expansao controlada
- formatacao e normalizacao;
- documentos adicionais com politica de confianca normativa;
- melhorias de XML e crypto.

## 5. Integracao futura nos projetos consumidores

### 5.1 Regra de adocao
- o consumidor deve importar apenas o modulo necessario;
- `domain` deve permanecer o artefato minimo;
- `xml` e `crypto` so devem ser puxados quando a aplicacao realmente precisar dessas capacidades.

### 5.2 Verificacao pos-migracao
- compila com Java 8;
- nao introduz framework inesperado;
- os validadores usados pelo consumidor possuem classificacao e fonte documental conhecidas;
- nao existe dependencia acidental de transporte dentro do core.

## 6. Riscos de implantacao

| Risco | Impacto | Mitigacao |
|-------|---------|-----------|
| confundir heuristica com regra oficial | alto | matriz de validadores e Javadoc explicitos |
| modularizacao quebrar imports consumidores | medio | migracao faseada e guias de adocao |
| manter cobertura artificialmente inflada | alto | revisar JaCoCo antes de fechar a v1.0.0 |
| aumentar escopo demais | alto | aplicar YAGNI e manter backlog fora do core |
