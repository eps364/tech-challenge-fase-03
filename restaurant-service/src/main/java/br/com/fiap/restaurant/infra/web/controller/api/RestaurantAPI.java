package br.com.fiap.restaurant.infra.web.controller.api;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import br.com.fiap.restaurant.core.dto.AddOwnerRequest;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Restaurant", description = "API for managing restaurants in the system")
public interface RestaurantAPI {

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "List all restaurants", description = "Retrieves a list of all restaurants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    ResponseEntity<List<RestaurantResponse>> list();

    @Operation(summary = "List owned restaurants", description = "Retrieves a list of all restaurants owned by the current user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of owned restaurants retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/owned")
    ResponseEntity<List<RestaurantResponse>> listOwned(@Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Get restaurant by ID", description = "Retrieves a specific restaurant by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<RestaurantResponse> get(@Parameter(description = "Restaurant ID") @PathVariable UUID id);

    @Operation(summary = "Update restaurant", description = "Updates an existing restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    ResponseEntity<RestaurantResponse> update(@Parameter(description = "Restaurant ID") @PathVariable UUID id, @RequestBody RestaurantRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Delete restaurant", description = "Deletes a restaurant from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "Restaurant ID") @PathVariable UUID id, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Add owner to restaurant", description = "Adds a new owner to a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Owner added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{restaurantId}/owners")
    ResponseEntity<RestaurantResponse> addOwner(@Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId, @RequestBody AddOwnerRequest request);
}
