package br.com.fiap.client.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "street is required")
        @Size(max = 255, message = "street must have at most 255 characters")
        String street,
        @NotBlank(message = "number is required")
        @Size(max = 32, message = "number must have at most 32 characters")
        String number,
        @NotBlank(message = "neighborhood is required")
        @Size(max = 255, message = "neighborhood must have at most 255 characters")
        String neighborhood,
        @Size(max = 255, message = "complement must have at most 255 characters")
        String complement,
        @NotBlank(message = "city is required")
        @Size(max = 255, message = "city must have at most 255 characters")
        String city,
        @NotBlank(message = "state is required")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "state must have 2 letters")
        String state,
        @NotBlank(message = "zipCode is required")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "zipCode must match 99999-999 or 99999999")
        String zipCode
) {
}
