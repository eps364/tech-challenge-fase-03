package br.com.fiap.catalog.core.usecase;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

import java.util.UUID;

public class UpdateProductUseCase {

    private final ProductRepositoryPort repo;

    public UpdateProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(UUID id, ProductRequest req) {
        Product current = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Product updated = new Product(id, req.name(), req.price(), current.getRestaurantId(), req.foodType());
        Product saved = repo.save(updated);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice(), saved.getRestaurantId(), saved.getFoodType());
    }
}
