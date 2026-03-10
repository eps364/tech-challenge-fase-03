package br.com.fiap.catalog.core.usecase;
import java.util.List;
import java.util.UUID;

import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class ListProductsByRestaurantUseCase {

    private final ProductRepositoryPort repo;

    public ListProductsByRestaurantUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public List<ProductResponse> execute(UUID restaurantId) {
        return repo.findByRestaurantId(restaurantId).stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getRestaurantId(), p.getFoodType()))
                .toList();
    }
}
