package br.com.fiap.restaurant.core.usecase.getrestaurant;

import java.util.UUID;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(UUID id) {
        super("Restaurant not found: " + id);
    }
}
