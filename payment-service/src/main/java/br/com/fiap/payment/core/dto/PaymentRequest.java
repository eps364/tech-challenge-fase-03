package br.com.fiap.payment.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
    UUID orderId,
    String paymentMethod,
    BigDecimal amount
) {}
