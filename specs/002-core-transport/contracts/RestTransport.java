package br.uem.npd.govcore.transport;

import java.io.Closeable;

/**
 * SPI for neutral HTTP transport.
 *
 * <p>Implementations MUST be thread-safe and reusable.
 * Consumers are responsible for calling {@link #close()} when done.
 */
public interface RestTransport extends Closeable {

    /**
     * Execute the given request and return a response.
     *
     * @param request the HTTP request; never null
     * @return the HTTP response; never null
     * @throws TransportException if the request cannot be executed
     */
    HttpResponse execute(HttpRequest request) throws TransportException;

    /**
     * Release underlying resources (connection pools, threads, etc.).
     */
    @Override
    void close();
}
