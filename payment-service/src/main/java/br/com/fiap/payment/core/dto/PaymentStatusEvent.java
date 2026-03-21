package br.com.fiap.payment.core.dto;

import java.time.Instant;
import java.util.UUID;

public record PaymentStatusEvent(
        String type,
        UUID orderId,
        UUID paymentId,
        String status,
        String reason,
        Instant occurredAt
) {
}