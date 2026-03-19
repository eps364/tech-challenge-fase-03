package br.com.fiap.orchestrator.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void constructor_and_getters_shouldWork() {
        UUID id = UUID.randomUUID();
        String name = "Salad";
        BigDecimal price = BigDecimal.valueOf(12);
        UUID restaurantId = UUID.randomUUID();
        String foodType = "Vegetarian";
        Product product = new Product(id, name, price, restaurantId, foodType);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
        assertEquals(restaurantId, product.getRestaurantId());
        assertEquals(foodType, product.getFoodType());
    }
}
