package br.com.fiap.restaurant.core.dto;

public record RestaurantRequest(
        String nome,
        boolean ativo,
        String street,
        String number,
        String district,
        String complement,
        String city,
        String state,
        String zipCode
) {}
