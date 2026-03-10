package br.com.fiap.catalog.infra.gateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.infra.entity.ProductEntity;
import br.com.fiap.catalog.infra.repository.ProductJpaRepository;

@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll().stream()
                .map(e -> new Product(e.getId(), e.getName(), e.getPrice(), e.getRestaurantId(), e.getFoodType()))
                .toList();
    }

    @Override
    public List<Product> findByRestaurantId(UUID restaurantId) {
        return jpa.findByRestaurantId(restaurantId).stream()
                .map(e -> new Product(e.getId(), e.getName(), e.getPrice(), e.getRestaurantId(), e.getFoodType()))
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id)
                .map(e -> new Product(e.getId(), e.getName(), e.getPrice(), e.getRestaurantId(), e.getFoodType()));
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getRestaurantId(), product.getFoodType());
        ProductEntity saved = jpa.save(entity);
        return new Product(saved.getId(), saved.getName(), saved.getPrice(), saved.getRestaurantId(), saved.getFoodType());
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}