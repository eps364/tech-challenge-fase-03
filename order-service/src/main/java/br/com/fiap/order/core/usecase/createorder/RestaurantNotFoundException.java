package br.com.fiap.order.core.usecase.createorder;

import java.util.UUID;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(UUID restaurantId) {
        super("Restaurant not found: " + restaurantId);
    }
}
