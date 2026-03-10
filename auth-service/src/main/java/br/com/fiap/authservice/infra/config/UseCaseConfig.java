package br.com.fiap.authservice.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;
import br.com.fiap.authservice.core.gateway.UserGateway;
import br.com.fiap.authservice.core.usecase.LoginUseCase;
import br.com.fiap.authservice.core.usecase.LogoutUseCase;
import br.com.fiap.authservice.core.usecase.RefreshTokenUseCase;
import br.com.fiap.authservice.core.usecase.RegisterUserUseCase;

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
    public RefreshTokenUseCase refreshTokenUseCase(IdentityProviderGateway identityProviderGateway) {
        return new RefreshTokenUseCase(identityProviderGateway);
    }

    @Bean
    public LogoutUseCase logoutUseCase(IdentityProviderGateway identityProviderGateway,
                                       TokenBlacklistGateway tokenBlacklistGateway) {
        return new LogoutUseCase(identityProviderGateway, tokenBlacklistGateway);
    }
}
