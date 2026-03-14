package br.com.fiap.catalog.core.usecase;

import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

import java.util.UUID;

public class DeleteProductUseCase {

    private final ProductRepositoryPort repo;

    public DeleteProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(UUID id) {
        repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        repo.delete(id);
    }
}
