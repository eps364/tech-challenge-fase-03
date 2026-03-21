package br.com.fiap.client.core.usecase;
import java.util.Map;

public record ProcessOrchestratorRequestResult(
        String correlationId,
        String responseType,
        Map<String, Object> responsePayload
) {}