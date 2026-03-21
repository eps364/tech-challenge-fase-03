package br.com.fiap.orchestrator.core.dto.requests.orchestration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderItemRequest(
        @NotNull
        UUID productId,

        @NotNull
        @Min(1)
        Integer quantity
) {
}