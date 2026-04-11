package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;

import java.util.Arrays;
import java.util.List;

/**
 * Facade utilitario para acesso direto e stateless aos validadores do core.
 * <p>
 * Validacao "forte" fica restrita aos tipos com regra consolidada no core.
 * Tipos sem algoritmo oficial mapeado devem usar as checagens estruturais
 * explicitas desta classe.
 */
public final class GovValidators {

    private static final List<DocumentValidator> CNPJ_CHAIN = Arrays.asList(
        new NumericCnpjValidator(),
        new AlphanumericCnpjValidator()
    );

    private static final DocumentValidator CPF_VALIDATOR = new CpfValidator();
    private static final DocumentValidator NIS_VALIDATOR = new NisValidator();

    private GovValidators() {
        // Prevents instantiation
    }

    public static boolean isCnpjValid(String cnpj) {
        return CNPJ_CHAIN.stream().anyMatch(validator -> validator.isValid(cnpj));
    }

    public static String stripCnpjIfValid(String cnpj) {
        return CNPJ_CHAIN.stream()
            .filter(v -> v.isValid(cnpj))
            .findFirst()
            .map(v -> v.strip(cnpj))
            .orElse(null);
    }

    public static boolean isCpfValid(String cpf) {
        return CPF_VALIDATOR.isValid(cpf);
    }

    public static boolean isNisValid(String nis) {
        return NIS_VALIDATOR.isValid(nis);
    }

    /**
     * Validacao forte apenas para tipos suportados por algoritmo oficial no core.
     */
    public static boolean isInscricaoValid(TipoInscricao tipo, String numero) {
        if (tipo == null || numero == null) {
            return false;
        }

        switch (tipo) {
            case CNPJ:
            case CGC:
                return isCnpjValid(numero);
            case CPF:
                return isCpfValid(numero);
            case CAEPF:
            case CNO:
            case CEI:
            default:
                return false;
        }
    }

    /**
     * Checagem estrutural para tipos sem algoritmo oficial consolidado no core.
     */
    public static boolean isInscricaoStructureValid(TipoInscricao tipo, String numero) {
        if (tipo == null || numero == null) {
            return false;
        }

        String digits = numero.replaceAll("[^0-9]", "");
        switch (tipo) {
            case CAEPF:
                return digits.length() == 14;
            case CNO:
            case CEI:
                return digits.length() == 12;
            default:
                return isInscricaoValid(tipo, numero);
        }
    }
}
