package br.com.fiap.orchestrator.core.dto;

public record RequestEvent(
        String clientId,
        String foodId,
        AddressEvent addressEvent,

) {}