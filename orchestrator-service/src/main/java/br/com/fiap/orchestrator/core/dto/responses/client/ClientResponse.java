package br.com.fiap.orchestrator.core.dto.responses.client;

import java.util.UUID;

public record ClientResponse(
        UUID id,
        String cpf,
        boolean active,
        AddressResponse address
) {
}
