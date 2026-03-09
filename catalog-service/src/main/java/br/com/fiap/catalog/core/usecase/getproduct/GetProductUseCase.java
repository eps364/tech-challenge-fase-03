package br.com.fiap.catalog.core.usecase.getproduct;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class GetProductUseCase {

    private final ProductRepositoryPort repo;

    public GetProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getRestaurantId(), p.getFoodType());
    }
}
