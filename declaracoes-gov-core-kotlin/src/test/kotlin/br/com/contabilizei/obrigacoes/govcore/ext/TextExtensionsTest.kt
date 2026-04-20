package br.com.contabilizei.obrigacoes.govcore.ext

import org.junit.Assert.*
import org.junit.Test

class TextExtensionsTest {

    // digitsOnly
    @Test fun `digitsOnly com null retorna vazio`() { assertEquals("", (null as String?).digitsOnly()) }
    @Test fun `digitsOnly remove mascara CNPJ`() {
        assertEquals("12345678000195", "12.345.678/0001-95".digitsOnly())
    }
    @Test fun `digitsOnly string sem mascara retorna a mesma`() {
        assertEquals("123", "123".digitsOnly())
    }

    // emptyIfNull
    @Test fun `emptyIfNull com null retorna vazio`() { assertEquals("", (null as String?).emptyIfNull()) }
    @Test fun `emptyIfNull com valor retorna o valor`() { assertEquals("abc", "abc".emptyIfNull()) }

    // normalizeToEmpty
    @Test fun `normalizeToEmpty com null retorna vazio`() { assertEquals("", (null as String?).normalizeToEmpty()) }
    @Test fun `normalizeToEmpty com valor retorna o valor`() { assertEquals("abc", "abc".normalizeToEmpty()) }

    // padLeftZeros
    @Test fun `padLeftZeros completa com zeros a esquerda`() { assertEquals("005", "5".padLeftZeros(3)) }
    @Test fun `padLeftZeros nao altera string maior que length`() { assertEquals("1234", "1234".padLeftZeros(3)) }

    // padLeft / padRight
    @Test fun `padLeft completa com caractere customizado`() { assertEquals("##abc", "abc".padLeft(5, '#')) }
    @Test fun `padRight completa com caractere customizado`() { assertEquals("abc##", "abc".padRight(5, '#')) }

    // truncate
    @Test fun `truncate corta string longa`() { assertEquals("abc", "abcdef".truncate(3)) }
    @Test fun `truncate nao altera string curta`() { assertEquals("ab", "ab".truncate(5)) }

    // removeMask
    @Test fun `removeMask remove pontos barras traco espaco`() {
        assertEquals("12345678000195", "12.345.678/0001-95".removeMask())
    }

    // toGovUpper
    @Test fun `toGovUpper remove acentos e converte para maiusculo`() {
        assertEquals("JOAO", "joão".toGovUpper())
    }
    @Test fun `toGovUpper compacta espacos multiplos`() {
        assertEquals("A B", "a  b".toGovUpper())
    }
}
