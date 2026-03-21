package br.com.fiap.orchestrator.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final BigDecimal price;
    private final UUID restaurantId;
    private final String foodType;

    public Product(UUID id, String name, BigDecimal price, UUID restaurantId, String foodType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.foodType = foodType;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getFoodType() {
        return foodType;
    }
}