package br.uem.npd.govcore.model;

import br.uem.npd.govcore.table.TipoInscricao;
import java.io.Serializable;

/**
 * Interface polimórfica que permite tratar diferentes tipos de inscrição
 * (CNPJ, CPF, CNO, CAEPF) sob o mesmo contrato quando agem
 * como titulares de uma declaração governamental.
 */
public interface IdentificadorEmpregador extends Serializable {

    /**
     * Retorna o tipo governamental desta inscrição.
     */
    TipoInscricao getTipoInscricao();

    /**
     * Retorna os dígitos da inscrição sem nenhuma máscara de formatação.
     */
    String getUnformatted();

    /**
     * Retorna a inscrição com a máscara de formatação padrão do tipo.
     */
    String getFormatted();
}
