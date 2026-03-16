package br.com.fiap.catalog.core.usecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class CreateProductUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductUseCase.class);

    private final ProductRepositoryPort repo;

    public CreateProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(ProductRequest req) {
        logger.info("Creating product '{}' for restaurant {}", req.name(), req.restaurantId());
        Product product = Product.create(req.name(), req.price(), req.restaurantId(), req.foodType());
        Product saved = repo.save(product);
        logger.info("Product created with id {}", saved.getId());
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice(), saved.getRestaurantId(), saved.getFoodType());
    }
}
