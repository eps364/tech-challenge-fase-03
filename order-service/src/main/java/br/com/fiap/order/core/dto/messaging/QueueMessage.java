package br.com.fiap.order.core.dto.messaging;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record QueueMessage(
        String correlationId,
        @JsonProperty("type")
        @JsonAlias("tipo")
        String type,
        String timestamp,
        Map<String, Object> payload
) {}
