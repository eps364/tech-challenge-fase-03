package br.com.fiap.client.core.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
        @NotBlank(message = "cpf is required")
        @Pattern(regexp = "^\\d{11}$", message = "cpf must have 11 digits")
        String cpf,
        @NotNull(message = "address is required")
        @Valid
        AddressRequest address
) {
}
