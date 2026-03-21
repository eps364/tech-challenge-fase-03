package br.com.fiap.order.infra.web.controller.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.order.core.dto.requests.order.CreateOrderRequest;
import br.com.fiap.order.core.dto.responses.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order", description = "API for managing orders")
public interface OrderAPI {

    @Operation(summary = "Create a new order", description = "Creates a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/orchestrated")
    ResponseEntity<OrderResponse> create(@RequestBody CreateOrderRequest request);

    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> findById(@Parameter(description = "Order ID") @PathVariable UUID id);

    @Operation(summary = "Get orders by client ID", description = "Retrieves all orders for a specific client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/client/{clientId}")
    ResponseEntity<List<OrderResponse>> findByClientId(@Parameter(description = "Client ID") @PathVariable UUID clientId);
}
