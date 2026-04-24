package br.com.contabilizei.obrigacoes.govcore.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testes unitários para validar a integridade das expressões regulares em GovRegexPatterns.
 */
public class GovRegexPatternsTest {

    @Test
    public void testEmail() {
        assertTrue(GovRegexPatterns.EMAIL.matcher("teste@exemplo.com").matches());
        assertTrue(GovRegexPatterns.EMAIL.matcher("usuario.sobrenome@empresa.com.br").matches());
        assertFalse(GovRegexPatterns.EMAIL.matcher("invalido").matches());
        assertFalse(GovRegexPatterns.EMAIL.matcher("usuario@").matches());
        assertFalse(GovRegexPatterns.EMAIL.matcher("@empresa.com").matches());
    }

    @Test
    public void testCep() {
        assertTrue(GovRegexPatterns.CEP.matcher("12345678").matches());
        assertFalse(GovRegexPatterns.CEP.matcher("1234567").matches());
        assertFalse(GovRegexPatterns.CEP.matcher("123456789").matches());
        assertFalse(GovRegexPatterns.CEP.matcher("12345-678").matches());
        assertFalse(GovRegexPatterns.CEP.matcher("ABCDEFGH").matches());
    }

    @Test
    public void testTelefoneBr() {
        assertTrue(GovRegexPatterns.TELEFONE_BR.matcher("11988887777").matches());
        assertTrue(GovRegexPatterns.TELEFONE_BR.matcher("1133334444").matches());
        assertFalse(GovRegexPatterns.TELEFONE_BR.matcher("119888877").matches()); // 9 dígitos (inválido)
        assertFalse(GovRegexPatterns.TELEFONE_BR.matcher("119888877777").matches());
        assertFalse(GovRegexPatterns.TELEFONE_BR.matcher("(11)98888-7777").matches());
    }

    @Test
    public void testCnpj() {
        assertTrue(GovRegexPatterns.CNPJ.matcher("12345678000199").matches());
        assertTrue(GovRegexPatterns.CNPJ.matcher("ABCDEFGHIJKLMN").matches()); // Alfanumérico RF 2026
        assertTrue(GovRegexPatterns.CNPJ.matcher("12345678A00199").matches());
        assertFalse(GovRegexPatterns.CNPJ.matcher("1234567800019").matches());
        assertFalse(GovRegexPatterns.CNPJ.matcher("123456780001999").matches());
        assertFalse(GovRegexPatterns.CNPJ.matcher("12.345.678/0001-99").matches());
    }

    @Test
    public void testCpf() {
        assertTrue(GovRegexPatterns.CPF.matcher("12345678901").matches());
        assertFalse(GovRegexPatterns.CPF.matcher("1234567890").matches());
        assertFalse(GovRegexPatterns.CPF.matcher("123456789012").matches());
        assertFalse(GovRegexPatterns.CPF.matcher("123.456.789-01").matches());
    }

    @Test
    public void testNisPisPasep() {
        assertTrue(GovRegexPatterns.NIS_PIS_PASEP.matcher("12345678901").matches());
        assertFalse(GovRegexPatterns.NIS_PIS_PASEP.matcher("1234567890").matches());
        assertFalse(GovRegexPatterns.NIS_PIS_PASEP.matcher("123456789012").matches());
    }

    @Test
    public void testCeiCnoCaepf() {
        assertTrue(GovRegexPatterns.CEI_CNO_CAEPF.matcher("123456789012").matches());
        assertTrue(GovRegexPatterns.CEI_CNO_CAEPF.matcher("1234567890123").matches());
        assertTrue(GovRegexPatterns.CEI_CNO_CAEPF.matcher("12345678901234").matches());
        assertFalse(GovRegexPatterns.CEI_CNO_CAEPF.matcher("12345678901").matches());
        assertFalse(GovRegexPatterns.CEI_CNO_CAEPF.matcher("123456789012345").matches());
    }

    @Test
    public void testPassaporte() {
        assertTrue(GovRegexPatterns.PASSAPORTE.matcher("ABC12345").matches());
        assertTrue(GovRegexPatterns.PASSAPORTE.matcher("12345678901234567890").matches());
        assertFalse(GovRegexPatterns.PASSAPORTE.matcher("").matches());
        assertFalse(GovRegexPatterns.PASSAPORTE.matcher("123456789012345678901").matches());
        assertFalse(GovRegexPatterns.PASSAPORTE.matcher("PAS-123").matches());
    }

    @Test
    public void testTituloEleitor() {
        assertTrue(GovRegexPatterns.TITULO_ELEITOR.matcher("123456789012").matches());
        assertFalse(GovRegexPatterns.TITULO_ELEITOR.matcher("12345678901").matches());
        assertFalse(GovRegexPatterns.TITULO_ELEITOR.matcher("1234567890123").matches());
    }

    @Test
    public void testCnh() {
        assertTrue(GovRegexPatterns.CNH.matcher("12345678901").matches());
        assertFalse(GovRegexPatterns.CNH.matcher("1234567890").matches());
        assertFalse(GovRegexPatterns.CNH.matcher("123456789012").matches());
    }

    @Test
    public void testRg() {
        assertTrue(GovRegexPatterns.RG.matcher("12.345.678-9").matches());
        assertTrue(GovRegexPatterns.RG.matcher("123456789").matches());
        assertTrue(GovRegexPatterns.RG.matcher("MG-12.345.678").matches());
        assertFalse(GovRegexPatterns.RG.matcher("1").matches());
        assertFalse(GovRegexPatterns.RG.matcher("MuitoLongoRgComMaisDeVinteCaracteres").matches());
    }

