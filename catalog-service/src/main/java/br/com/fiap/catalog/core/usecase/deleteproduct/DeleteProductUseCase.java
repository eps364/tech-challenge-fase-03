package br.com.fiap.catalog.core.usecase.deleteproduct;

import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.core.usecase.getproduct.ProductNotFoundException;

public class DeleteProductUseCase {

    private final ProductRepositoryPort repo;

    public DeleteProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(Long id) {
        repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        repo.delete(id);
    }
}
