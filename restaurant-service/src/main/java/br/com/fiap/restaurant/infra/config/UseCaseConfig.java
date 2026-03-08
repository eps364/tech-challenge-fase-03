package br.com.fiap.restaurant.infra.config;

import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.addowner.AddOwnerToRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.createrestaurant.CreateRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.deleterestaurant.DeleteRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.getrestaurant.GetRestaurantUseCase;
import br.com.fiap.restaurant.core.usecase.listrestaurants.ListRestaurantsUseCase;
import br.com.fiap.restaurant.core.usecase.processar_requisicao_orquestrador.ProcessarRequisicaoOrquestradorUseCase;
import br.com.fiap.restaurant.core.usecase.updaterestaurant.UpdateRestaurantUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessarRequisicaoOrquestradorUseCase processarRequisicaoOrquestradorUseCase(
            RestauranteRepositoryPort repo) {
        return new ProcessarRequisicaoOrquestradorUseCase(repo);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestauranteRepositoryPort repo) {
        return new ListRestaurantsUseCase(repo);
    }

    @Bean
    public GetRestaurantUseCase getRestaurantUseCase(RestauranteRepositoryPort repo) {
        return new GetRestaurantUseCase(repo);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestauranteRepositoryPort repo,
                                                           KeycloakAdminPort keycloakAdmin) {
        return new CreateRestaurantUseCase(repo, keycloakAdmin);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestauranteRepositoryPort repo) {
        return new UpdateRestaurantUseCase(repo);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestauranteRepositoryPort repo) {
        return new DeleteRestaurantUseCase(repo);
    }

    @Bean
    public AddOwnerToRestaurantUseCase addOwnerToRestaurantUseCase(RestauranteRepositoryPort repo,
                                                                    KeycloakAdminPort keycloakAdminPort) {
        return new AddOwnerToRestaurantUseCase(repo, keycloakAdminPort);
    }
}
