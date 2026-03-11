package br.com.fiap.client.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ClientRequest(
        String cpf,
        @NotNull(message = "address is required")
        @Valid
        AddressRequest address
) {
}
