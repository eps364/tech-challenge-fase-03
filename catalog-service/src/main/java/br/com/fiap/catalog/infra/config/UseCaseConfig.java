package br.com.fiap.catalog.infra.config;

import br.com.fiap.catalog.core.gateway.ProductRepositoryPort;
import br.com.fiap.catalog.core.usecase.CreateProductUseCase;
import br.com.fiap.catalog.core.usecase.DeleteProductUseCase;
import br.com.fiap.catalog.core.usecase.GetProductUseCase;
import br.com.fiap.catalog.core.usecase.ListProductsByRestaurantUseCase;
import br.com.fiap.catalog.core.usecase.ListProductsUseCase;
import br.com.fiap.catalog.core.usecase.ResolveProductsUseCase;
import br.com.fiap.catalog.core.usecase.UpdateProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public ResolveProductsUseCase resolveProductsUseCase(ProductRepositoryPort repo) {
        return new ResolveProductsUseCase(repo);
    }
}
