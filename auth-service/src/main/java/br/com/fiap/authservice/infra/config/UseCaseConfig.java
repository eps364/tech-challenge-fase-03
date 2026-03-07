package br.com.fiap.authservice.infra.config;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.UserGateway;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserGateway userGateway, IdentityProviderGateway identityProviderGateway) {
        return new RegisterUserUseCase(userGateway, identityProviderGateway);
    }

    @Bean
    public LoginUseCase loginUseCase(IdentityProviderGateway identityProviderGateway) {
        return new LoginUseCase(identityProviderGateway);
    }

    @Bean
    public LogoutUseCase logoutUseCase(IdentityProviderGateway identityProviderGateway) {
        return new LogoutUseCase(identityProviderGateway);
    }
}
