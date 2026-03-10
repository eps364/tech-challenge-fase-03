package br.com.fiap.restaurant.core.usecase;
import java.util.Map;

public record ProcessOrchestratorRequestResult(
        String correlationId,
        String responseType,
        Map<String, Object> responsePayload
) {}