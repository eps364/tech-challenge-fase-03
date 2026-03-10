package br.com.fiap.authservice.core.dto;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String firstName,
        String lastName
) {
}