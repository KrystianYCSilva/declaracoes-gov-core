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

    public static boolean isCpfStructureValid(String cpf) {
        return hasDigitsCount(cpf, 11);
    }

    public static boolean isNisStructureValid(String nis) {
        return hasDigitsCount(nis, 11);
    }

    public static boolean isCpfProvisionallyValid(String cpf) {
        return CPF_VALIDATOR.isValid(cpf);
    }

    public static boolean isNisProvisionallyValid(String nis) {
        return NIS_VALIDATOR.isValid(nis);
    }

    /**
     * @deprecated Use {@link #isCpfStructureValid(String)} para o contrato padrão
     *             do core ou {@link #isCpfProvisionallyValid(String)} para o algoritmo
     *             legado explicitamente provisório.
     */
    @Deprecated
    public static boolean isCpfValid(String cpf) {
        return isCpfProvisionallyValid(cpf);
    }

    /**
     * @deprecated Use {@link #isNisStructureValid(String)} para o contrato padrão
     *             do core ou {@link #isNisProvisionallyValid(String)} para o algoritmo
     *             legado explicitamente provisório.
     */
    @Deprecated
    public static boolean isNisValid(String nis) {
        return isNisProvisionallyValid(nis);
    }

    public static boolean isCaepfStructureValid(String numero) {
        return hasDigitsCount(numero, 14);
    }

    public static boolean isCnoStructureValid(String numero) {
        return hasDigitsCount(numero, 12);
    }

    public static boolean isCeiStructureValid(String numero) {
        return hasDigitsCount(numero, 12);
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
            case CAEPF:
            case CNO:
            case CEI:
            case CPF:
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

        switch (tipo) {
            case CPF:
                return isCpfStructureValid(numero);
            case CAEPF:
                return isCaepfStructureValid(numero);
            case CNO:
                return isCnoStructureValid(numero);
            case CEI:
                return isCeiStructureValid(numero);
            default:
                return isInscricaoValid(tipo, numero);
        }
    }

    private static boolean hasDigitsCount(String numero, int expectedLength) {
        if (numero == null) {
            return false;
        }
        return numero.replaceAll("[^0-9]", "").length() == expectedLength;
    }
}
