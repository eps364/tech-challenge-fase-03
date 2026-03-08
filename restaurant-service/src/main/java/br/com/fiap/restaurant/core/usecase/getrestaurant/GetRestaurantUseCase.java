package br.com.fiap.restaurant.core.usecase.getrestaurant;

import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class GetRestaurantUseCase {

    private final RestauranteRepositoryPort repo;

    public GetRestaurantUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public RestaurantResponse execute(UUID id) {
        Restaurante r = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        return toResponse(r);
    }

    private RestaurantResponse toResponse(Restaurante r) {
        return new RestaurantResponse(r.getId(), r.getNome(), r.isAtivo(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners());
    }
}
