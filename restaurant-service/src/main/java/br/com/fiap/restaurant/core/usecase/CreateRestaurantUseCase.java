package br.com.fiap.restaurant.core.usecase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.dto.RestaurantRequest;
import br.com.fiap.restaurant.core.dto.RestaurantResponse;
import br.com.fiap.restaurant.core.gateway.KeycloakAdminPort;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class CreateRestaurantUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateRestaurantUseCase.class);

    private final RestaurantRepositoryPort repo;
    private final KeycloakAdminPort keycloakAdmin;

    public CreateRestaurantUseCase(RestaurantRepositoryPort repo, KeycloakAdminPort keycloakAdmin) {
        this.repo = repo;
        this.keycloakAdmin = keycloakAdmin;
    }

    public RestaurantResponse execute(RestaurantRequest req, UUID creatorId, boolean isAdmin) {
        logger.info("Creating restaurant '{}' by creator {} (admin: {})", req.name(), creatorId, isAdmin);
        List<UUID> owners = creatorId != null ? new ArrayList<>(List.of(creatorId)) : new ArrayList<>();
        Restaurant r = Restaurant.create(
                req.name(),
                req.street(), req.number(), req.district(), req.complement(),
                req.city(), req.state(), req.zipCode(),
                owners
        );
        Restaurant saved = repo.save(r);
        logger.info("Restaurant saved with id {}", saved.getId());
        boolean ownerRoleAssigned = false;
        if (creatorId != null && !isAdmin) {
            keycloakAdmin.assignOwnerRole(creatorId);
            ownerRoleAssigned = true;
            logger.info("Owner role assigned to user {}", creatorId);
        }
        return toResponse(saved, ownerRoleAssigned);
    }

    private RestaurantResponse toResponse(Restaurant r, boolean refreshTokenRequired) {
        return new RestaurantResponse(r.getId(), r.getName(), r.isActive(),
                r.getStreet(), r.getNumber(), r.getDistrict(), r.getComplement(),
                r.getCity(), r.getState(), r.getZipCode(), r.getOwners(), refreshTokenRequired);
    }
}
