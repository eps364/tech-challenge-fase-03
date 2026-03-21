package br.com.fiap.orchestrator.core.exception;

public class OrchestrationServiceException extends RuntimeException {
    public OrchestrationServiceException(String message) {
        super(message);
    }
}