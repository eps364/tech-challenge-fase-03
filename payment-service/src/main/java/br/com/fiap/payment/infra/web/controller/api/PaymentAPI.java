package br.com.fiap.payment.infra.web.controller.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.payment.core.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment", description = "API for managing payments")
public interface PaymentAPI {

    @Operation(summary = "Get payment by order ID", description = "Retrieves payment details for a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/{orderId}")
    ResponseEntity<PaymentResponse> getByOrderId(@Parameter(description = "Order ID") @PathVariable UUID orderId);

    @Operation(summary = "Process order payment", description = "Receives orderId, paymentMethod and amount. Returns payment status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    ResponseEntity<PaymentResponse> createPayment(@RequestBody br.com.fiap.payment.core.dto.PaymentRequest request);

    @Operation(summary = "Check payment status", description = "Returns payment status by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<PaymentResponse> getPayment(@Parameter(description = "Payment ID") @PathVariable String id);
}
