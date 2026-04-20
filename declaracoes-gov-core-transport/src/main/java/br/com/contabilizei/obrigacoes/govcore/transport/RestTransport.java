package br.com.contabilizei.obrigacoes.govcore.transport;

import java.io.Closeable;

/**
 * SPI para transporte HTTP neutro.
 *
 * <p>Implementações DEVEM ser thread-safe e reutilizáveis.
 * Consumidores são responsáveis por chamar {@link #close()} quando terminarem.
 */
public interface RestTransport extends Closeable {

    /**
     * Executa a requisição fornecida e retorna uma resposta.
     *
     * @param request a requisição HTTP; nunca null
     * @return a resposta HTTP; nunca null
     * @throws TransportException se a requisição não puder ser executada
     */
    HttpResponse execute(HttpRequest request) throws TransportException;

    /**
     * Libera recursos subjacentes (pools de conexão, threads, etc.).
     */
    @Override
    void close();
}
