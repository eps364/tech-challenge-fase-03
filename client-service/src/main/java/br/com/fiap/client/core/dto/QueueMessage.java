package br.com.fiap.client.core.dto;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record QueueMessage(
        String correlationId,
        @JsonProperty("type")
        @JsonAlias("tipo")
        String type,
        Instant timestamp,
        Map<String, Object> payload
) {}