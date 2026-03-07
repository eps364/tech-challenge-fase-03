package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class LogoutUseCase {
    private final IdentityProviderGateway identityProviderGateway;

    public LogoutUseCase(IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = identityProviderGateway;
    }

    public void execute(String username) {
        identityProviderGateway.logout(username);
    }
}
