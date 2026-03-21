package br.com.fiap.catalog.core.gateway;

import br.com.fiap.catalog.core.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {
    List<Product> findAll();

    List<Product> findByRestaurantId(UUID restaurantId);

    Optional<Product> findById(UUID id);

    Product save(Product product);

    void delete(UUID id);

    List<Product> findAllByIds(List<UUID> ids);
}