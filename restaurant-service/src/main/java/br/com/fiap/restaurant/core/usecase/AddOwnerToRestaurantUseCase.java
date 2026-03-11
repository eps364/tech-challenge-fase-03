package br.com.fiap.restaurant.core.usecase;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;
import br.com.fiap.restaurant.core.usecase.RestaurantNotFoundException;
public class AddOwnerToRestaurantUseCase {

    private final RestaurantRepositoryPort repository;
    private final KeycloakAdminPort keycloakAdminPort;

    public AddOwnerToRestaurantUseCase(RestaurantRepositoryPort repository,
                                       KeycloakAdminPort keycloakAdminPort) {
        this.repository = repository;
        this.keycloakAdminPort = keycloakAdminPort;
    }

    public RestaurantResponse execute(UUID restaurantId, UUID userId) {
        Restaurant restaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        Restaurant updated = restaurant.addOwner(userId);
        Restaurant saved = repository.save(updated);

        keycloakAdminPort.assignOwnerRole(userId);

        return new RestaurantResponse(
                saved.getId(),
                saved.getName(),
                saved.isActive(),
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
