package br.com.fiap.restaurant.core.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaidForPreparationEvent(
        UUID eventId,
        Instant occurredAt,
        UUID orderId,
        UUID restaurantId,
        UUID clientId,
        BigDecimal total,
        String status
) {
}