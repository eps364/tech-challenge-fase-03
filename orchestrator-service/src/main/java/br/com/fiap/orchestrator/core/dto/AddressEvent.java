package br.com.fiap.orchestrator.core.dto;

import java.time.Instant;
import java.util.Map;

public record PedidoEvent(
        String clientId,
        String foodId,
        String E
) {}