package br.com.fiap.restaurant.core.usecase;
import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;
import br.com.fiap.restaurant.core.usecase.RestaurantAccessDeniedException;import br.com.fiap.restaurant.core.usecase.RestaurantNotFoundException;
import java.util.UUID;

public class DeleteRestaurantUseCase {

    private final RestaurantRepositoryPort repo;

    public DeleteRestaurantUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public void execute(UUID id, UUID callerId, boolean isAdmin) {
        Restaurant existing = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        if (!isAdmin && !existing.getOwners().contains(callerId)) {
            throw new RestaurantAccessDeniedException(id);
        }
        repo.delete(id);
    }
}
