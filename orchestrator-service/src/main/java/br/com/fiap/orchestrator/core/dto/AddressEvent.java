package br.com.fiap.orchestrator.core.dto;

public record AddressEvent(
        String street,
         String number,
         String city,
         String neighborhood,
         String country,
         String state,
         String zipCode
) {}