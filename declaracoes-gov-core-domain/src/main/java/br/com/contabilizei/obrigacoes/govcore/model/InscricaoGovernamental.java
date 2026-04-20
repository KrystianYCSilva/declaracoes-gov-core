package br.com.contabilizei.obrigacoes.govcore.model;

import br.com.contabilizei.obrigacoes.govcore.table.TipoInscricao;

import java.io.Serializable;

/**
 * Contrato neutro para inscricoes governamentais brasileiras reutilizaveis no
 * ecossistema fiscal e contabil.
 */
public interface InscricaoGovernamental extends Serializable {

    TipoInscricao getTipoInscricao();

    String getUnformatted();

    String getFormatted();
}
