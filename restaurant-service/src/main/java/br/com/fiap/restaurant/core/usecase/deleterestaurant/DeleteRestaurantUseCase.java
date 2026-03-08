package br.com.fiap.restaurant.core.usecase.deleterestaurant;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.getrestaurant.RestaurantAccessDeniedException;
import br.com.fiap.restaurant.core.usecase.getrestaurant.RestaurantNotFoundException;

import java.util.UUID;

public class DeleteRestaurantUseCase {

    private final RestauranteRepositoryPort repo;

    public DeleteRestaurantUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(UUID id, UUID callerId, boolean isAdmin) {
        Restaurante existing = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        if (!isAdmin && !existing.getOwners().contains(callerId)) {
            throw new RestaurantAccessDeniedException(id);
        }
        repo.delete(id);
    }
}
