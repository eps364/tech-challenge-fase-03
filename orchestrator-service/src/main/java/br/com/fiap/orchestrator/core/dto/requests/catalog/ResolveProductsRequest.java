package br.com.fiap.orchestrator.core.dto.requests.catalog;

import java.util.List;
import java.util.UUID;

public record ResolveProductsRequest(
        UUID restaurantId,
        List<UUID> productIds
) {
}