package br.com.fiap.restaurant.core.usecase.updaterestaurant;

import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;
import br.com.fiap.restaurant.core.usecase.getrestaurant.RestaurantAccessDeniedException;
import br.com.fiap.restaurant.core.usecase.getrestaurant.RestaurantNotFoundException;

public class UpdateRestaurantUseCase {

    private final RestauranteRepositoryPort repo;

    public UpdateRestaurantUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public RestaurantResponse execute(UUID id, RestaurantRequest req, UUID callerId, boolean isAdmin) {
        Restaurante existing = repo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        if (!isAdmin && !existing.getOwners().contains(callerId)) {
            throw new RestaurantAccessDeniedException(id);
        }
        Restaurante updated = new Restaurante(
                id,
                req.nome(),
                req.ativo(),
                req.street(), req.number(), req.district(), req.complement(),
                req.city(), req.state(), req.zipCode(),
                existing.getOwners()
        );
        Restaurante saved = repo.save(updated);
        return toResponse(saved);
    }

    private RestaurantResponse toResponse(Restaurante r) {
        return new RestaurantResponse(r.getId(), r.getNome(), r.isAtivo(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners());
    }
}
