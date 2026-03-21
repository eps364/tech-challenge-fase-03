package br.com.fiap.orchestrator.core.exception;

import java.util.Map;

public class OrchestrationValidationException extends RuntimeException {
    private final Map<String, String> fields;

    public OrchestrationValidationException(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }

    public Map<String, String> getFields() {
        return fields;
    }
}