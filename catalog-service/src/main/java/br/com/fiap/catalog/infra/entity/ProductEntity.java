package br.com.fiap.catalog.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;

    @Column(name = "food_type", nullable = false)
    private String foodType;

    protected ProductEntity() {
    }

    public ProductEntity(UUID id, String name, BigDecimal price, UUID restaurantId, String foodType) {
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

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
