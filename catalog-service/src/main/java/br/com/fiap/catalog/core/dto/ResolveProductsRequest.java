package br.com.fiap.catalog.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ResolveProductsRequest(
        @NotNull
        UUID restaurantId,

        @NotEmpty
        List<UUID> productIds
) {
}