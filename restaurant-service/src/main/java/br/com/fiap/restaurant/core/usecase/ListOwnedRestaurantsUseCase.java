package br.com.fiap.restaurant.core.usecase;
import java.util.List;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class ListOwnedRestaurantsUseCase {

    private final RestaurantRepositoryPort repo;

    public ListOwnedRestaurantsUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public List<RestaurantResponse> execute(UUID ownerId) {
        return repo.findAll().stream()
                .filter(r -> r.getOwners().contains(ownerId))
                .map(this::toResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
