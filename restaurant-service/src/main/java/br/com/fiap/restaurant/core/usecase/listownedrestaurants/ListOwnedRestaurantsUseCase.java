package br.com.fiap.restaurant.core.usecase.listownedrestaurants;

import java.util.List;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class ListOwnedRestaurantsUseCase {

    private final RestauranteRepositoryPort repo;

    public ListOwnedRestaurantsUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public List<RestaurantResponse> execute(UUID ownerId) {
        return repo.findAll().stream()
                .filter(r -> r.getOwners().contains(ownerId))
                .map(this::toResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurante r) {
        return new RestaurantResponse(r.getId(), r.getNome(), r.isAtivo(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), false);
    }
}
