package br.com.fiap.order.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        Long id,
        String name,
        BigDecimal price,
        UUID restaurantId,
        String foodType
) {}
