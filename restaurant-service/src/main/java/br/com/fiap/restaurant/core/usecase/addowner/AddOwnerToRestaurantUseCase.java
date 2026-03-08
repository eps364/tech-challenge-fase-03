package br.com.fiap.restaurant.core.usecase.addowner;

import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.getrestaurant.RestaurantNotFoundException;

public class AddOwnerToRestaurantUseCase {

    private final RestauranteRepositoryPort repository;
    private final KeycloakAdminPort keycloakAdminPort;

    public AddOwnerToRestaurantUseCase(RestauranteRepositoryPort repository,
                                       KeycloakAdminPort keycloakAdminPort) {
        this.repository = repository;
        this.keycloakAdminPort = keycloakAdminPort;
    }

    public RestaurantResponse execute(UUID restaurantId, UUID userId) {
        Restaurante restaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        Restaurante updated = restaurant.addOwner(userId);
        Restaurante saved = repository.save(updated);

        keycloakAdminPort.assignOwnerRole(userId);

        return new RestaurantResponse(
                saved.getId(),
                saved.getNome(),
                saved.isAtivo(),
                saved.getStreet(),
                saved.getNumber(),
                saved.getDistrict(),
                saved.getComplement(),
                saved.getCity(),
                saved.getState(),
                saved.getZipCode(),
                saved.getOwners(),
                true
        );
    }
}
