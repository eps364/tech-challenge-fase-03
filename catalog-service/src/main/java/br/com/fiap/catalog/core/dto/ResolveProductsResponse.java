package br.com.fiap.catalog.core.dto;

import java.util.Map;
import java.util.UUID;

public record ResolveProductsResponse(
        Map<UUID, ProductResponse> products
) {
}