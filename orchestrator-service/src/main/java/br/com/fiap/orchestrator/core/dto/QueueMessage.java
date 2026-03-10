package br.com.fiap.orchestrator.core.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record QueueMessage(
        String correlationId,
        @JsonProperty("type")
        @JsonAlias("tipo")
        String type,
        String timestamp,
        Map<String, Object> payload
) {}