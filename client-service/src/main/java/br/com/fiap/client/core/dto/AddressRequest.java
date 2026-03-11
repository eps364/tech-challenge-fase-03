package br.com.fiap.client.core.dto;

public record AddressRequest(
        String street,
        String number,
        String neighborhood,
        String complement,
        String city,
        String state,
        String zipCode
) {
}
