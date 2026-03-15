package br.com.fiap.orchestrator.core.dto.responses.client;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        String street,
        String number,
        String neighborhood,
        String complement,
        String city,
        String state,
        String zipCode
) {
}
