package br.com.fiap.orchestrator.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.client")
public record KeycloakClientCredentialsProperties(
        String tokenUri,
        String clientId,
        String clientSecret
) {
}
