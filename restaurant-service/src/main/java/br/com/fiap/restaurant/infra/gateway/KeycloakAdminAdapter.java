package br.com.fiap.restaurant.infra.gateway;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.infra.config.KeycloakAdminProperties;

@Component
public class KeycloakAdminAdapter implements KeycloakAdminPort {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminAdapter.class);

    private final RestTemplate restTemplate;
    private final KeycloakAdminProperties props;

    public KeycloakAdminAdapter(KeycloakAdminProperties props) {
        this.restTemplate = new RestTemplate();
        this.props = props;
    }

    @Override
    public void assignOwnerRole(UUID userId) {
        try {
            String adminToken = getAdminToken();
            Map<String, Object> ownerRole = getRole(adminToken, "owner");
            assignRole(adminToken, userId, ownerRole);
            log.info("Owner role assigned to user {}", userId);
        } catch (Exception e) {
            log.warn("Could not assign owner role to user {}: {}", userId, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", props.getAdminClientId());
        body.add("username", props.getAdminUsername());
        body.add("password", props.getAdminPassword());

        String url = props.getUrl() + "/realms/master/protocol/openid-connect/token";
        Map<String, Object> response = restTemplate.postForObject(
                url, new HttpEntity<>(body, headers), Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new IllegalStateException("Failed to obtain Keycloak admin token");
        }
        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getRole(String adminToken, String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        String url = props.getUrl() + "/admin/realms/" + props.getRealm() + "/roles/" + roleName;
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        return response.getBody();
    }

    private void assignRole(String adminToken, UUID userId, Map<String, Object> roleRepresentation) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = props.getUrl() + "/admin/realms/" + props.getRealm()
                + "/users/" + userId + "/role-mappings/realm";
        restTemplate.postForObject(url,
                new HttpEntity<>(List.of(roleRepresentation), headers), Void.class);
    }
}