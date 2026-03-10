package br.com.fiap.catalog.core.gateway;

import java.util.List;
import java.util.Optional;

import br.com.fiap.catalog.core.domain.Product;

public interface ProductRepositoryPort {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Long id);
}
