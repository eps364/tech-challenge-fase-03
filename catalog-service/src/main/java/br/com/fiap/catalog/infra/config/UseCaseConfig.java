package br.com.fiap.catalog.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.core.usecase.createproduct.CreateProductUseCase;
import br.com.fiap.catalog.core.usecase.deleteproduct.DeleteProductUseCase;
import br.com.fiap.catalog.core.usecase.getproduct.GetProductUseCase;
import br.com.fiap.catalog.core.usecase.listproducts.ListProductsUseCase;
import br.com.fiap.catalog.core.usecase.listproductsbyrestaurant.ListProductsByRestaurantUseCase;
import br.com.fiap.catalog.core.usecase.updateproduct.UpdateProductUseCase;

@Configuration
public class UseCaseConfig {

    @Bean
    public ListProductsUseCase listProductsUseCase(ProductRepositoryPort repo) {
        return new ListProductsUseCase(repo);
    }

    @Bean
    public ListProductsByRestaurantUseCase listProductsByRestaurantUseCase(ProductRepositoryPort repo) {
        return new ListProductsByRestaurantUseCase(repo);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepositoryPort repo) {
        return new GetProductUseCase(repo);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort repo) {
        return new CreateProductUseCase(repo);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepositoryPort repo) {
        return new UpdateProductUseCase(repo);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepositoryPort repo) {
        return new DeleteProductUseCase(repo);
    }
}
