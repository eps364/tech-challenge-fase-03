package br.com.fiap.catalog.core.usecase;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class ListProductsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ListProductsUseCase.class);

    private final ProductRepositoryPort repo;

    public ListProductsUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public List<ProductResponse> execute() {
        logger.info("Listing all products");
        List<ProductResponse> products = repo.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getRestaurantId(), p.getFoodType()))
                .toList();
        logger.info("Found {} products", products.size());
        return products;
    }
}
