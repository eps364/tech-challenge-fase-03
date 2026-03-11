package br.com.fiap.client.core.usecase;

public class ClientConflictException extends RuntimeException {
    public ClientConflictException(String message) {
        super(message);
    }
}
