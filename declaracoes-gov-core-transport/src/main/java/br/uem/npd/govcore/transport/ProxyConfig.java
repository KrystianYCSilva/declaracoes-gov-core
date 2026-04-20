package br.uem.npd.govcore.transport;

import java.util.Objects;
import java.util.Optional;

/**
 * Configuração imutável de proxy HTTP com autenticação opcional.
 *
 * <p>Construída via {@link Builder}. As credenciais, quando fornecidas, devem ser
 * informadas em pares (usuário e senha).
 */
public final class ProxyConfig {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private ProxyConfig(Builder builder) {
        this.host = Objects.requireNonNull(builder.host, "host");
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port must be between 1 and 65535");
        }
        boolean hasUser = username != null && !username.isEmpty();
        boolean hasPass = password != null && !password.isEmpty();
        if (hasUser != hasPass) {
            throw new IllegalArgumentException("username and password must both be present or both absent");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public Optional<String> username() {
        return Optional.ofNullable(username);
    }

    public Optional<String> password() {
        return Optional.ofNullable(password);
    }

    public static final class Builder {
        private String host;
        private int port;
        private String username;
        private String password;

        private Builder() {}

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public ProxyConfig build() {
            return new ProxyConfig(this);
        }
    }
}
