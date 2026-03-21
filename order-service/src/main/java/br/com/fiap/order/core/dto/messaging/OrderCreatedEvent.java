package br.com.fiap.order.core.dto.messaging;

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