package br.com.fiap.catalog.infra.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantOwnershipDTO(
        UUID id,
        List<UUID> owners
) {}
