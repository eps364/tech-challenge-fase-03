package br.com.fiap.catalog.core.usecase.updateproduct;

public class ProductAccessDeniedException extends RuntimeException {

    public ProductAccessDeniedException(String message) {
        super(message);
    }
}
