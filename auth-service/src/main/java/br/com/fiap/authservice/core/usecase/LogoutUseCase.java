package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;

public class LogoutUseCase {
    private final IdentityProviderGateway identityProviderGateway;
    private final TokenBlacklistGateway tokenBlacklistGateway;

    public LogoutUseCase(IdentityProviderGateway identityProviderGateway,
                         TokenBlacklistGateway tokenBlacklistGateway) {
        this.identityProviderGateway = identityProviderGateway;
        this.tokenBlacklistGateway = tokenBlacklistGateway;
    }

    public void execute(String userId, String jti, long ttlSeconds) {
        identityProviderGateway.logout(userId);
        tokenBlacklistGateway.blacklist(jti, ttlSeconds);
    }
}
