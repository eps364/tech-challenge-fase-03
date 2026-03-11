package br.com.fiap.restaurant.core.usecase;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class GetRestaurantUseCase {

    private final RestaurantRepositoryPort repo;

    public GetRestaurantUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public RestaurantResponse execute(UUID id) {
        Restaurant r = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        return toResponse(r);
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
