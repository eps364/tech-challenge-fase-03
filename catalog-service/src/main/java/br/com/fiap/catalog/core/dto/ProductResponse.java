package br.com.fiap.catalog.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        UUID restaurantId,
        String foodType
) {}
