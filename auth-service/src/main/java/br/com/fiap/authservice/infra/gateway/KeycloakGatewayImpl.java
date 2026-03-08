package br.com.fiap.authservice.infra.gateway;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.fiap.authservice.core.domain.LoginResult;
import br.com.fiap.authservice.core.domain.User;
import br.com.fiap.authservice.core.gateway.IdentityProviderGateway;
import jakarta.ws.rs.core.Response;

@Component
public class KeycloakGatewayImpl implements IdentityProviderGateway {

    private final Keycloak keycloak;
    private final ObjectMapper objectMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.public-client-id}")
    private String publicClientId;

    public KeycloakGatewayImpl(Keycloak keycloak, ObjectMapper objectMapper) {
        this.keycloak = keycloak;
        this.objectMapper = objectMapper;
    }

    @Override
    public String createUser(User user, String password) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(user.getUsername());
        userRep.setEmail(user.getEmail());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        userRep.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(userRep);

        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);

            // Assign realm-level role "user"
            RoleRepresentation userRole = keycloak.realm(realm).roles().get("user").toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

            // Assign client-level roles for "account" client
            String accountClientUuid = keycloak.realm(realm).clients().findByClientId("account").get(0).getId();
            List<RoleRepresentation> accountRoles = new ArrayList<>();
            accountRoles.add(keycloak.realm(realm).clients().get(accountClientUuid).roles().get("manage-account").toRepresentation());
            accountRoles.add(keycloak.realm(realm).clients().get(accountClientUuid).roles().get("view-profile").toRepresentation());
            
            keycloak.realm(realm).users().get(userId).roles().clientLevel(accountClientUuid).add(accountRoles);

            // Populate user roles
            List<String> roles = new ArrayList<>();
            roles.add("user");
            roles.add("manage-account");
            roles.add("view-profile");
            user.setRoles(roles);

            return userId;
        } else if (response.getStatus() == 409) {
            throw new RuntimeException("User already exists in Keycloak");
        } else {
            String errorResponse = response.readEntity(String.class);
            throw new RuntimeException("Error creating user in Keycloak: " + response.getStatusInfo() + " - " + errorResponse);
        }
    }

    @Override
    public LoginResult login(String username, String password) {
        try (Keycloak userKeycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(publicClientId)
                .username(username)
                .password(password)
                .grantType("password")
                .build()) {

            org.keycloak.representations.AccessTokenResponse tokenResponse =
                    userKeycloak.tokenManager().getAccessToken();
            String accessToken = tokenResponse.getToken();

            // Decode JWT payload (second segment) to extract sub and realm_access.roles
            String[] parts = accessToken.split("\\.");
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            JsonNode payload = objectMapper.readTree(payloadBytes);

            UUID userId = UUID.fromString(payload.get("sub").asText());

            List<String> roles = new ArrayList<>();
            JsonNode realmAccess = payload.get("realm_access");
            if (realmAccess != null && realmAccess.has("roles")) {
                for (JsonNode role : realmAccess.get("roles")) {
                    roles.add(role.asText());
                }
            }

            return new LoginResult(
                    userId,
                    accessToken,
                    tokenResponse.getExpiresIn(),
                    tokenResponse.getRefreshExpiresIn(),
                    tokenResponse.getTokenType(),
                    roles
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials or error during login: " + e.getMessage());
        }
    }

    @Override
    public void logout(String userId) {
        keycloak.realm(realm).users().get(userId).logout();
    }
}
