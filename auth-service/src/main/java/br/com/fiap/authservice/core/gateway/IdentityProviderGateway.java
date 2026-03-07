package br.com.fiap.authservice.core.gateway;

import br.com.fiap.authservice.core.domain.User;

public interface IdentityProviderGateway {
    String createUser(User user, String password);
    String login(String username, String password);
    void logout(String userId);
}
