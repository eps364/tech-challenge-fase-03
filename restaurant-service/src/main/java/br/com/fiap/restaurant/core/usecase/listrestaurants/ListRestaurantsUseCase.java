package br.com.fiap.restaurant.core.usecase.listrestaurants;

import java.util.List;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class ListRestaurantsUseCase {

    private final RestauranteRepositoryPort repo;

    public ListRestaurantsUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public List<RestaurantResponse> execute() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurante r) {
        return new RestaurantResponse(r.getId(), r.getNome(), r.isAtivo(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners());
    }
}
