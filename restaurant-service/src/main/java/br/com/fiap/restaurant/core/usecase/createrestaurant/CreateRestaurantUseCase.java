package br.com.fiap.restaurant.core.usecase.createrestaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class CreateRestaurantUseCase {

    private final RestauranteRepositoryPort repo;
    private final KeycloakAdminPort keycloakAdmin;

    public CreateRestaurantUseCase(RestauranteRepositoryPort repo, KeycloakAdminPort keycloakAdmin) {
        this.repo = repo;
        this.keycloakAdmin = keycloakAdmin;
    }

    public RestaurantResponse execute(RestaurantRequest req, UUID creatorId, boolean isAdmin) {
        List<UUID> owners = creatorId != null ? new ArrayList<>(List.of(creatorId)) : new ArrayList<>();
        Restaurante r = new Restaurante(
                UUID.randomUUID(),
                req.nome(),
                req.ativo(),
                req.street(), req.number(), req.district(), req.complement(),
                req.city(), req.state(), req.zipCode(),
                owners
        );
        Restaurante saved = repo.save(r);
        boolean ownerRoleAssigned = false;
        if (creatorId != null && !isAdmin) {
            keycloakAdmin.assignOwnerRole(creatorId);
            ownerRoleAssigned = true;
        }
        return toResponse(saved, ownerRoleAssigned);
    }

    private RestaurantResponse toResponse(Restaurante r, boolean refreshTokenRequired) {
        return new RestaurantResponse(r.getId(), r.getNome(), r.isAtivo(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), refreshTokenRequired);
    }
}
