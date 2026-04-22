package br.com.contabilizei.obrigacoes.govcore.util;

/**
 * Utilitários para detecção de tipo de arquivo por magic numbers e proteção
 * contra uploads de arquivos maliciosos.
 */
public final class GovFileUtils {

    private GovFileUtils() {
        // Impede instanciação
    }

    private static final byte[] MAGIC_PDF  = {0x25, 0x50, 0x44, 0x46};                       // %PDF
    private static final byte[] MAGIC_ZIP  = {0x50, 0x4B, 0x03, 0x04};                       // PK
    private static final byte[] MAGIC_PNG  = {(byte) 0x89, 0x50, 0x4E, 0x47};               // .PNG
    private static final byte[] MAGIC_JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};        // JPEG SOI
    private static final byte[] MAGIC_XML  = {0x3C, 0x3F, 0x78, 0x6D, 0x6C};                // <?xml

    /**
     * Extrai os primeiros até 8 bytes do arquivo (magic numbers).
     *
     * @param fileBytes bytes do arquivo; não pode ser {@code null}
     * @return array com até 8 bytes iniciais
     * @throws IllegalArgumentException se {@code fileBytes} for nulo
     */
    public static byte[] getMagicNumbers(byte[] fileBytes) {
        if (fileBytes == null) {
            throw new IllegalArgumentException("fileBytes não pode ser nulo");
        }
        int len = Math.min(fileBytes.length, 8);
        byte[] result = new byte[len];
        System.arraycopy(fileBytes, 0, result, 0, len);
        return result;
    }

    /**
     * Detecta o MIME type pelos magic numbers.
     * Retorna {@code "application/octet-stream"} se não reconhecido ou
     * se o array tiver menos de 3 bytes.
     *
     * @param fileBytes bytes do arquivo; não pode ser {@code null}
     * @return string com o MIME type detectado
     * @throws IllegalArgumentException se {@code fileBytes} for nulo
     */
    public static String getMimeType(byte[] fileBytes) {
        if (fileBytes == null) {
            throw new IllegalArgumentException("fileBytes não pode ser nulo");
        }
        if (fileBytes.length < 3) {
            return "application/octet-stream";
        }
        if (startsWith(fileBytes, MAGIC_PDF)) {
            return "application/pdf";
        }
        if (startsWith(fileBytes, MAGIC_ZIP)) {
            return "application/zip";
        }
        if (startsWith(fileBytes, MAGIC_PNG)) {
            return "image/png";
        }
        if (startsWith(fileBytes, MAGIC_JPEG)) {
            return "image/jpeg";
        }
        if (fileBytes.length >= 5 && startsWith(fileBytes, MAGIC_XML)) {
            return "application/xml";
        }
        return "application/octet-stream";
    }

    /**
     * Lança {@link IllegalArgumentException} se o arquivo for considerado malicioso.
     * Detecta:
     * <ul>
     *   <li>Executável Windows — cabeçalho MZ ({@code 4D 5A})</li>
     *   <li>Executável Linux/Unix — cabeçalho ELF ({@code 7F 45 4C 46})</li>
     *   <li>Executável macOS — cabeçalho Mach-O 32-bit ({@code FE ED FA CE}) e 64-bit ({@code FE ED FA CF})</li>
     *   <li>Script executável — shebang ({@code 23 21} = {@code #!})</li>
     * </ul>
     *
     * @param fileBytes bytes do arquivo; não pode ser {@code null}
     * @throws IllegalArgumentException se {@code fileBytes} for nulo ou o arquivo for executável
     */
    public static void throwIfMalicious(byte[] fileBytes) {
        if (fileBytes == null) {
            throw new IllegalArgumentException("fileBytes não pode ser nulo");
        }
        // Cabeçalho MZ — executável Windows (EXE/DLL)
        if (fileBytes.length >= 2 && fileBytes[0] == 0x4D && fileBytes[1] == 0x5A) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido: executável Windows detectado");
        }
        // Cabeçalho ELF — executável Linux/Unix
        if (fileBytes.length >= 4
                && fileBytes[0] == 0x7F
                && fileBytes[1] == 0x45
                && fileBytes[2] == 0x4C
                && fileBytes[3] == 0x46) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido: executável Linux detectado");
        }
        // Cabeçalho Mach-O — executável macOS (big-endian 32-bit e 64-bit)
        if (fileBytes.length >= 4
                && (byte) fileBytes[0] == (byte) 0xFE
                && (byte) fileBytes[1] == (byte) 0xED
                && (byte) fileBytes[2] == (byte) 0xFA
                && ((byte) fileBytes[3] == (byte) 0xCE || (byte) fileBytes[3] == (byte) 0xCF)) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido: executável macOS detectado");
        }
        // Shebang — script executável Unix (#!)
        if (fileBytes.length >= 2
                && fileBytes[0] == 0x23
                && fileBytes[1] == 0x21) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido: script executável detectado");
        }
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}
