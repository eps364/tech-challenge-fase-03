package br.com.fiap.restaurantes.core.dto;

import java.time.Instant;
import java.util.Map;

public record MensagemFila(
        String correlationId,
        String tipo,
        Instant timestamp,
        Map<String, Object> payload
) {}