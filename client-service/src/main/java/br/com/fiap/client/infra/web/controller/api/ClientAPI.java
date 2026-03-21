package br.com.fiap.client.infra.web.controller.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import br.com.fiap.client.core.dto.ClientRequest;
import br.com.fiap.client.core.dto.ClientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Client", description = "API for managing clients")
public interface ClientAPI {

    @Operation(summary = "Create a new client", description = "Creates a new client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}")
    ResponseEntity<ClientResponse> create(@Parameter(description = "Client ID") @PathVariable UUID id, @RequestBody ClientRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<ClientResponse> get(@Parameter(description = "Client ID") @PathVariable UUID id, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Update client", description = "Updates an existing client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    ResponseEntity<ClientResponse> update(@Parameter(description = "Client ID") @PathVariable UUID id, @RequestBody ClientRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Delete client", description = "Deletes a client from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "Client ID") @PathVariable UUID id, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);
}
