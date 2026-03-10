package br.com.fiap.order.core.gateway;

import java.util.UUID;

public interface RestaurantClientPort {
    void validateExists(UUID restaurantId);
}
