package br.com.fiap.orchestrator.core.exception;

public class OrchestrationInvalidRequestException extends RuntimeException {
    private final String field;
    public OrchestrationInvalidRequestException(String message, String field) {
        super(message);
        this.field = field;
    }
    public String getField() {
        return field;
    }
}