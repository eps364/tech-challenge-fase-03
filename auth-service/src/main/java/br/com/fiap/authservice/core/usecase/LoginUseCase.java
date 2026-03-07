package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class LoginUseCase {
    private final IdentityProviderGateway identityProviderGateway;

    public LoginUseCase(IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = identityProviderGateway;
    }

    public String execute(String username, String password) {
        return identityProviderGateway.login(username, password);
    }
}
