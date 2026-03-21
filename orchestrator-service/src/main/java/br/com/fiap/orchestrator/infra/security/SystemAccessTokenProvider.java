package br.com.fiap.orchestrator.infra.security;

import br.com.fiap.orchestrator.infra.config.KeycloakClientCredentialsProperties;
import br.com.fiap.orchestrator.infra.security.dto.KeycloakTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.Objects;

@Component
public class SystemAccessTokenProvider {

    private final RestClient restClient;
    private final KeycloakClientCredentialsProperties properties;

    private volatile String accessToken;
    private volatile Instant expiresAt;

    public SystemAccessTokenProvider(RestClient.Builder restClientBuilder,
                                     KeycloakClientCredentialsProperties properties) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
    }

    public synchronized String getAccessToken() {
        if (isCachedTokenValid()) {
            return accessToken;
        }

        KeycloakTokenResponse tokenResponse = requestNewToken();
        this.accessToken = Objects.requireNonNull(tokenResponse.accessToken(), "access_token não retornado pelo Keycloak");
        this.expiresAt = Instant.now().plusSeconds(Math.max(1, tokenResponse.expiresIn() - 30));
        return accessToken;
    }

    private boolean isCachedTokenValid() {
        return accessToken != null && expiresAt != null && Instant.now().isBefore(expiresAt);
    }

    private KeycloakTokenResponse requestNewToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", properties.clientId());
        formData.add("client_secret", properties.clientSecret());

        try {
            KeycloakTokenResponse response = restClient.post()
                    .uri(properties.tokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(KeycloakTokenResponse.class);

            return Objects.requireNonNull(response, "Resposta vazia ao obter token do Keycloak");
        } catch (RestClientException ex) {
            throw new IllegalStateException("Falha ao obter token técnico no Keycloak", ex);
        }
    }
}
