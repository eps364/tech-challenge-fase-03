package br.com.fiap.catalog.core.usecase;
import br.com.fiap.catalog.core.domain.Product;
import br.com.fiap.catalog.core.dto.ProductRequest;
import br.com.fiap.catalog.core.dto.ProductResponse;
import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;

public class CreateProductUseCase {

    private final ProductRepositoryPort repo;

    public CreateProductUseCase(ProductRepositoryPort repo) {
        this.repo = repo;
    }

    public ProductResponse execute(ProductRequest req) {
        Product product = Product.create(req.name(), req.price(), req.restaurantId(), req.foodType());
        Product saved = repo.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice(), saved.getRestaurantId(), saved.getFoodType());
    }
}
