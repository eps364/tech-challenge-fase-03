package br.com.fiap.restaurant.core.usecase;

import java.util.Map;
import java.util.UUID;

import br.com.fiap.restaurant.core.domain.Restaurante;
import br.com.fiap.restaurant.core.gateway.RestauranteRepositoryPort;

public class ProcessOrchestratorRequestUseCase {

    private final RestauranteRepositoryPort repo;

    public ProcessOrchestratorRequestUseCase(RestauranteRepositoryPort repo) {
        this.repo = repo;
    }

    public ProcessOrchestratorRequestResult execute(ProcessOrchestratorRequestCommand cmd) {

        if (isRestaurantActivate(cmd.type())) {
            UUID restaurantId = resolveRestaurantId(cmd.payload());

            Restaurante restaurant = repo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

            Restaurante updatedRestaurant = restaurant.ativar();
            repo.save(updatedRestaurant);

            return new ProcessOrchestratorRequestResult(
                    cmd.correlationId(),
                "RESTAURANT_ACTIVATED",
                Map.of("restaurantId", updatedRestaurant.getId().toString(), "active", updatedRestaurant.isAtivo())
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