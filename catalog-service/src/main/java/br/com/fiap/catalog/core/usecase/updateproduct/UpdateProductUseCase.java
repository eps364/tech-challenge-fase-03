package br.com.fiap.catalog.core.usecase.updateproduct;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.core.usecase.getproduct.ProductNotFoundException;

public class UpdateProductUseCase {

    private final ProductRepositoryPort repo;

    public UpdateProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(Long id, ProductRequest req) {
        repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Product updated = new Product(id, req.name(), req.price());
        Product saved = repo.save(updated);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice());
    }
}
