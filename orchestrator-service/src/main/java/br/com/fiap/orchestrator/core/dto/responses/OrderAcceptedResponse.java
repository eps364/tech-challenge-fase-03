package br.com.fiap.orchestrator.core.dto.responses;

import java.util.UUID;

public record OrderAcceptedResponse(UUID id, String message) {}
