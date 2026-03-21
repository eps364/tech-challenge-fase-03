package br.com.fiap.order.core.dto.responses;

import java.util.UUID;

public record OrderAcceptedResponse(UUID id, String message) {}
