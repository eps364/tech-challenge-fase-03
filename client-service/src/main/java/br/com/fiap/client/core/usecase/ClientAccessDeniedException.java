package br.com.fiap.client.core.usecase;

public class ClientAccessDeniedException extends RuntimeException {
    public ClientAccessDeniedException(String message) {
        super(message);
    }
}
