package br.com.fiap.authservice.core.usecase;

import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.UserGateway;

import java.util.UUID;

public class RegisterUserUseCase {
    private final UserGateway userGateway;
    private final IdentityProviderGateway identityProviderGateway;

    public RegisterUserUseCase(UserGateway userGateway, IdentityProviderGateway identityProviderGateway) {
        this.userGateway = userGateway;
        this.identityProviderGateway = identityProviderGateway;
    }

    public User execute(User user, String password) {
        // 1. Create user in Keycloak
        String keycloakId = identityProviderGateway.createUser(user, password);
        
        // 2. Set ID from Keycloak (standard practice to sync IDs if possible, or store Keycloak ID)
        user.setId(UUID.fromString(keycloakId));

        // 3. Save user in local database
        User savedUser = userGateway.save(user);

        // 4. Preserve roles from Keycloak
        savedUser.setRoles(user.getRoles());

        return savedUser;
    }
}
