package br.com.fiap.restaurant.core.usecase;
import java.util.Map;

public record ProcessOrchestratorRequestCommand(
        String correlationId,
        String type,
        Map<String, Object> payload
) {}