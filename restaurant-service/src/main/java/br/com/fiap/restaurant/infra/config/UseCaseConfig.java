package br.com.fiap.restaurant.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;
import br.com.fiap.restaurant.core.usecase.AddOwnerToRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.CreateRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.DeleteRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.GetRestaurantUseCase;import br.com.fiap.restaurant.core.usecase.ListOwnedRestaurantsUseCase;import br.com.fiap.restaurant.core.usecase.ListRestaurantsUseCase;import br.com.fiap.restaurant.core.usecase.ProcessOrchestratorRequestUseCase;import br.com.fiap.restaurant.core.usecase.UpdateRestaurantUseCase;
@Configuration
public class UseCaseConfig {

    @Bean
    public ProcessOrchestratorRequestUseCase processOrchestratorRequestUseCase(
            RestaurantRepositoryPort repo) {
        return new ProcessOrchestratorRequestUseCase(repo);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantRepositoryPort repo) {
        return new ListRestaurantsUseCase(repo);
    }

    @Bean
    public ListOwnedRestaurantsUseCase listOwnedRestaurantsUseCase(RestaurantRepositoryPort repo) {
        return new ListOwnedRestaurantsUseCase(repo);
    }

    @Bean
    public GetRestaurantUseCase getRestaurantUseCase(RestaurantRepositoryPort repo) {
        return new GetRestaurantUseCase(repo);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepositoryPort repo,
                                                           KeycloakAdminPort keycloakAdmin) {
        return new CreateRestaurantUseCase(repo, keycloakAdmin);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantRepositoryPort repo) {
        return new UpdateRestaurantUseCase(repo);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepositoryPort repo) {
        return new DeleteRestaurantUseCase(repo);
    }

    @Bean
    public AddOwnerToRestaurantUseCase addOwnerToRestaurantUseCase(RestaurantRepositoryPort repo,
                                                                    KeycloakAdminPort keycloakAdminPort) {
        return new AddOwnerToRestaurantUseCase(repo, keycloakAdminPort);
    }
}
