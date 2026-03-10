package br.com.fiap.catalog.core.usecase;
public class ProductAccessDeniedException extends RuntimeException {

    public ProductAccessDeniedException(String message) {
        super(message);
    }
}
