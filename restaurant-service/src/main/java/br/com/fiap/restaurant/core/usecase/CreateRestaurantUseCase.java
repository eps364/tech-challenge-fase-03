package br.com.fiap.restaurant.core.usecase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class CreateRestaurantUseCase {

    private final RestaurantRepositoryPort repo;
    private final KeycloakAdminPort keycloakAdmin;

    public CreateRestaurantUseCase(RestaurantRepositoryPort repo, KeycloakAdminPort keycloakAdmin) {
        this.repo = repo;
        this.keycloakAdmin = keycloakAdmin;
    }

    public RestaurantResponse execute(RestaurantRequest req, UUID creatorId, boolean isAdmin) {
        List<UUID> owners = creatorId != null ? new ArrayList<>(List.of(creatorId)) : new ArrayList<>();
        Restaurant r = new Restaurant(
                UUID.randomUUID(),
                req.name(),
                req.active(),
                req.street(), req.number(), req.district(), req.complement(),
                req.city(), req.state(), req.zipCode(),
                owners
        );
        Restaurant saved = repo.save(r);
        boolean ownerRoleAssigned = false;
        if (creatorId != null && !isAdmin) {
            keycloakAdmin.assignOwnerRole(creatorId);
            ownerRoleAssigned = true;
        }
        return toResponse(saved, ownerRoleAssigned);
    }

    private RestaurantResponse toResponse(Restaurant r, boolean refreshTokenRequired) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), refreshTokenRequired);
    }
}
