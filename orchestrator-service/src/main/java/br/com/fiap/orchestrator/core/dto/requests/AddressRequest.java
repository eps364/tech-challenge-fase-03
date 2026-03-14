package br.com.fiap.orchestrator.core.dto.requests;

public record AddressRequest(
        String street,
        String number,
        String city,
        String neighborhood,
        String country,
        String state,
        String zipCode) {
}