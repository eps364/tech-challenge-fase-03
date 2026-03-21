package br.com.fiap.catalog.infra.web.controller.api;

import java.util.List;
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

import br.com.fiap.catalog.core.dto.ResolveProductsRequest;
import br.com.fiap.catalog.core.dto.ResolveProductsResponse;

import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "API for managing products in the catalog")
public interface ProductAPI {

    @Operation(summary = "Create a new product", description = "Creates a new product in the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "List all products", description = "Retrieves a list of all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of products retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    ResponseEntity<List<ProductResponse>> list();

    @Operation(summary = "List products by restaurant", description = "Retrieves a list of all products for a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of products retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant/{restaurantId}")
    ResponseEntity<List<ProductResponse>> listByRestaurant(@Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId);

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> get(@Parameter(description = "Product ID") @PathVariable UUID id);

    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> update(@Parameter(description = "Product ID") @PathVariable UUID id, @RequestBody ProductRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Delete product", description = "Deletes a product from the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "Product ID") @PathVariable UUID id, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);

    @Operation(summary = "Resolve products", description = "Resolves multiple products by ID and restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products resolved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/resolve")
    ResponseEntity<ResolveProductsResponse> resolveProducts(@RequestBody ResolveProductsRequest request, @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt);
}
