package br.com.fiap.authservice.core.gateway;

public interface TokenBlacklistGateway {
    void blacklist(String jti, long ttlSeconds);
}
