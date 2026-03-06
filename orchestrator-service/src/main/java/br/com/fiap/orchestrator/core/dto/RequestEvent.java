package br.com.fiap.orchestrator.core.dto;

import java.time.LocalDateTime;

public record RequestEvent(
        String clientId,
        String cpf,
        String restaurantId,
        String foodId,
        AddressEvent addressEvent,
        PriceEvent price,
        String paymentMode,
        LocalDateTime requestDate

) {
}