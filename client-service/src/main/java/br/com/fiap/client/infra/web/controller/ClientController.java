package br.com.fiap.client.infra.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.dto.ClientResponse;
import br.com.fiap.client.core.usecase.CreateClientUseCase;
import br.com.fiap.client.core.usecase.DeleteClientUseCase;
import br.com.fiap.client.core.usecase.GetClientUseCase;
import br.com.fiap.client.core.usecase.UpdateClientUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController implements br.com.fiap.client.infra.web.controller.api.ClientAPI {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final CreateClientUseCase createClient;
    private final GetClientUseCase getClient;
    private final UpdateClientUseCase updateClient;
    private final DeleteClientUseCase deleteClient;

    public ClientController(CreateClientUseCase createClient,
                            GetClientUseCase getClient,
                            UpdateClientUseCase updateClient,
                            DeleteClientUseCase deleteClient) {
        this.createClient = createClient;
        this.getClient = getClient;
        this.updateClient = updateClient;
        this.deleteClient = deleteClient;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ClientResponse> create(@PathVariable UUID id,
                                                 @Valid @RequestBody ClientRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        logger.info("Received create client request for ID {} by caller {} (admin: {})", id, callerId, isAdmin);
        try {
            ClientResponse created = createClient.execute(id, callerId, isAdmin, request);
            logger.info("Client created successfully: {}", created.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Error creating client {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> get(@PathVariable UUID id,
                                              @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        logger.info("Received get client request for ID {} by caller {} (admin: {})", id, callerId, isAdmin);
        try {
            ClientResponse client = getClient.execute(id, callerId, isAdmin);
            logger.info("Client retrieved successfully: {}", client.id());
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            logger.error("Error retrieving client {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody ClientRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        logger.info("Received update client request for ID {} by caller {} (admin: {})", id, callerId, isAdmin);
        try {
            ClientResponse updated = updateClient.execute(id, callerId, isAdmin, request);
            logger.info("Client updated successfully: {}", updated.id());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Error updating client {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        logger.info("Received delete client request for ID {} by caller {} (admin: {})", id, callerId, isAdmin);
        try {
            deleteClient.execute(id, callerId, isAdmin);
            logger.info("Client deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting client {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private UUID extractCallerId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    private boolean isAdminOrSystem(Jwt jwt) {
        return extractRoles(jwt).contains("admin") || extractRoles(jwt).contains("system");
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> rolesValue)) {
            return Collections.emptyList();
        }

        return rolesValue.stream().map(String::valueOf).toList();
    }
}
