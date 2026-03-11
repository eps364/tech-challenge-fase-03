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
        validate(name, price, restaurantId, foodType);
        this.id = id;
        this.name = name.trim();
        this.price = price;
        this.restaurantId = restaurantId;
        this.foodType = foodType.trim();
    }

    public static Product create(String name, BigDecimal price, UUID restaurantId, String foodType) {
        return new Product(null, name, price, restaurantId, foodType);
    }

    private void validate(String name, BigDecimal price, UUID restaurantId, String foodType) {
        if (name == null || name.isBlank()) throw new ValidationException("name", "The name is required");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("price", "The price must be greater than zero");
        }
        if (restaurantId == null) throw new ValidationException("restaurantId", "The restaurant id is required");
        if (foodType == null || foodType.isBlank()) throw new ValidationException("foodType", "The food type is required");
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public UUID getRestaurantId() { return restaurantId; }
    public String getFoodType() { return foodType; }
}
