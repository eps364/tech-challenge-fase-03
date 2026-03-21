package br.com.fiap.payment.core.dto;

import java.util.UUID;

public record PaymentAcceptedResponse(UUID id, String message) {}
