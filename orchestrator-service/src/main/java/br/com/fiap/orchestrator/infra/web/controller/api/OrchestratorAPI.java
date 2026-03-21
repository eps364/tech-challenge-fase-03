package br.com.fiap.orchestrator.infra.web.controller.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.orchestrator.core.dto.requests.orchestration.CreateOrderRequest;
import br.com.fiap.orchestrator.core.dto.responses.OrderAcceptedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Orchestrator", description = "API for orchestrating order creation")
public interface OrchestratorAPI {

    @Operation(summary = "Create orchestrated order", description = "Creates an order via orchestration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order accepted and will be processed."),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/requests")
    org.springframework.http.ResponseEntity<OrderAcceptedResponse> criarPedido(@RequestBody CreateOrderRequest event);
}
