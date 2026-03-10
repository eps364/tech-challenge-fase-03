package br.com.fiap.catalog.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
        String name,
        BigDecimal price,
        UUID restaurantId,
        String foodType
) {}
