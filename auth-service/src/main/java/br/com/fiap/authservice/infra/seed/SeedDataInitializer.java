package br.com.fiap.authservice.infra.seed;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.fiap.authservice.infra.persistence.UserEntity;
import br.com.fiap.authservice.infra.persistence.UserRepository;
import jakarta.ws.rs.core.Response;

/**
 * Inicializa os usuários de seed no Keycloak e no banco local do auth-service.
 *
 * Fluxo por usuário:
 *  1. Verifica se já existe no banco local → pula se sim.
 *  2. Busca no Keycloak pelo username exato.
 *     a. Encontrado → usa o UUID do Keycloak para sincronizar ao banco.
 *     b. Não encontrado → cria no Keycloak com a role correspondente, depois salva no banco.
 *
 * Seed: admin/admin (role: admin), user/user (role: user), owner/owner (role: owner).
 */
@Component
public class SeedDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataInitializer.class);

    private record SeedUser(String username, String password, String email,
                            String firstName, String lastName, String role) {}

    private static final List<SeedUser> SEED_USERS = List.of(
            new SeedUser("admin", "admin", "admin@techchallenge.local", "Admin",   "User", "admin"),
            new SeedUser("user",  "user",  "user@techchallenge.local",  "Default", "User", "user"),
            new SeedUser("owner", "owner", "owner@techchallenge.local", "Owner",   "User", "owner")
    );

    private final Keycloak keycloak;
    private final UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realm;

    public SeedDataInitializer(Keycloak keycloak, UserRepository userRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== SeedDataInitializer: starting ===");
        for (SeedUser seed : SEED_USERS) {
            try {
                seedUser(seed);
            } catch (Exception e) {
                log.warn("Failed to seed user '{}': {}", seed.username(), e.getMessage());
            }
        }
        log.info("=== SeedDataInitializer: done ===");
    }

    private void seedUser(SeedUser seed) {
        // 1. Skip if already in local DB
        if (userRepository.findByUsername(seed.username()).isPresent()) {
            log.debug("Seed user '{}' already in local DB, skipping.", seed.username());
            return;
        }

        // 2. Check Keycloak
        List<UserRepresentation> found = keycloak.realm(realm).users().searchByUsername(seed.username(), true);
        String keycloakId;

        if (!found.isEmpty()) {
            // User already in Keycloak (imported via realm-export): just sync UUID to DB
            keycloakId = found.get(0).getId();
            log.info("Seed user '{}' found in Keycloak (id={}), syncing to local DB.", seed.username(), keycloakId);
        } else {
            // Create in Keycloak with the requested realm role
            keycloakId = createInKeycloak(seed);
            log.info("Seed user '{}' created in Keycloak (id={}, role={}).", seed.username(), keycloakId, seed.role());
        }

        // 3. Save to local DB
        userRepository.save(new UserEntity(
                UUID.fromString(keycloakId), seed.username(), seed.email(), seed.firstName(), seed.lastName()));
        log.info("Seed user '{}' saved to local DB.", seed.username());
    }

    private String createInKeycloak(SeedUser seed) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(seed.username());
        userRep.setEmail(seed.email());
        userRep.setFirstName(seed.firstName());
        userRep.setLastName(seed.lastName());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(seed.password());
        cred.setTemporary(false);
        userRep.setCredentials(Collections.singletonList(cred));

        String userId;
        try (Response response = keycloak.realm(realm).users().create(userRep)) {
            if (response.getStatus() != 201) {
                String body = response.readEntity(String.class);
                throw new IllegalStateException(
                        "Keycloak returned HTTP " + response.getStatus() + " creating user '" + seed.username() + "': " + body);
            }
            String path = response.getLocation().getPath();
            userId = path.substring(path.lastIndexOf('/') + 1);
        }

        // Assign requested realm role
        RoleRepresentation realmRole = keycloak.realm(realm).roles().get(seed.role()).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel()
                .add(Collections.singletonList(realmRole));

        // Grant standard account client roles
        String accountClientUuid = keycloak.realm(realm).clients()
                .findByClientId("account").get(0).getId();
        keycloak.realm(realm).users().get(userId).roles()
                .clientLevel(accountClientUuid)
                .add(List.of(
                        keycloak.realm(realm).clients().get(accountClientUuid).roles().get("manage-account").toRepresentation(),
                        keycloak.realm(realm).clients().get(accountClientUuid).roles().get("view-profile").toRepresentation()
                ));

        return userId;
    }
}
