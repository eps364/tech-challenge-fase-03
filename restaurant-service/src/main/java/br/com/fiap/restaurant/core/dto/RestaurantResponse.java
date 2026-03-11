package br.com.fiap.restaurant.core.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RestaurantResponse(
        UUID id,
    @JsonProperty("nome") String name,
    @JsonProperty("ativo") boolean active,
        String street,
        String number,
        String district,
        String complement,
        String city,
        String state,
        String zipCode,
        List<UUID> owners,
        boolean refreshTokenRequired
) {
    public RestaurantResponse(UUID id, String name, boolean active, String street, String number,
            String district, String complement, String city, String state, String zipCode,
            List<UUID> owners) {
        this(id, name, active, street, number, district, complement, city, state, zipCode, owners, false);
    }
}
