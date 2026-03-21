package br.com.fiap.orchestrator.core.exception;

public class OrchestrationNotFoundException extends RuntimeException {
    public OrchestrationNotFoundException(String message) {
        super(message);
    }
}