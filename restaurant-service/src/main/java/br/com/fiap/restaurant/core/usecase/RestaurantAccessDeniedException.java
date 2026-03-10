package br.com.fiap.restaurant.core.usecase;
import java.util.UUID;

public class RestaurantAccessDeniedException extends RuntimeException {
    public RestaurantAccessDeniedException(UUID restaurantId) {
        super("Access denied to restaurant: " + restaurantId);
    }
}
