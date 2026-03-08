package br.com.fiap.restaurant.core.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String nome,
        boolean ativo,
        String street,
        String number,
        String district,
        String complement,
        String city,
        String state,
        String zipCode,
        List<UUID> owners
) {}
