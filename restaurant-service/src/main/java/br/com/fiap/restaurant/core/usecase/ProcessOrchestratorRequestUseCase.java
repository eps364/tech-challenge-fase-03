package br.com.fiap.restaurant.core.usecase;

import java.util.Map;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurant;
import br.com.fiap.restaurant.core.gateway.RestaurantRepositoryPort;

public class ProcessOrchestratorRequestUseCase {

    private final RestaurantRepositoryPort repo;

    public ProcessOrchestratorRequestUseCase(RestaurantRepositoryPort repo) {
        this.repo = repo;
    }

    public ProcessOrchestratorRequestResult execute(ProcessOrchestratorRequestCommand cmd) {

        if (isRestaurantActivate(cmd.type())) {
            UUID restaurantId = resolveRestaurantId(cmd.payload());

            Restaurant restaurant = repo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

            Restaurant updatedRestaurant = restaurant.activate();
            repo.save(updatedRestaurant);

            return new ProcessOrchestratorRequestResult(
                    cmd.correlationId(),
                "RESTAURANT_ACTIVATED",
                Map.of("restaurantId", updatedRestaurant.getId().toString(), "active", updatedRestaurant.isActive())
            );
        }

        return new ProcessOrchestratorRequestResult(
                cmd.correlationId(),
            "RESTAURANTS_ACK",
            Map.of("message", "Unhandled type: " + cmd.type())
        );
    }

    private boolean isRestaurantActivate(String type) {
        return "RESTAURANT_ACTIVATE".equals(type) || "RESTAURANTE_ATIVAR".equals(type);
    }

    private UUID resolveRestaurantId(Map<String, Object> payload) {
        Object restaurantId = payload.get("restaurantId");
        if (restaurantId == null) {
            restaurantId = payload.get("restauranteId");
        }
        return UUID.fromString(String.valueOf(restaurantId));
    }
}