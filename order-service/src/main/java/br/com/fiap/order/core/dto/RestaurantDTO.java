package br.com.fiap.order.core.dto;

import java.util.UUID;

public record RestaurantDTO(
        UUID id,
        String name,
        boolean active
) {}
