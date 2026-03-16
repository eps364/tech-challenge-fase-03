package br.com.fiap.authservice.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.UserGateway;

import java.util.UUID;

public class RegisterUserUseCase {
    private static final Logger logger = LoggerFactory.getLogger(RegisterUserUseCase.class);

    private final UserGateway userGateway;
    private final IdentityProviderGateway identityProviderGateway;

    public RegisterUserUseCase(UserGateway userGateway, IdentityProviderGateway identityProviderGateway) {
        this.userGateway = userGateway;
        this.identityProviderGateway = identityProviderGateway;
    }

    public User execute(User user, String password) {
        logger.info("Registering user: {}", user.getUsername());

        String keycloakId = identityProviderGateway.createUser(user, password);
        logger.info("User created in Keycloak with id: {}", keycloakId);

        User userWithId = user.withId(UUID.fromString(keycloakId));

        User savedUser = userGateway.save(userWithId);
        logger.info("User saved in local database with id: {}", savedUser.getId());

        return savedUser;
    }
}
