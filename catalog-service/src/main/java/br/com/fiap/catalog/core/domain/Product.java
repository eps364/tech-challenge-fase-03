package br.com.fiap.catalog.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final UUID restaurantId;
    private final String foodType;

    public Product(Long id, String name, BigDecimal price, UUID restaurantId, String foodType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.foodType = foodType;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public UUID getRestaurantId() { return restaurantId; }
    public String getFoodType() { return foodType; }
}
