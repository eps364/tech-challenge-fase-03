package br.com.fiap.client.infra.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
public class ClientController {

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
        ClientResponse created = createClient.execute(id, callerId, isAdmin, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> get(@PathVariable UUID id,
                                              @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        return ResponseEntity.ok(getClient.execute(id, callerId, isAdmin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody ClientRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        return ResponseEntity.ok(updateClient.execute(id, callerId, isAdmin, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal Jwt jwt) {
        UUID callerId = extractCallerId(jwt);
        boolean isAdmin = isAdminOrSystem(jwt);
        deleteClient.execute(id, callerId, isAdmin);
        return ResponseEntity.noContent().build();
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
