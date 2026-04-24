package br.com.contabilizei.obrigacoes.govcore.formater;

import br.com.contabilizei.obrigacoes.govcore.model.contract.Documento;

public interface DocumentoFormatter {

    String normalizar(Documento documento);

    String formatter(Documento documento);
}
