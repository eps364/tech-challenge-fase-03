package br.com.fiap.catalog.core.usecase;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.dto.ResolveProductsRequest;
import br.com.fiap.catalog.core.dto.ResolveProductsResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResolveProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ResolveProductsUseCase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public ResolveProductsResponse execute(ResolveProductsRequest request) {
        List<Product> products = productRepositoryPort.findAllByIds(request.productIds());

        if (products.size() != request.productIds().size()) {
            throw new IllegalArgumentException("Um ou mais produtos não foram encontrados");
        }

        Map<UUID, ProductResponse> resolved = new HashMap<>();

        for (Product product : products) {
            if (!product.getRestaurantId().equals(request.restaurantId())) {
                throw new IllegalArgumentException("Um ou mais produtos não pertencem ao restaurante informado");
            }

            resolved.put(
                    product.getId(),
                    new ProductResponse(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getRestaurantId(),
                            product.getFoodType()
                    )
            );
        }

        return new ResolveProductsResponse(resolved);
    }
}