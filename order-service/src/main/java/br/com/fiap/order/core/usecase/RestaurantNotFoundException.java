package br.com.fiap.order.core.usecase;
import java.util.UUID;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(UUID restaurantId) {
        super("Restaurant not found: " + restaurantId);
    }
}
