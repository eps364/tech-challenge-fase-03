package br.com.fiap.restaurant.core.usecase;
import java.util.List;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class ListRestaurantsUseCase {

    private final RestaurantRepositoryPort repo;

    public ListRestaurantsUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public List<RestaurantResponse> execute() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurant r) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
