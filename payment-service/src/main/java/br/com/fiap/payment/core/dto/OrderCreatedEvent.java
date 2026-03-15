package br.com.fiap.payment.core.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID clientId,
        BigDecimal total,
        Instant occurredAt
) {
}