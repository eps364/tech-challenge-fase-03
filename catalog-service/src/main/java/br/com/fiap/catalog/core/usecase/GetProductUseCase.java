package br.com.fiap.catalog.core.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

import java.util.UUID;

public class GetProductUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetProductUseCase.class);

    private final ProductRepositoryPort repo;

    public GetProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(UUID id) {
        logger.info("Getting product by id {}", id);
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        logger.info("Product found: {}", p.getName());
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getRestaurantId(), p.getFoodType());
    }
}