    @Test
    public void testChaveAcesso() {
        assertTrue(GovRegexPatterns.CHAVE_ACESSO.matcher("35170112345678000199550010000000011234567890").matches());
        assertFalse(GovRegexPatterns.CHAVE_ACESSO.matcher("3517011234567800019955001000000001123456789").matches());
        assertFalse(GovRegexPatterns.CHAVE_ACESSO.matcher("351701123456780001995500100000000112345678901").matches());
    }

    @Test
    public void testCfop() {
        assertTrue(GovRegexPatterns.CFOP.matcher("1101").matches());
        assertTrue(GovRegexPatterns.CFOP.matcher("7949").matches());
        assertFalse(GovRegexPatterns.CFOP.matcher("0101").matches());
        assertFalse(GovRegexPatterns.CFOP.matcher("8101").matches());
        assertFalse(GovRegexPatterns.CFOP.matcher("110").matches());
        assertFalse(GovRegexPatterns.CFOP.matcher("11011").matches());
    }

    @Test
    public void testCst() {
        assertTrue(GovRegexPatterns.CST.matcher("00").matches());
        assertTrue(GovRegexPatterns.CST.matcher("010").matches());
        assertTrue(GovRegexPatterns.CST.matcher("101").matches());
        assertFalse(GovRegexPatterns.CST.matcher("1").matches());
        assertFalse(GovRegexPatterns.CST.matcher("1010").matches());
    }

    @Test
    public void testCnae() {
        assertTrue(GovRegexPatterns.CNAE.matcher("1234567").matches());
        assertFalse(GovRegexPatterns.CNAE.matcher("123456").matches());
        assertFalse(GovRegexPatterns.CNAE.matcher("12345678").matches());
    }

    @Test
    public void testNcm() {
        assertTrue(GovRegexPatterns.NCM.matcher("12345678").matches());
        assertFalse(GovRegexPatterns.NCM.matcher("1234567").matches());
        assertFalse(GovRegexPatterns.NCM.matcher("123456789").matches());
    }

    @Test
    public void testCest() {
        assertTrue(GovRegexPatterns.CEST.matcher("1234567").matches());
        assertFalse(GovRegexPatterns.CEST.matcher("123456").matches());
        assertFalse(GovRegexPatterns.CEST.matcher("12345678").matches());
    }

    @Test
    public void testCbo() {
        assertTrue(GovRegexPatterns.CBO.matcher("123456").matches());
        assertFalse(GovRegexPatterns.CBO.matcher("12345").matches());
        assertFalse(GovRegexPatterns.CBO.matcher("1234567").matches());
    }

    @Test
    public void testMatriculaEsocial() {
        assertTrue(GovRegexPatterns.MATRICULA_ESOCIAL.matcher("MAT-123_456").matches());
        assertTrue(GovRegexPatterns.MATRICULA_ESOCIAL.matcher("M").matches());
        assertFalse(GovRegexPatterns.MATRICULA_ESOCIAL.matcher("").matches());
        assertFalse(GovRegexPatterns.MATRICULA_ESOCIAL.matcher("MAT 123").matches());
    }

    @Test
    public void testNumeroReciboGov() {
        assertTrue(GovRegexPatterns.NUMERO_RECIBO_GOV.matcher("1.1.12345678901234567890").matches());
        assertTrue(GovRegexPatterns.NUMERO_RECIBO_GOV.matcher("1.9.09876543210987654321").matches());
        assertFalse(GovRegexPatterns.NUMERO_RECIBO_GOV.matcher("1.1.1234567890123456789").matches());
        assertFalse(GovRegexPatterns.NUMERO_RECIBO_GOV.matcher("2.1.12345678901234567890").matches());
        assertFalse(GovRegexPatterns.NUMERO_RECIBO_GOV.matcher("1.A.12345678901234567890").matches());
    }

    @Test
    public void testProcessoJudicialNup() {
        assertTrue(GovRegexPatterns.PROCESSO_JUDICIAL_NUP.matcher("12345678901234567").matches());
        assertTrue(GovRegexPatterns.PROCESSO_JUDICIAL_NUP.matcher("12345678901234567890").matches());
        assertFalse(GovRegexPatterns.PROCESSO_JUDICIAL_NUP.matcher("1234567890123456").matches());
        assertFalse(GovRegexPatterns.PROCESSO_JUDICIAL_NUP.matcher("123456789012345678").matches());
        assertFalse(GovRegexPatterns.PROCESSO_JUDICIAL_NUP.matcher("1234567890123456789").matches());
    }

    @Test
    public void testXmlInvalidChars() {
        // Caracteres válidos
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher("\t").find());
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher("\n").find());
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher("\r").find());
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher(" ").find());
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher("A").find());
        assertFalse(GovRegexPatterns.XML_INVALID_CHARS.matcher("\uD800\uDC00").find()); // Supplementary char

        // Caracteres inválidos
        assertTrue(GovRegexPatterns.XML_INVALID_CHARS.matcher("\u0000").find());
        assertTrue(GovRegexPatterns.XML_INVALID_CHARS.matcher("\u0007").find());
        assertTrue(GovRegexPatterns.XML_INVALID_CHARS.matcher("\u000B").find());
        assertTrue(GovRegexPatterns.XML_INVALID_CHARS.matcher("\u000E").find());
        assertTrue(GovRegexPatterns.XML_INVALID_CHARS.matcher("\uFFFF").find());
    }

    @Test
    public void testOnlyDigits() {
        assertTrue(GovRegexPatterns.ONLY_DIGITS.matcher("123456").matches());
        assertFalse(GovRegexPatterns.ONLY_DIGITS.matcher("123A56").matches());
        assertFalse(GovRegexPatterns.ONLY_DIGITS.matcher("").matches());
        assertFalse(GovRegexPatterns.ONLY_DIGITS.matcher(" ").matches());
    }
}
