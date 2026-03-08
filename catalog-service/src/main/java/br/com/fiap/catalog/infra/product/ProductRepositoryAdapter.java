package br.com.fiap.catalog.infra.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll().stream()
                .map(e -> new Product(e.getId(), e.getName(), e.getPrice()))
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id)
                .map(e -> new Product(e.getId(), e.getName(), e.getPrice()));
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity(product.getId(), product.getName(), product.getPrice());
        ProductEntity saved = jpa.save(entity);
        return new Product(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public void delete(Long id) {
        jpa.deleteById(id);
    }
}

