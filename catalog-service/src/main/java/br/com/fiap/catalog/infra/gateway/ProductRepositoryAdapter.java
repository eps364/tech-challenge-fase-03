package br.com.fiap.catalog.infra.gateway;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.infra.entity.ProductEntity;
import br.com.fiap.catalog.infra.repository.ProductJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByRestaurantId(UUID restaurantId) {
        return jpa.findByRestaurantId(restaurantId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public void delete(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Product> findAllByIds(List<UUID> ids) {
        return jpa.findAllByIdIn(ids).stream()
                .map(this::toDomain)
                .toList();
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getRestaurantId(),
                entity.getFoodType()
        );
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getRestaurantId(),
                product.getFoodType()
        );
    }
}