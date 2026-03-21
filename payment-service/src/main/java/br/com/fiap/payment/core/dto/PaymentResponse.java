package br.com.fiap.payment.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.payment.core.domain.PaymentStatus;

public record PaymentResponse(
    UUID id,
    UUID orderId,
    UUID clientId,
    BigDecimal amount,
    PaymentStatus status,
    LocalDateTime createdAt
) {}
