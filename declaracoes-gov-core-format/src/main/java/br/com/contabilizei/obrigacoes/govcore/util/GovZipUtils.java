package br.com.contabilizei.obrigacoes.govcore.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utilitários de compressão GZIP com codificação Base64, consolidando
 * implementações duplicadas no ecossistema declarações (ecd, ecf, efd, back-core).
 */
public final class GovZipUtils {

    private GovZipUtils() {
        // Impede instanciação
    }

    /**
     * Comprime o conteúdo em UTF-8 com GZIP e retorna o resultado codificado em Base64.
     *
     * @param content conteúdo a comprimir; não pode ser {@code null}
     * @return string Base64 do conteúdo comprimido
     * @throws IllegalArgumentException se {@code content} for {@code null}
     */
    public static String compressAndEncodeBase64(String content) {
        if (content == null) {
            throw new IllegalArgumentException("content não pode ser nulo");
        }
        byte[] input = content.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
        try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(input);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao comprimir conteúdo com GZIP", e);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * Decodifica Base64 e descomprime o resultado com GZIP, retornando os bytes originais.
     *
     * @param compressed bytes Base64 que representam dados comprimidos em GZIP; não pode ser {@code null}
     * @return bytes descomprimidos
     * @throws IllegalArgumentException se {@code compressed} for {@code null}
     */
    public static byte[] decompress(byte[] compressed) {
        if (compressed == null) {
            throw new IllegalArgumentException("compressed não pode ser nulo");
        }
        byte[] gzipBytes;
        try {
            gzipBytes = Base64.getDecoder().decode(compressed);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Entrada inválida: Base64 corrompido ou não-Base64", e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(gzipBytes))) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao descomprimir bytes GZIP", e);
        }
        return baos.toByteArray();
    }

    /**
     * Serializa o objeto com {@link ObjectOutputStream}, comprime com GZIP e
     * retorna codificado em Base64.
     *
     * @param obj objeto a serializar; não pode ser {@code null} e deve implementar {@link Serializable}
     * @return string Base64 do objeto serializado e comprimido
     * @throws IllegalArgumentException se {@code obj} for {@code null} ou não implementar {@link Serializable}
     */
    public static String zipToString(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("obj não pode ser nulo");
        }
        if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException(
                    "obj deve implementar java.io.Serializable: " + obj.getClass().getName());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(baos);
             ObjectOutputStream oos = new ObjectOutputStream(gzip)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao serializar e comprimir objeto", e);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
