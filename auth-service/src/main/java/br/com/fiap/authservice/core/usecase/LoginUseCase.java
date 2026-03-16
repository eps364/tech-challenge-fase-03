package br.com.fiap.authservice.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;

public class LoginUseCase {
    private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);

    private final IdentityProviderGateway identityProviderGateway;

    public LoginUseCase(IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = identityProviderGateway;
    }

    public LoginResult execute(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        return identityProviderGateway.login(username, password);
    }
}
