package br.com.fiap.restaurant.core.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RestaurantRequest(
        @JsonProperty("nome") @JsonAlias("name") String name,
        @JsonProperty("ativo") @JsonAlias("active") boolean active,
        String street,
        String number,
        String district,
        String complement,
        String city,
        String state,
        String zipCode
) {}
