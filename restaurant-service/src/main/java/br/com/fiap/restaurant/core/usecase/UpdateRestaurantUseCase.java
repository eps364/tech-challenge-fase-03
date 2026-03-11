package br.com.fiap.restaurant.core.usecase;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;
import br.com.fiap.restaurant.core.usecase.RestaurantAccessDeniedException;import br.com.fiap.restaurant.core.usecase.RestaurantNotFoundException;
public class UpdateRestaurantUseCase {

    private final RestaurantRepositoryPort repo;

    public UpdateRestaurantUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public RestaurantResponse execute(UUID id, RestaurantRequest req, UUID callerId, boolean isAdmin) {
        Restaurant existing = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        if (!isAdmin && !existing.getOwners().contains(callerId)) {
            throw new RestaurantAccessDeniedException(id);
        }
        Restaurant updated = new Restaurant(
                id,
                req.name(),
                req.active(),
                req.street(), req.number(), req.district(), req.complement(),
                req.city(), req.state(), req.zipCode(),
                existing.getOwners()
        );
        Restaurant saved = repo.save(updated);
        return toResponse(saved);
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
