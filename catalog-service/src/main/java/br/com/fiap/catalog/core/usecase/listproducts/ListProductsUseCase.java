package br.com.fiap.catalog.core.usecase.listproducts;

import java.util.List;

import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class ListProductsUseCase {

    private final ProductRepositoryPort repo;

    public ListProductsUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public List<ProductResponse> execute() {
        return repo.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }
}
