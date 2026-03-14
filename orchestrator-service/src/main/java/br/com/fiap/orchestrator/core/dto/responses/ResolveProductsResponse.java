package br.com.fiap.orchestrator.core.dto.responses;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record ResolveProductsResponse(
        Map<UUID, ProductResponse> products
) {
    public record ProductResponse(
            UUID id,
            String name,
            BigDecimal price,
            UUID restaurantId,
            String foodType
    ) {
    }
}