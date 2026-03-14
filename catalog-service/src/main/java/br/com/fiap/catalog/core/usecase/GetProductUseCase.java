package br.com.fiap.catalog.core.usecase;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

import java.util.UUID;

public class GetProductUseCase {

    private final ProductRepositoryPort repo;

    public GetProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(UUID id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getRestaurantId(), p.getFoodType());
    }
}
