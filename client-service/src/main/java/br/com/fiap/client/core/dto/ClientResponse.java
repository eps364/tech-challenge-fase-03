package br.com.fiap.client.core.dto;

import java.util.UUID;

public record ClientResponse(
        UUID id,
        String cpf,
        boolean active,
        AddressResponse address
) {
}
