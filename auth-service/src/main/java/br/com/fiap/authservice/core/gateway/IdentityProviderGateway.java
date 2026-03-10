package br.com.fiap.authservice.core.gateway;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.domain.User;

public interface IdentityProviderGateway {
    String createUser(User user, String password);
    LoginResult login(String username, String password);
    LoginResult refreshToken(String refreshToken);
    void logout(String userId);
}
